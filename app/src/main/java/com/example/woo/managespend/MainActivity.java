package com.example.woo.managespend;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.Interface.ISpend;
import com.example.woo.adapter.CustomAdapter;
import com.example.woo.model.ListDisplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISpend {

    String DATABASE_NAME="dbSpend.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;

    public static final int REQUEST_INPUT = 1;
    public static final int RESULT_SAVE = 2;

    public static final int REQUEST_DELETE = 6;
    public static final int RESULT_DELETE = 7;
    public static final int RESULT_EDIT1 = 8;

    TextView tvTotal;

    RecyclerView rcvList;
    CustomAdapter spendAdapter;
    ArrayList<ListDisplay> listSpendd;

    int I;
    int Flag;

    DecimalFormat dcfp = new DecimalFormat("#,###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();

        addControls();
        addEvents();

        showAllListSpend();
    }


    //Show list trang chủ
    public void showAllListSpend() {
        //Bước 1: Mở CSDL trước:
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("Spend",null,null,null,null,null,null);
        //Cursor cursor2=database.rawQuery("select * from Contact",null);
        listSpendd.clear();
        int SumM = 0;
        while (cursor.moveToNext())
        {
            int Code=cursor.getInt(0);
            Long Money=cursor.getLong(1);
            String Group=cursor.getString(2);
            String Note=cursor.getString(3);
            String Time=cursor.getString(4);
            String With=cursor.getString(5);
            int FlagS=cursor.getInt(6);
            int Image = 0;

            if (!With.isEmpty())
                With=" với "+With;

            switch (FlagS){
                case SelectGroup.LIST_CHI:
                    for (int i = 0; i < 11; i++){
                        if (Group.equals(SelectGroup.nameListChi[i])) {
                            Image = SelectGroup.imageListChi[i];
                            SumM -= Money;
                            Money = -Money;
                        }
                    }
                    break;
                case SelectGroup.LIST_THU:
                    for (int i = 0; i < 6; i++){
                        if (Group.equals(SelectGroup.nameListThu[i])){
                            Image=SelectGroup.imageListThu[i];
                            SumM += Money;

                        }
                    }
                    break;
            }

            if (SumM > 0){
                tvTotal.setText(" + "+dcfp.format(SumM)+"$");
            }
            else tvTotal.setText(" "+dcfp.format(SumM)+"$");
            listSpendd.add(new ListDisplay(Code, Money, Group, With, Image, Time, Note, FlagS));
        }
        cursor.close();//đóng kết nối
        spendAdapter.notifyDataSetChanged();
    }

    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try
            {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Sao chép CSDL vào hệ thống thành công", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CopyDataBaseFromAsset() {
        try
        {
            InputStream myInput=getAssets().open(DATABASE_NAME);
            String outFileName = layDuongDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists())
            {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex)
        {
            Log.e("Loi_SaoChep",ex.toString());
        }
    }

    private String layDuongDanLuuTru() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    private void addControls() {
        //Tự sinh
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //-----------------------------------//

        tvTotal = (TextView) findViewById(R.id.Total);
        tvTotal.setText("0$");

        //Khởi tạo dữ liệu
        listSpendd = new ArrayList<>();

        //Khởi tạo Adapter
        spendAdapter = new CustomAdapter(listSpendd, this);
        //RecyclerView
        rcvList = (RecyclerView) findViewById(R.id.rcvList);

        //Vẽ theo chiều ngang
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvList.setLayoutManager(manager);

        //Tích hợp vào Adapter
        rcvList.setAdapter(spendAdapter);


    }

    private void addEvents() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddSpend.class), REQUEST_INPUT);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Nhận dữ liệu từ AddSpend
        if (requestCode == REQUEST_INPUT && resultCode == RESULT_SAVE){
            String Time = data.getStringExtra("Day");
            I = data.getIntExtra("Position", -1);
            Flag = data.getIntExtra("Flag", -1);
            String Money = data.getStringExtra("Money");
            String Note = data.getStringExtra("Note");
            String With = data.getStringExtra("With");

            switch (Flag){
                case SelectGroup.LIST_CHI:
                    String sql = "INSERT INTO Spend VALUES(null, ?, ?, ?, ?, ?, ?)";
                    SQLiteStatement statement = database.compileStatement(sql);
                    statement.clearBindings();

                    statement.bindLong(1, Long.parseLong(Money));
                    statement.bindString(2, SelectGroup.nameListChi[I]);
                    statement.bindString(3, Note);
                    statement.bindString(4, Time);
                    statement.bindString(5, With);
                    statement.bindLong(6, SelectGroup.LIST_CHI);

                    statement.executeInsert();
                    showAllListSpend();
                    Toast.makeText(this, "Đã thêm thành công!", Toast.LENGTH_LONG).show();
                    break;
                case SelectGroup.LIST_THU:
                    String sql1 = "INSERT INTO Spend VALUES(null, ?, ?, ?, ?, ?, ?)";
                    SQLiteStatement statement1 = database.compileStatement(sql1);
                    statement1.clearBindings();

                    statement1.bindLong(1, Long.parseLong(Money));
                    statement1.bindString(2, SelectGroup.nameListThu[I]);
                    statement1.bindString(3, Note);
                    statement1.bindString(4, Time);
                    statement1.bindString(5, With);
                    statement1.bindLong(6, SelectGroup.LIST_THU);

                    statement1.executeInsert();
                    showAllListSpend();
                    Toast.makeText(this, "Đã thêm thành công!", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        //Nhận dữ liệu từ DeleteEditSpend và xử lý Delete
        if (requestCode == REQUEST_DELETE && resultCode == RESULT_DELETE){
            String Code = String.valueOf(data.getIntExtra("Code_R", -1));
            database.delete("Spend", "Code=?", new String[]{Code});
            showAllListSpend();
            Toast.makeText(this, "Đã xóa thành công!", Toast.LENGTH_LONG).show();
        }

        //Nhận dữ liệu từ DeleteEditSpend và xử lý Update
        if (requestCode == REQUEST_DELETE && resultCode == RESULT_EDIT1){
            String Code = String.valueOf(data.getIntExtra("MCode", -1));
            String Money = data.getStringExtra("MMoney");
            String Group = data.getStringExtra("MGroup");
            String Time = data.getStringExtra("MTime");
            String Note = data.getStringExtra("MNote");
            String With = data.getStringExtra("MWith");
            int F = data.getIntExtra("MFlag", -1);
            //Log.i("TTD11", Code+"\n"+Money+"\n"+Group+"\n"+Time+"\n"+Note+"\n"+With+"\n"+F);

            String sql = "UPDATE Spend SET Money=?, Gr=?, Note=?, Time=?, With=?, CodeSpend=? WHERE Code=?";
            SQLiteStatement statement = database.compileStatement(sql);
            statement.clearBindings();

            statement.bindLong(1, Long.parseLong(Money));
            statement.bindString(2, Group);
            statement.bindString(3, Note);
            statement.bindString(4, Time);
            statement.bindString(5, With);
            statement.bindLong(6, F);
            statement.bindLong(7, Integer.parseInt(Code));

            statement.execute();
            showAllListSpend();
            Toast.makeText(this, "Đã sửa thành công!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //Setting phát triển sau
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Click hiển thị TC DSThu DSChi
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            showAllListSpend();
        } else if (id == R.id.nav_gallery) {
            showAllListChi();
        } else if (id == R.id.nav_slideshow) {
            showAllListThu();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Hiện danh sách các khoản thu nhập
    private void showAllListThu() {
        //Bước 1: Mở CSDL trước:
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("Spend",null,null,null,null,null,null);
        //Cursor cursor2=database.rawQuery("select * from Contact",null);
        listSpendd.clear();
        int SumT = 0;
        while (cursor.moveToNext())
        {
            int Code = cursor.getInt(0);
            Long Money=cursor.getLong(1);
            String Group=cursor.getString(2);
            String Note=cursor.getString(3);
            String Time=cursor.getString(4);
            String With=cursor.getString(5);
            int FlagS=cursor.getInt(6);
            int Image = 0;

            if (!With.isEmpty())
                With=" với "+With;

            switch (FlagS){
                case SelectGroup.LIST_CHI:
                    for (int i = 0; i < 11; i++){
                        if (Group.equals(SelectGroup.nameListChi[i])) {
                            Image = SelectGroup.imageListChi[i];
                            Money = -Money;
                        }
                    }
                    break;
                case SelectGroup.LIST_THU:
                    for (int i = 0; i < 6; i++){
                        if (Group.equals(SelectGroup.nameListThu[i])) {
                            Image = SelectGroup.imageListThu[i];
                            SumT += Money;

                        }
                    }
                    break;
            }

            //Log.i("SumT", SumT+"");
            if (SumT > 0){
                tvTotal.setText(" + "+dcfp.format(SumT)+"$");
            }
            else tvTotal.setText(" - "+dcfp.format(SumT)+"$");
            if (FlagS==SelectGroup.LIST_THU)
                listSpendd.add(new ListDisplay(Code, Money, Group, With, Image, Time, Note, FlagS));
        }
        cursor.close();//đóng kết nối
        spendAdapter.notifyDataSetChanged();
    }
    //Hiện danh sách các khoản chi tiêu
    private void showAllListChi() {
        //Bước 1: Mở CSDL trước:
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("Spend",null,null,null,null,null,null);
        //Cursor cursor2=database.rawQuery("select * from Contact",null);
        listSpendd.clear();
        int SumC = 0;
        while (cursor.moveToNext())
        {
            int Code = cursor.getInt(0);
            Long Money=cursor.getLong(1);
            String Group=cursor.getString(2);
            String Note=cursor.getString(3);
            String Time=cursor.getString(4);
            String With=cursor.getString(5);
            int FlagS=cursor.getInt(6);
            int Image = R.drawable.ic_cafe;

            if (!With.isEmpty())
                With=" với "+With;

            switch (FlagS){
                case SelectGroup.LIST_CHI:
                    for (int i = 0; i < 11; i++){
                        if (Group.equals(SelectGroup.nameListChi[i])) {
                            Image = SelectGroup.imageListChi[i];
                            SumC -= Money;
                            Money = -Money;
                        }
                    }
                    break;
                case SelectGroup.LIST_THU:
                    for (int i = 0; i < 6; i++){
                        if (Group.equals(SelectGroup.nameListThu[i])) {
                            Image = SelectGroup.imageListThu[i];
                            //Money = "+$ "+Money;
                        }
                    }
                    break;
            }
            if (SumC > 0){
                tvTotal.setText(" + "+dcfp.format(SumC)+"$");
            }
            else tvTotal.setText(" "+dcfp.format(SumC)+"$");
            //Log.i("SumC", SumC+"");
            if (FlagS==SelectGroup.LIST_CHI)
                listSpendd.add(new ListDisplay(Code, Money, Group, With, Image, Time, Note, FlagS));
        }
        cursor.close();//đóng kết nối
        spendAdapter.notifyDataSetChanged();
    }

    //Xử lý click danh sách các chi tiêu ở đây
    @Override
    public void onItemClick(int position) {
        Intent mIntent = new Intent(MainActivity.this, DeleteEditSpend.class);
        mIntent.putExtra("Code", spendAdapter.getListDisplay().get(position).getCode());
        mIntent.putExtra("Money", spendAdapter.getListDisplay().get(position).getMoney());
        mIntent.putExtra("Group", spendAdapter.getListDisplay().get(position).getGroup());
        mIntent.putExtra("Note", spendAdapter.getListDisplay().get(position).getNote());
        mIntent.putExtra("With", spendAdapter.getListDisplay().get(position).getWith());
        mIntent.putExtra("Time", spendAdapter.getListDisplay().get(position).getDay());
        mIntent.putExtra("Image", spendAdapter.getListDisplay().get(position).getImage());
        mIntent.putExtra("CodeSpend", spendAdapter.getListDisplay().get(position).getCodeSpend());
        startActivityForResult(mIntent, REQUEST_DELETE);
    }
}
