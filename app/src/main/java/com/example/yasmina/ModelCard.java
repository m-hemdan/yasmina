package com.example.yasmina;

import java.util.ArrayList;
import java.util.List;

public class ModelCard {

    private ArrayList<ModelProduct> cartProducts = new ArrayList<ModelProduct>();


    public ModelProduct getProducts(int pPosition) {

        return cartProducts.get(pPosition);
    }

    public void setProducts(ModelProduct Products) {

        cartProducts.add(Products);

    }

    public int getCartSize() {

        return cartProducts.size();

    }

    public boolean checkProductInCart(ModelProduct aProduct) {

        return cartProducts.contains(aProduct);

    }
}
