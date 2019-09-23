package com.example.pickl.nirvanascouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by pickl on 1/16/2018.
 */

public class fragmentPost extends android.support.v4.app.Fragment {
     public static Button selfclimb, rob1climb, rob2climb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_post, container, false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        selfclimb = getView().findViewById(R.id.climbself);
        rob1climb = getView().findViewById(R.id.climbrob1);
        rob2climb = getView().findViewById(R.id.climbrob2);
        Spinner spinner = getView().findViewById(R.id.comment_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.comments_array, R.layout.spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //String[] commentsArray = getResources().getStringArray(R.array.comments_array);
        //spinner.setAdapter(new ArrayAdapter<String>(getContext(), ,commentsArray));


    }



}
