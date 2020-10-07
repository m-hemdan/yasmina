package com.example.yasmina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SorryNetwork extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorry_network);
        Button bt=(Button)findViewById(R.id.tryAgain);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetConnection internetConnection=new InternetConnection();
                if (InternetConnection.isConnected(getApplication())) {
                    Intent intent = new Intent(getApplication(), book.class);
                    startActivity(intent);
                }
            }
        });
    }
}
