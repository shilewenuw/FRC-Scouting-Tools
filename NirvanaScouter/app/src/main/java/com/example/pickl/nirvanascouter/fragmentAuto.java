package com.example.pickl.nirvanascouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by pickl on 1/16/2018.
 */

public class fragmentAuto extends android.support.v4.app.Fragment {
    public static TextView autohatch, autocargo = null;
    public static Button autohatcht3,autohatcht2, autohatcht1,
            autocargot3,autocargot2, autocargot1 = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        autohatch = getView().findViewById(R.id.autohatch);
        autocargo = getView().findViewById(R.id.autocargo);
        autohatcht3 = getView().findViewById(R.id.autohatcht3);
        autohatcht2 = getView().findViewById(R.id.autohatcht2);
        autohatcht1 = getView().findViewById(R.id.autohatcht1);
        autocargot3 = getView().findViewById(R.id.autocargot3);
        autocargot2 = getView().findViewById(R.id.autocargot2);
        autocargot1 = getView().findViewById(R.id.autocargot1);


    }


}
