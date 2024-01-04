/*
Developed as a part of hackathon solely - 'Hack Hurricane 1.0'
Author : Mahendra Kumar
Gmail: androiprogrammers@gmail.com
Linkedin: https://www.linkedin.com/in/mahendra-kumar-b5881324b/
*/

package com.androidai.chatgpttextdetection;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Ad_Images extends RecyclerView.Adapter<Ad_Images.CategoryViewHolder> {

    Context context;
    ArrayList<Bitmap> items;

    public Ad_Images(Context context, ArrayList<Bitmap> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.lay_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.iv1.setImageBitmap(items.get(position));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView iv1;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            iv1 = itemView.findViewById(R.id.image);

        }
    }

}
