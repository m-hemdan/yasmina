package com.example.yasmina;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HairColorFragment extends Fragment {
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    private myListAdapter Adapter;

    public HairColorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView= inflater.inflate(R.layout.fragment_hair_color, container, false);
         listView=(ListView)rootView.findViewById(R.id.listView);
        final List<Content> listContent=new ArrayList<>();
        Adapter=new myListAdapter(getContext(),R.layout.section_layout,listContent);
        Log.v("adapter",Adapter.toString());
        listView.setAdapter(Adapter);
        firebaseDatabase= FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("", "onChildAdded:" + dataSnapshot.getKey());
                Content comment = dataSnapshot.getValue(Content.class);
                Content contentMessage = dataSnapshot.getValue(Content.class);
                Adapter.add(contentMessage);
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
                Toast.makeText(getContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addChildEventListener(childEventListener);
       return rootView;
    }

}
