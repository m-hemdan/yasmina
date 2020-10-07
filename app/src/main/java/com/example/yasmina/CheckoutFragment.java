package com.example.yasmina;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutFragment extends Fragment  {
   RecyclerView recyclerViewCheckOut;
   private FirebaseDatabase firebaseDatabase;
   private DatabaseReference mDatabase,mDatabase2;
   private FirebaseUser firebaseUser;
   List<ModelProduct> listModelproduct;
   ModelCard modelCard;
   TextView textView;
   int totalCost=0;
   Controller mControler;
   String userId;
   Button selectDateBtn;

    public CheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        totalCost=0;
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_checkout, container, false);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(firebaseUser != null)
        {
            userId=firebaseUser.getUid();
            mDatabase.child("cart").child(userId);
        }
       int size=((Controller)getContext().getApplicationContext()).getProductsArraylistSize();
        for (int i=0;i<size;i++)
        {
            ModelProduct x=  ((Controller)getContext().getApplicationContext()).getProducts(i);
           totalCost+=0;
        }
        listModelproduct=new ArrayList<>();
        recyclerViewCheckOut=(RecyclerView)rootView.findViewById(R.id.checkout);
        recyclerViewCheckOut.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerViewCheckOut.setLayoutManager(linearLayoutManager);
        final AdapterCheckOut adapterCheckOut=new AdapterCheckOut(listModelproduct,getContext());
        selectDateBtn=(Button)rootView.findViewById(R.id.date_btn);
        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Intent intent=new Intent(getContext(),DateActivity.class);
           startActivity(intent);
            }
        });
        textView=(TextView)rootView.findViewById(R.id.totalCost);

            textView.setText(String.valueOf(totalCost));

        recyclerViewCheckOut.setAdapter(adapterCheckOut);

           firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            firebaseDatabase= FirebaseDatabase.getInstance();
            if(firebaseUser != null)
            {
                mDatabase = FirebaseDatabase.getInstance().getReference("cart").child(firebaseUser.getUid());
            }


           ChildEventListener childEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ModelProduct modelProduct=dataSnapshot.getValue(ModelProduct.class);
                    listModelproduct.add(modelProduct);
                    adapterCheckOut.notifyData(listModelproduct);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            };
            if (mDatabase !=null) {
                mDatabase.addChildEventListener(childEventListener);
            }

        return rootView;
    }




}
