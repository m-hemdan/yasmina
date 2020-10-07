package com.example.yasmina;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HairFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    TextView durationTextView;
    private DatabaseReference mDatabase;
    private AdapterProtine Adapter;
    List<Content> listContent;
    ProgressBar progressBarProtine;
    RelativeLayout loadingRelativeLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InternetConnection internetConnection = new InternetConnection();
        if (!internetConnection.isConnected(getActivity())) {
            OfflineNetwork offlineNetwork = new OfflineNetwork();
            offlineNetwork.alertShowFunction(getActivity());

        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_protine, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_protine);
        progressBarProtine=(ProgressBar)rootView.findViewById(R.id.progressBarLoadingProtineX);
        loadingRelativeLayout=(RelativeLayout)rootView.findViewById(R.id.loadingPage);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        listContent = new ArrayList<>();

        Adapter = new AdapterProtine(listContent, getContext(), new AdapterProtine.OnItemClickListener() {
            @Override
            public void onItemClick(Content item) {

            }
        }, recyclerView);
        recyclerView.setAdapter(Adapter);
        new AsynClass().execute();
        return rootView;
    }


    public class AsynClass extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingRelativeLayout.setVisibility(View.VISIBLE);
            Log.d("preExecute", "preExecuite:");
            progressBarProtine.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("asasasa", "doInBackground:");
            firebaseDatabase = FirebaseDatabase.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference("HairTable");
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d("", "onChildAdded:" + dataSnapshot.getKey());
                    Content contentMessage = dataSnapshot.getValue(Content.class);
                    listContent.add(contentMessage);
                    Adapter.notifyData(listContent);
                    // Adapter.notifyItemInserted(listContent.size()-1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d("", "onChildChanged:" + dataSnapshot.getKey());
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d("", "onChildRemoved:" + dataSnapshot.getKey());
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d("", "onChildMoved:" + dataSnapshot.getKey());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("", "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(getContext(), "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            mDatabase.addChildEventListener(childEventListener);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBarProtine.setProgress(70);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingRelativeLayout.setVisibility(View.GONE);
            progressBarProtine.setVisibility(View.GONE);


        }
    }
}