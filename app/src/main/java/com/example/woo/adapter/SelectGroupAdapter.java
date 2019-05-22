package com.example.woo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.woo.managespend.R;

/**
 * Created by Woo on 19/09/2017.
 */

public class SelectGroupAdapter extends ArrayAdapter<String>{
    String [] name;
    int [] img;
    Context mcontext;

    public SelectGroupAdapter(Context context, String[] nameList, int[] imgList) {
        super(context, R.layout.item);
        this.name=nameList;
        this.img=imgList;
        this.mcontext=context;
    }

    @Override
    public int getCount() {
        return name.length;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent,false);

            viewHolder.mName= (TextView) convertView.findViewById(R.id.tv);
            viewHolder.mImg= (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.mName.setText(name[position]);
        viewHolder.mImg.setImageResource(img[position]);

        return convertView;
    }

    static class ViewHolder{
        ImageView mImg;
        TextView mName;
    }
}
