package com.example.pickl.samsarascouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pickl on 1/16/2018.
 */

public class fragmentAuto extends android.support.v4.app.Fragment {
    public static TextView aswitch, ascale, avault = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        aswitch = getView().findViewById(R.id.aswitch);
        ascale = getView().findViewById(R.id.ascale);
        avault = getView().findViewById(R.id.avault);
    }
}
