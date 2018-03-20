package com.example.pickl.samsarascouter;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pickl on 1/16/2018.
 */

public class fragmentTeleop extends android.support.v4.app.Fragment {
    public static TextView tswitchself, tswitchenemy, tscale, tvault = null;
    //public static Chronometer switchMeter, scaleMeter, vaultMeter = null;
    //public static ArrayList<String>
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tswitchself = getView().findViewById(R.id.tswitchself);
        tswitchenemy = getView().findViewById(R.id.tswitchenemy);
        tscale = getView().findViewById(R.id.tscale);
        tvault = getView().findViewById(R.id.tvault);

    }
}
