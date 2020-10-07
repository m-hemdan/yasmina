package com.example.yasmina;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.paperdb.Book;

public class OfflineNetwork {
    public void alertShowFunction(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Login Failed");
        builder.setMessage("Connection Failed.Please check your network connection and try again");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(context, "enter a text here", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }
}

