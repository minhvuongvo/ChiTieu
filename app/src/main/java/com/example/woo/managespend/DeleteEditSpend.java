package com.example.woo.managespend;

import android.content.Intent;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteEditSpend extends AppCompatActivity {

    ImageButton btnBack, btnEdit, btnDelete;
    TextView tvMoney, tvChoose, tvNote, tvTime, tvWith;
    ImageView imgChoose;

    public static final int REQUEST_EDIT = 4;
    public static final int RESULT_EDIT = 5;

    Intent mIntent;
    int Code, CodeSpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_edit_spend);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Xử lý Delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDelete();
            }
        });

        //Xử lý Edit
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyEdit();
                //dataToMain();
                //Toast.makeText(DeleteEditSpend.this, RMoney+RGroup+RNote+RWith+RTime, Toast.LENGTH_LONG).show();
            }
        });

    }


    private void xuLyEdit() {
        imgChoose.buildDrawingCache();
        Bitmap img = imgChoose.getDrawingCache();
        Intent nIntent = new Intent(DeleteEditSpend.this, Edit.class);
        nIntent.putExtra("Money", tvMoney.getText().toString());
        nIntent.putExtra("Note", tvNote.getText().toString());
        nIntent.putExtra("Time", tvTime.getText().toString());
        nIntent.putExtra("Group", tvChoose.getText().toString());
        nIntent.putExtra("With", tvWith.getText().toString());
        nIntent.putExtra("Image", img);
        nIntent.putExtra("Code", Code);
        nIntent.putExtra("CodeSpend", CodeSpend);
        startActivityForResult(nIntent, REQUEST_EDIT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_EDIT){
            String RMoney = data.getStringExtra("MoneyE");
            String RGroup = data.getStringExtra("GroupE");
            String RNote = data.getStringExtra("NoteE");
            String RWith = data.getStringExtra("WithE");
            String RTime = data.getStringExtra("TimeE");
            int Flag = data.getIntExtra("FlagE", -1);

            mIntent.putExtra("MMoney", RMoney);
            mIntent.putExtra("MNote", RNote);
            mIntent.putExtra("MGroup", RGroup);
            mIntent.putExtra("MTime", RTime);
            mIntent.putExtra("MWith", RWith);
            mIntent.putExtra("MCode", Code);
            mIntent.putExtra("MFlag", Flag);
            setResult(MainActivity.RESULT_EDIT1, mIntent);
            finish();

            //Toast.makeText(this, RMoney+RGroup+RNote+RWith+RTime, Toast.LENGTH_LONG).show();
        }
    }

    private void xuLyDelete() {
//        database.delete("Spend", "Code=?", new String[]{Code});
        mIntent.putExtra("Code_R", Code);
        setResult(MainActivity.RESULT_DELETE, mIntent);
        finish();
    }

    private void addControls() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);
        tvChoose = (TextView) findViewById(R.id.tvChoose);
        tvMoney = (TextView) findViewById(R.id.tvMoney);
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvWith = (TextView) findViewById(R.id.tvWith);
        imgChoose = (ImageView) findViewById(R.id.imgChoose);

        //Nhận dư liệu từ main
        mIntent = getIntent();
        Code = mIntent.getIntExtra("Code", -1);
        tvMoney.setText(String.valueOf(mIntent.getLongExtra("Money", 0)));
        tvChoose.setText(mIntent.getStringExtra("Group"));
        tvTime.setText(mIntent.getStringExtra("Time"));
        tvNote.setText(mIntent.getStringExtra("Note"));
        String with = mIntent.getStringExtra("With");
        if (with.isEmpty()){
            tvWith.setText(mIntent.getStringExtra("With"));
        }else tvWith.setText(mIntent.getStringExtra("With").substring(5));
        imgChoose.setImageResource(mIntent.getIntExtra("Image", 0));
        CodeSpend = mIntent.getIntExtra("CodeSpend", -1);
    }
}
