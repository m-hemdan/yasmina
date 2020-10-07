package com.example.yasmina;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterHorBack extends RecyclerView.Adapter<AdapterHorBack.ViewHolder> {
  List<String> mImgArr;
    public AdapterHorBack(List<String> imgArr)
    {
        mImgArr=imgArr;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item1, parent, false);
        AdapterHorBack.ViewHolder viewHolder = new AdapterHorBack.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String currentUrl=mImgArr.get(position);
        Picasso.get().load(currentUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImgArr.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.photo_horizontal_image);
        }
    }
}
