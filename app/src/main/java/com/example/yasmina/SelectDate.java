package com.example.yasmina;

public class SelectDate {
    public String userId,day,timeInDay;

    public SelectDate()
    {}
    public SelectDate(String mUserId,String mDay, int mNumHr)
    {

        day=mDay;

        userId=mUserId;
    }

    public SelectDate(String mUserId,String mDay,String mTime)
    {

        day=mDay;

        userId=mUserId;
        timeInDay=mTime;
    }

    public String getDay() {
        return day;
    }
    public String getUserId()
    {
        return userId;
    }
    public String getTimeInDay()
    { return timeInDay;}
}
