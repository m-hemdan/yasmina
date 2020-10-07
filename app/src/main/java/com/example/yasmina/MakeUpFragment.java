package com.example.yasmina;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MakeUpFragment extends Fragment {
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    private myListAdapter Adapter;
    Button priceBtn;
    LinearLayout linearLayout;
    ImageView photoImageView;
    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_make_up, container, false);
        priceBtn=(Button)rootView.findViewById(R.id.addToCard);
        photoImageView=(ImageView)rootView.findViewById(R.id.photoImageView);
        ListView listView=(ListView)rootView.findViewById(R.id.listView);
        final List<Content> listContent=new ArrayList<>();
        Adapter=new myListAdapter(getContext(),R.layout.section_layout,listContent);
         Log.v("adapter",Adapter.toString());
        listView.setAdapter(Adapter);

        firebaseDatabase=FirebaseDatabase.getInstance();
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


       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"hi",Toast.LENGTH_LONG).show();
                final ImageView xImageView=(ImageView)view.findViewById(R.id.photoImageView);
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(xImageView, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(xImageView, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        xImageView.setImageResource(R.drawable.back);
                        oa2.start();
                    }
                });
                oa1.start();
            }
        });*/
       return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


}
