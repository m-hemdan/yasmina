package com.example.yasmina;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class BadgeClass {


   public int setbadgeConnect(Context context) {
        Controller controller=(Controller)context.getApplicationContext();
        int mCartItemCount=controller.AllProduct().size();
     return  mCartItemCount;
    }
}
