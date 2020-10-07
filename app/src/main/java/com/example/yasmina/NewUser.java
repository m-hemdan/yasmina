package com.example.yasmina;

public class NewUser {
    String mEmail,mName,mPassword,mUserId;



    public  NewUser(String email,String name,String password,String userId)
    {
        mEmail=email;
        mName=name;
        mPassword=password;
        mUserId=userId;

    }

    public String getmEmail() {
        return mEmail;
    }
    public String getmName()
    {
        return mName;
    }
    public String getmPassword()
    {
        return mPassword;
    }
    public String getmUserId()
    {
        return mUserId;
    }
}
