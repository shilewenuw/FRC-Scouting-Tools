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

public class fragmentTeleop extends android.support.v4.app.Fragment {
    public static TextView telehatch, telecargo = null;
    public static Button telehatcht3,telehatcht2, telehatcht1,
            telecargot3,telecargot2, telecargot1 = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        telehatch = getView().findViewById(R.id.telehatch);
        telecargo = getView().findViewById(R.id.telecargo);
        telehatcht3 = getView().findViewById(R.id.telehatcht3);
        telehatcht2 = getView().findViewById(R.id.telehatcht2);
        telehatcht1 = getView().findViewById(R.id.telehatcht1);
        telecargot3 = getView().findViewById(R.id.telecargot3);
        telecargot2 = getView().findViewById(R.id.telecargot2);
        telecargot1 = getView().findViewById(R.id.telecargot1);



    }
}

