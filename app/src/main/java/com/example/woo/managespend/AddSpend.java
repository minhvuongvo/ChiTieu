package com.example.woo.managespend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.Image;
import android.net.ParseException;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddSpend extends AppCompatActivity {

    public static final int REQUEST_CODE_INPUT=12;
    public static final int RESULT_CODE_SAVE1=13;
    public static final int RESULT_CODE_SAVE2=14;

    ImageView imgChoose;
    EditText edMoney, edNote, edWith;
    TextView tvChoose, tvTime;
    Button btnLuu;
    int img;
    int flag;

    Calendar calendar=Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spend);
        addControls();
        addEvents();
    }

    private void addEvents() {

        //Đến activity Chọn nhóm
        tvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddSpend.this, SelectGroup.class), REQUEST_CODE_INPUT);
            }
        });
        //Xử lý thời gian
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyTime();
            }
        });
        //Xử lý lưu
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLuu();
            }
        });

    }

    private void xuLyLuu() {
        if (!TextUtils.isEmpty(edMoney.getText()) && !tvChoose.getText().equals("Chọn nhóm")){
            if ((Integer.parseInt(edMoney.getText().toString()) > 0)) {
                Intent mIntent = getIntent();
                mIntent.putExtra("Day", tvTime.getText().toString());
                mIntent.putExtra("Position", img);
                mIntent.putExtra("Flag", flag);
                mIntent.putExtra("Money", edMoney.getText().toString());
                mIntent.putExtra("Note", edNote.getText().toString());
                mIntent.putExtra("With", edWith.getText().toString());
                setResult(MainActivity.RESULT_SAVE, mIntent);
                finish();
            }else Toast.makeText(this, "Vui lòng nhập lại số tiền > 0", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Hãy nhập vào số tiền và chọn nhóm!", Toast.LENGTH_LONG).show();
        }

    }

    //
    private void xuLyTime() {
        DatePickerDialog.OnDateSetListener callback=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(calendar.YEAR, year);
                calendar.set(calendar.MONTH, month);
                calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
                tvTime.setText(sdf.format(calendar.getTime()));
            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(
                AddSpend.this,
                callback,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INPUT){
            switch(resultCode){
                case RESULT_CODE_SAVE1:
                    img = data.getIntExtra("Position", 0);
                    flag = data.getIntExtra("Flag", 0);
                    switch (flag){
                        case SelectGroup.LIST_CHI:
                            tvChoose.setText(SelectGroup.nameListChi[img]);
                            imgChoose.setImageResource(SelectGroup.imageListChi[img]);
                            break;
                        case SelectGroup.LIST_THU:
                            tvChoose.setText(SelectGroup.nameListThu[img]);
                            imgChoose.setImageResource(SelectGroup.imageListThu[img]);
                            break;
                    }

                    break;
                case RESULT_CODE_SAVE2:
                    img = data.getIntExtra("Position", 0);
                    flag = data.getIntExtra("Flag", 0);
                    switch (flag){
                        case SelectGroup.LIST_CHI:
                            tvChoose.setText(SelectGroup.nameListChi[img]);
                            imgChoose.setImageResource(SelectGroup.imageListChi[img]);
                            break;
                        case SelectGroup.LIST_THU:
                            tvChoose.setText(SelectGroup.nameListThu[img]);
                            imgChoose.setImageResource(SelectGroup.imageListThu[img]);
                            break;
                    }
            }
        }
    }

    private void addControls() {
        edMoney= (EditText) findViewById(R.id.edMoney);
        edNote= (EditText) findViewById(R.id.edNote);
        tvChoose= (TextView) findViewById(R.id.tvChoose);
        tvTime= (TextView) findViewById(R.id.tvTime);
        edWith= (EditText) findViewById(R.id.edWith);
        btnLuu= (Button) findViewById(R.id.btnLuu);

        calendar=Calendar.getInstance();
        tvTime.setText(sdf.format(calendar.getTime()));
        imgChoose= (ImageView) findViewById(R.id.imgChoose);

        //edMoney.addTextChangedListener(new NumberTextWatcher(edMoney));

    }

    public static class NumberTextWatcher implements TextWatcher {

        private DecimalFormat df;
        private DecimalFormat dfnd;
        private boolean hasFractionalPart;

        private EditText et;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public NumberTextWatcher(EditText et)
        {
            df = new DecimalFormat("#,###.##");
            df.setDecimalSeparatorAlwaysShown(true);
            dfnd = new DecimalFormat("#,###");
            this.et = et;
            hasFractionalPart = false;
        }

        @SuppressWarnings("unused")
        private static final String TAG = "NumberTextWatcher";

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void afterTextChanged(Editable s)
        {
            et.removeTextChangedListener(this);

            try {
                int inilen, endlen;
                inilen = et.getText().length();

                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                if (hasFractionalPart) {
                    et.setText(df.format(n));
                } else {
                    et.setText(dfnd.format(n));
                }
                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel <= et.getText().length()) {
                    et.setSelection(sel);
                } else {
                    // place cursor at the end?
                    et.setSelection(et.getText().length() - 1);
                }
            } catch (NumberFormatException nfe) {
                // do nothing?
            } catch (ParseException e) {
                // do nothing?
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            et.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
            {
                hasFractionalPart = true;
            } else {
                hasFractionalPart = false;
            }
        }

    }
}
