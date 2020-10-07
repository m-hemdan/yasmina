package com.example.yasmina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class LastPrice {
    int sumTotalPrice=0;
    int sumNumOFHr=0;
    int numOfItem=0;
    public LastPrice()
    {}
 public LastPrice(int mSumTotalPrice,int mSumNumOFHr,int mNnumOfItem) {
   sumTotalPrice=mSumTotalPrice;
   sumNumOFHr=mSumNumOFHr;
   numOfItem=mNnumOfItem;
 }

    public int getSumTotalPrice() {
        return sumTotalPrice;
    }

    public int getSumNumOFHr() {
        return sumNumOFHr;
    }

    public int getNumOfItem() {
        return numOfItem;
    }
}
