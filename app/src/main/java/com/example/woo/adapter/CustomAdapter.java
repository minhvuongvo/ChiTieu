package com.example.woo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.woo.Interface.ISpend;
import com.example.woo.managespend.R;
import com.example.woo.model.ListDisplay;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.mViewHolder>{

    DecimalFormat dcf = new DecimalFormat("#,###,###,###");

    private ArrayList<ListDisplay> listDisplay;
    private ISpend iSpend;

    public ArrayList<ListDisplay> getListDisplay() {
        return listDisplay;
    }

    public CustomAdapter(ArrayList<ListDisplay> listDisplay, ISpend iSpend) {
        this.iSpend = iSpend;
        this.listDisplay = listDisplay;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlist, parent, false);

        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
        holder.tvDay.setText(listDisplay.get(position).getDay());
        holder.tvGroup.setText(listDisplay.get(position).getGroup());
        holder.tvMoney.setText(dcf.format(listDisplay.get(position).getMoney()).toString()+"$");
        holder.imgImage.setImageResource(listDisplay.get(position).getImage());
        holder.tvNote.setText(listDisplay.get(position).getNote());
        holder.tvWith.setText(listDisplay.get(position).getWith());

        //Click item
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onItem_Click);

    }

    private View.OnClickListener onItem_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i =  Integer.parseInt(v.getTag().toString());
            iSpend.onItemClick(i);
        }
    };


    @Override
    public int getItemCount() {
        return listDisplay.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        TextView tvDay, tvMoney, tvNote, tvWith, tvGroup;
        ImageView imgImage;

        public mViewHolder(View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            imgImage = (ImageView) itemView.findViewById(R.id.imgImage);
            tvGroup = (TextView) itemView.findViewById(R.id.tvGroup);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
            tvNote = (TextView) itemView.findViewById(R.id.tvNote);
            tvWith = (TextView) itemView.findViewById(R.id.tvWith);
        }
    }

}