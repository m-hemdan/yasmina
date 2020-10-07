package com.example.yasmina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
ListView listView;
FirebaseDatabase firebaseDatabase;
private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        listView=(ListView)findViewById(R.id.listView);
        List<Content> mList=new ArrayList<>();
        final ShowAdapter showAdapter=new ShowAdapter(this,R.layout.card_layout,mList);
        listView.setAdapter(showAdapter);
        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        ChildEventListener childEventListener = new ChildEventListener() {
                       @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("", "onChildAdded:" + dataSnapshot.getKey());
                Content comment = dataSnapshot.getValue(Content.class);
                           Content contentMessage = dataSnapshot.getValue(Content.class);
                           showAdapter.add(contentMessage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("", "onChildChanged:" + dataSnapshot.getKey());

                Content newComment = dataSnapshot.getValue(Content.class);
                String commentKey = dataSnapshot.getKey();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("", "onChildRemoved:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("", "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Content movedComment = dataSnapshot.getValue(Content.class);
                String commentKey = dataSnapshot.getKey();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addChildEventListener(childEventListener);
    }
}
