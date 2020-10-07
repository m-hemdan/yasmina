package com.example.yasmina;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Content {
    String text,img,time,subText,price;
    int duration,timeInHr;
   List<String>arrImage=new ArrayList<String>();
    public  Content()
    {}
    public Content(String mText,String mImg)
    {
        //System.arraycopy(mArrImage,0,arrImage,0,mArrImage.length);

        text=mText;
        img=mImg;
    }
    public Content(String mText,String mImg,List<String> mArrImage)
    {
        //System.arraycopy(mArrImage,0,arrImage,0,mArrImage.length);
       arrImage.addAll(mArrImage);

        text=mText;
        img=mImg;
    }
    public Content(String mText,String mImg,List<String> mArrImage,int mDuration, String mSubText ,String mPrice, int mTimeInHr)
    {
        //System.arraycopy(mArrImage,0,arrImage,0,mArrImage.length);
        arrImage.addAll(mArrImage);
        text=mText;
        img=mImg;
        duration=mDuration;
        subText=mSubText;
        price=mPrice;
        timeInHr=mTimeInHr;
    }
    public Content(String mText,String mImg,String mTime)
    {
        text=mText;
        img=mImg;
        time=mTime;
    }

    public String getImg() {
        return img;
    }

    public String getText() {
        return text;
    }
    public String getTime(){return time;}

    public int getDuration() {
        return duration;
    }

    public String getPrice() {
        return price;
    }

    public String getSubtext() {
        return subText;
    }

    public int getTimeInHr() {
        return timeInHr;
    }

    public List<String> getArrImage()
    {
        return arrImage;
    }
}
