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

import java.util.List;

import io.paperdb.Paper;

public class AdapterCheckOut extends RecyclerView.Adapter<AdapterCheckOut.ViewHolder> {
    List<ModelProduct> mList;
    Context mContext;
     int totalSum=0;
     int totalHr=0;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelProduct currentPosition=mList.get(position);
        Log.v("model product",currentPosition.getTextProduct());
        holder.name.setText(currentPosition.getTextProduct());
        holder.subText.setText(currentPosition.getSubTextProduct());
        holder.price.setText(currentPosition.getPriceProduct()+"$");
        totalSum+=Integer.valueOf(currentPosition.getPriceProduct());
        totalHr+=Integer.valueOf(currentPosition.getDurationProduct());
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemFun();
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

    }

    public void restoreItem(ModelProduct modelProduct, int position) {
        mList.add(position, modelProduct);
        notifyItemInserted(position);
    }
   public LastPrice calculatePrice()
   {
       for (int i=0;i<mList.size();i++)
       {
           totalSum+=Integer.valueOf(mList.get(i).getPriceProduct());
           totalHr+=Integer.valueOf(mList.get(i).getDurationProduct());
       }
       LastPrice lastPrice=new LastPrice(totalSum,totalHr,mList.size());
       return lastPrice;
   }
    public List<ModelProduct> getData() {
        return mList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView subText;
            TextView price;
            TextView mtotalPriceTextView,numofHrTextView,numOfItemTextView;
            ImageView deleteItem;
                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    name=(TextView)itemView.findViewById(R.id.name);
                    subText=(TextView)itemView.findViewById(R.id.sub_text);
                    price=(TextView)itemView.findViewById(R.id.price);
                    deleteItem=(ImageView)itemView.findViewById(R.id.deleteItem);



                }
    }
}
