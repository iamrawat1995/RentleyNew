package com.example1.ankitrawat.rentley;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ANKIT RAWAT on 18-Mar-18.
 */

public class HomeFragment extends Fragment {
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_home, container, false);

        CardView cv4=(CardView)rootView.findViewById(R.id.home_add_tenant);
        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendUserAddTenant();
            }
        });


        return rootView;
    }

    private void SendUserAddTenant()
    {
        Intent addTenantIntent = new Intent(HomeFragment.this.getActivity(), AddTenantActivity.class);
        startActivity(addTenantIntent);
    }
  /*  @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        Toolbar tb=(Toolbar)view.getRootView().findViewById(R.id.toolbar);
        tb.setTitle("Rentley");
    }*/

}
