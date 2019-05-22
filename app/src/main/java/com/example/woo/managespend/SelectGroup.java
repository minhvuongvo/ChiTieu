package com.example.woo.managespend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.woo.adapter.SelectGroupAdapter;

public class SelectGroup extends AppCompatActivity {

    TabHost tabHost;
    ListView lvThu, lvChi;
    public static final int LIST_CHI=21;
    public static final int LIST_THU=22;

    public static final int [] imageListThu={
            R.drawable.ic_tien,
            R.drawable.ic_luong,
            R.drawable.ic_duoctang,
            R.drawable.ic_giohang,
            R.drawable.ic_vi,
            R.drawable.ic_chonnhom};
    public static final String [] nameListThu={
            "Thưởng",
            "Lương",
            "Được tặng",
            "Bán đồ",
            "Tiền lãi",
            "Khoản thu khác"};

    public static final int [] imageListChi={
            R.drawable.ic_an,
            R.drawable.ic_cafe,
            R.drawable.ic_mang,
            R.drawable.ic_thuenha,
            R.drawable.ic_taxi,
            R.drawable.ic_doxe,
            R.drawable.ic_xangdau,
            R.drawable.ic_nguoiyeu,
            R.drawable.ic_giaitri,
            R.drawable.ic_quatotnghiep,
            R.drawable.ic_chonnhom};
    public static final String [] nameListChi={
            "Ăn uống",
            "Cà phê",
            "Internet",
            "Thuê nhà",
            "Taxi",
            "Gửi xe",
            "Xăng dầu",
            "Người yêu",
            "Giải trí",
            "Giáo dục",
            "Khoản chi khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvThu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent mIntent = getIntent();
                mIntent.putExtra("Flag", LIST_THU);
                mIntent.putExtra("Position", i);
                setResult(AddSpend.RESULT_CODE_SAVE1, mIntent);
                finish();
            }
        });

        lvChi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent mIntent = getIntent();
                mIntent.putExtra("Flag", LIST_CHI);
                mIntent.putExtra("Position", i);
                setResult(AddSpend.RESULT_CODE_SAVE2, mIntent);
                finish();
            }
        });
    }


    private void addControls() {
        //Khởi tạo TabHost
        tabHost= (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setIndicator("CHI TIÊU");
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setIndicator("THU NHẬP");
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);
        //ListView
        lvThu= (ListView) findViewById(R.id.lvThu);
        lvChi= (ListView) findViewById(R.id.lvChi);

        lvThu.setAdapter(new SelectGroupAdapter(this, nameListThu, imageListThu));
        lvChi.setAdapter(new SelectGroupAdapter(this, nameListChi, imageListChi));
    }
}
