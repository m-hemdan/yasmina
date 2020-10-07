package com.example.yasmina;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Application {
    private ArrayList<ModelProduct> myProducts = new ArrayList<ModelProduct>();
    private  ModelCard myCart = new ModelCard();


    public ModelProduct getProducts(int pPosition) {

        return myProducts.get(pPosition);
    }

    public void setProducts(ModelProduct Products) {

        myProducts.add(Products);

    }
    public ArrayList<ModelProduct> AllProduct()
    {

        return myProducts;
    }

    public ModelCard getCart() {

        return myCart;

    }

    public int getProductsArraylistSize() {

        return myProducts.size();
    }
    public LastPrice getTotalSum()
    {
        LastPrice lastPrice=new LastPrice();
        int totalSum=0;
        int sumDuration=0;
        for (int i=0;i<myProducts.size();i++)
        {
            totalSum+=Integer.valueOf(myProducts.get(i).getPriceProduct());


        }
        lastPrice=new LastPrice(totalSum,0,0);
        return  lastPrice;
    }



}
