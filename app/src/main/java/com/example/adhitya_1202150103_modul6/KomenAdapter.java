package com.example.adhitya_1202150103_modul6;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//class adapter untuk row pada comment list
public class KomenAdapter extends RecyclerView.Adapter<KomenAdapter.MyViewHolder> {
    //deklarasi variable
    private List<KomenModel> listComment;

    //class viewholder untuk declare dan inisialisasi views pada row yang digunakan
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaTV, commentTV;
        public ImageView avatarIMG;

        public MyViewHolder(View view) {
            super(view);
            avatarIMG = (ImageView) view.findViewById(R.id.imgAvatar);
            namaTV = (TextView) view.findViewById(R.id.tvNama);
            commentTV = (TextView) view.findViewById(R.id.tvKomentar);
        }
    }

    //konstruktor untuk menerima data yang dikirimkan dari activity ke adapter
    public KomenAdapter(List<KomenModel> listComment) {
        this.listComment = listComment;
    }

    //create ke layout row yang dipilih
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    //binding antara data yang didapatkan ke dalam views
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        KomenModel food = listComment.get(position);
        holder.commentTV.setText(food.getComment());
        holder.namaTV.setText(food.getName());
    }

    //count data
    @Override
    public int getItemCount() {
        return listComment.size();
    }
}