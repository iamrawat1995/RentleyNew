package com.example1.ankitrawat.rentley;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ANKIT RAWAT on 18-Mar-18.
 */

public class ReportsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_reports,container,false);
    }
  /*  @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        Toolbar tb=(Toolbar)view.getRootView().findViewById(R.id.toolbar);
        tb.setTitle("Reports");
    }*/
}
