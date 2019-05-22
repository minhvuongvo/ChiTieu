package com.example.woo.managespend;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static com.example.woo.managespend.AddSpend.REQUEST_CODE_INPUT;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Edit extends AppCompatActivity {

    ImageView imgChooseD;
    EditText edMoneyD, edNoteD, edWithD;
    TextView tvChooseD, tvTimeD;
    Button btnLuuD;

    Intent mIntent;

    int img;
    int flag;

    int Code;

    Calendar calendar=Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        addControls();
        addEvents();
    }

    private void addEvents() {
        //Đến activity Chọn nhóm
        tvChooseD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), SelectGroup.class);
                startActivityForResult(mIntent, AddSpend.REQUEST_CODE_INPUT);
            }
        });

        //Xử lý thời gian
        tvTimeD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDay();
            }
        });

        //Xử lý lưu
        btnLuuD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLuu();
            }
        });


    }

    private void xuLyLuu() {
        if (!TextUtils.isEmpty(edMoneyD.getText()) && !tvChooseD.getText().equals("Chọn nhóm")){
            if ((Integer.parseInt(edMoneyD.getText().toString()) > 0)){
                mIntent.putExtra("TimeE", tvTimeD.getText().toString());
                mIntent.putExtra("MoneyE", edMoneyD.getText().toString());
                mIntent.putExtra("NoteE", edNoteD.getText().toString());
                mIntent.putExtra("WithE", edWithD.getText().toString());
                mIntent.putExtra("GroupE", tvChooseD.getText().toString());
                mIntent.putExtra("FlagE", flag);
                //Log.i("Flag", String.valueOf(flag));
                setResult(DeleteEditSpend.RESULT_EDIT, mIntent);
                finish();
            }else Toast.makeText(this, "Vui lòng nhập lại số tiền  > 0", Toast.LENGTH_LONG).show();

        }else Toast.makeText(this, "Hãy nhập vào số tiền và chọn nhóm!", Toast.LENGTH_LONG).show();

    }

    private void addControls() {
        edMoneyD= (EditText) findViewById(R.id.edMoneyD);
        edNoteD= (EditText) findViewById(R.id.edNoteD);
        tvChooseD= (TextView) findViewById(R.id.tvChooseD);
        tvTimeD= (TextView) findViewById(R.id.tvTimeD);
        edWithD= (EditText) findViewById(R.id.edWithD);
        btnLuuD= (Button) findViewById(R.id.btnLuuD);

        calendar= Calendar.getInstance();
        tvTimeD.setText(sdf.format(calendar.getTime()));
        imgChooseD= (ImageView) findViewById(R.id.imgChooseD);
        //edMoneyD.addTextChangedListener(new AddSpend.NumberTextWatcher(edMoneyD));

        //Nhận dữ liệu từ DeleteEditSpend
        mIntent = getIntent();
        int Mn = Integer.parseInt(mIntent.getStringExtra("Money"));
        if (Mn > 0){
            edMoneyD.setText(mIntent.getStringExtra("Money"));
        }else edMoneyD.setText(mIntent.getStringExtra("Money").substring(1));

        tvChooseD.setText(mIntent.getStringExtra("Group"));
        imgChooseD.setImageBitmap((Bitmap)mIntent.getParcelableExtra("Image"));
        edNoteD.setText(mIntent.getStringExtra("Note"));
        edWithD.setText(mIntent.getStringExtra("With"));
        tvTimeD.setText(mIntent.getStringExtra("Time"));
        Code = mIntent.getIntExtra("Code", -1);
        flag = mIntent.getIntExtra("CodeSpend", -1);
    }

    private void xuLyDay() {
        DatePickerDialog.OnDateSetListener callback=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(calendar.YEAR, year);
                calendar.set(calendar.MONTH, month);
                calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
                tvTimeD.setText(sdf.format(calendar.getTime()));
            }
        };

        DatePickerDialog dt=new DatePickerDialog(
                Edit.this,
                callback,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH)
        );
        dt.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddSpend.REQUEST_CODE_INPUT){
            switch(resultCode){
                case AddSpend.RESULT_CODE_SAVE1:
                    img = data.getIntExtra("Position", 0);
                    flag = data.getIntExtra("Flag", 0);

                    switch (flag){
                        case SelectGroup.LIST_CHI:
                            tvChooseD.setText(SelectGroup.nameListChi[img]);
                            imgChooseD.setImageResource(SelectGroup.imageListChi[img]);
                            break;
                        case SelectGroup.LIST_THU:
                            tvChooseD.setText(SelectGroup.nameListThu[img]);
                            imgChooseD.setImageResource(SelectGroup.imageListThu[img]);
                            break;
                    }

                    break;
                case AddSpend.RESULT_CODE_SAVE2:
                    img = data.getIntExtra("Position", 0);
                    flag = data.getIntExtra("Flag", 0);

                    switch (flag){
                        case SelectGroup.LIST_CHI:
                            tvChooseD.setText(SelectGroup.nameListChi[img]);
                            imgChooseD.setImageResource(SelectGroup.imageListChi[img]);
                            break;
                        case SelectGroup.LIST_THU:
                            tvChooseD.setText(SelectGroup.nameListThu[img]);
                            imgChooseD.setImageResource(SelectGroup.imageListThu[img]);
                            break;
                    }
            }
        }
    }
}

