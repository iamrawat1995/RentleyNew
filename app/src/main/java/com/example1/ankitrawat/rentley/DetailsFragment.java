package com.example1.ankitrawat.rentley;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUserID;

    ArrayList <String> list;
    ArrayAdapter <String> adapter;
    Tenant tenant;
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.activity_fragment_details,container,false);

        listView = (ListView) rootView.findViewById(R.id.tenant_listView);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        tenant = new Tenant();
        firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference().child("Tenants").child(currentUserID);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(),R.layout.user_info,R.id.userInfo,list);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tenants");



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( final DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChildren())
                {
                    for(final DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        String Name = String.valueOf(postSnapshot.child("TenantName").getValue());
                        String Mobile = String.valueOf(postSnapshot.child("TenantMobile").getValue());
                        String Adress = String.valueOf(postSnapshot.child("TenantAdress").getValue());
                        list.add(new Tenant().getTenantName().toString());
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        /*
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot)
           {
               if(dataSnapshot.hasChildren())
               {
                   for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                   {
                       tenant = dataSnapshot1.getValue(Tenant.class);
                       String tenantString = String.valueOf(tenant);
                       adapter.add(tenantString);
                   }
               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError)
           {

           }
       });


*/


        return rootView;
    }
}
