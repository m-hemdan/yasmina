package com.example.yasmina;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;

public class AdapterCheckOut extends RecyclerView.Adapter<AdapterCheckOut.ViewHolder> {
    List<ModelProduct> mList;
    Context mContext;
     int totalSum=0;
     int totalHr=0;
     ViewHolder mHolder;
    Controller globalControl;
    ModelProduct currentPosition;


    public AdapterCheckOut(List<ModelProduct> list, Context context)
    {
        mList=list;
        mContext=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_checkout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        globalControl =(Controller)mContext.getApplicationContext();
        mHolder=holder;
         currentPosition=mList.get(position);
        Log.v("model product",currentPosition.getTextProduct());
        holder.name.setText(currentPosition.getTextProduct());
        holder.subText.setText(currentPosition.getSubTextProduct());
        holder.numHr.setText(currentPosition.getDurationProduct()+" HR  ");
        holder.price.setText(currentPosition.getPriceProduct()+"$");
        totalSum+=Integer.valueOf(currentPosition.getPriceProduct());
        totalHr+=Integer.valueOf(currentPosition.getDurationProduct());
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemFun();
            }
        });
        Picasso.get().load(currentPosition.getImageHair()).into(holder.imgHair, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    @Override
    public int getItemCount() {

        return mList.size();
    }
    public void deleteItemFun()
    {}

    public void notifyData(List<ModelProduct> listModelproduct) {
        mList=listModelproduct;
        notifyDataSetChanged();

    }
    public  void removeItem(int position) {
        mList.remove(position);
      /*  Controller globalControl=(Controller)mContext.getApplicationContext();
        globalControl.AllProduct().remove(position);*/
        Paper.book().write("modelproductList",mList);
        notifyItemRemoved(position);

        ((checkOut)mContext).calculatePriceCheckOut();

    }

    public void restoreItem(ModelProduct modelProduct, int position) {
        mList.add(position, modelProduct);
        notifyItemInserted(position);
        ((checkOut)mContext).calculatePriceCheckOut();



    }

    public List<ModelProduct> getData() {
        return mList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView subText;
            TextView price;
            TextView numHr;
            ImageView deleteItem,imgHair;
            TextView numOfHrTextView,numOfItemTextView,totalPriceTextView;
                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    name=(TextView)itemView.findViewById(R.id.product_title);
                    subText=(TextView)itemView.findViewById(R.id.product_Sub_title);
                    price=(TextView)itemView.findViewById(R.id.product_price);
                    deleteItem=(ImageView)itemView.findViewById(R.id.deleteItem);
                    numHr=(TextView)itemView.findViewById(R.id.num_of_hr);
                    imgHair=(ImageView)itemView.findViewById(R.id.product_image);


                }
    }
}
