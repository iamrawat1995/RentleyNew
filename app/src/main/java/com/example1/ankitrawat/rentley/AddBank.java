package com.example1.ankitrawat.rentley;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ANKIT RAWAT on 22-Mar-18.
 */

public class AddBank  extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_add_bank,container,false);

        //ImageView v=(ImageView)view.findViewById(R.id.i)




        return view;
    }
}