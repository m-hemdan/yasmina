package com.example.yasmina;

public class ModelProduct {
    private String textProduct;
    private String subTextProduct;
   private String priceProduct;
   private int durationProduct;
   private String densityProduct;
   private String lengthProduct;
   private int timeWithOwner;
   String time;
   String selectDate;
   private String imageHair;

    public  ModelProduct()
    {}
    public ModelProduct(String mTextProduct,String mSubTextProduct,String mPriceProduct,int mDurationProduct)
    {
        textProduct=mTextProduct;
        subTextProduct=mSubTextProduct;
        priceProduct=mPriceProduct;
        durationProduct=mDurationProduct;
    }
    public ModelProduct(String mTextProduct,String mSubTextProduct,String mPriceProduct,int mDurationProduct,String mDensityProduct,String mLengthProduct,int mTimeWithOwner)
    {
        textProduct=mTextProduct;
        subTextProduct=mSubTextProduct;
        priceProduct=mPriceProduct;
        durationProduct=mDurationProduct;
        densityProduct=mDensityProduct;
      lengthProduct= mLengthProduct;
      timeWithOwner=mTimeWithOwner;
    }
    public ModelProduct(String mTextProduct,String mSubTextProduct,String mPriceProduct,int mDurationProduct,int mTimeWithOwner)
    {
        textProduct=mTextProduct;
        subTextProduct=mSubTextProduct;
        priceProduct=mPriceProduct;
        durationProduct=mDurationProduct;
        timeWithOwner=mTimeWithOwner;
    }
    public ModelProduct(String mTextProduct,String mSubTextProduct,String mPriceProduct,int mDurationProduct,int mTimeWithOwner,String mImageHair)
    {
        textProduct=mTextProduct;
        subTextProduct=mSubTextProduct;
        priceProduct=mPriceProduct;
        durationProduct=mDurationProduct;
        timeWithOwner=mTimeWithOwner;
        imageHair=mImageHair;
    }
    public ModelProduct(String mTextProduct,String mSubTextProduct,String mPriceProduct,int mDurationProduct,int mTimeWithOwner,String mTime,String mSelectDate,String mImg)
    {
        textProduct=mTextProduct;
        subTextProduct=mSubTextProduct;
        priceProduct=mPriceProduct;
        durationProduct=mDurationProduct;
        timeWithOwner=mTimeWithOwner;
        time=mTime;
        selectDate=mSelectDate;
        imageHair=mImg;

    }
    public String getTime() {
        return time;
    }

    public String getSelectDate() {
        return selectDate;
    }

    public int getTimeWithOwner() {
        return timeWithOwner;
    }

    public String getTextProduct() {
        return textProduct;
    }

    public String getSubTextProduct() {
        return subTextProduct;
    }
    public String getPriceProduct() {
        return priceProduct;
    }

    public int getDurationProduct() {
        return durationProduct;
    }

    public String getImageHair() {
        return imageHair;
    }

    public String getDensityProduct() {
        return densityProduct;
    }

    public String getLengthProduct() {
        return lengthProduct;
    }
}
