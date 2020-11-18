package com.arora.developer.myapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<String> urls;
    Context context;


    public Adapter(ArrayList<String> urls, Context context) {
        this.urls = urls;
        this.context = context;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View menuItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.image_cell, parent, false);
        return new ViewHolder(menuItemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holderr = (ViewHolder) holder;
        Picasso.get().load(urls.get(position)).into(holderr.image);

        holderr.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailedActivity.class);
                i.putExtra("url", urls.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_cell0);

        }
    }
}
