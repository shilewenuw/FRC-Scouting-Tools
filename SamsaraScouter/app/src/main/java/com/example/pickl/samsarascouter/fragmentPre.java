package com.example.pickl.samsarascouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by pickl on 1/16/2018.
 */

public class fragmentPre extends android.support.v4.app.Fragment {
    public EditText matchNumber = null;
    public TextView cookies = null;
    public TextView pokername = null;
    int pokercount = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_pre, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        matchNumber = getView().findViewById(R.id.mn);
        cookies = getActivity().findViewById(R.id.cachedcookiescore);
        pokername = getActivity().findViewById(R.id.pokername);
        try {
            Bundle bundle = getActivity().getIntent().getExtras();
            int matchnum = bundle.getInt("matchnumberplusone");
            matchNumber.setText(Integer.toString(matchnum + 1));
            cookies.setText(Integer.toString(bundle.getInt("cookies")));
            pokername.setText(bundle.getString("pokername"));

        }catch (Exception e){matchNumber.setText("1");};
        final ImageButton cookiebutton = getView().findViewById(R.id.cookiebutton);
        cookiebutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Log.i("click", "clicked");
                    //cookiebutton.setLayoutParams(new LinearLayout.LayoutParams(180,180));
                    cookiebutton.getLayoutParams().height = 180;//set appropriate sizes
                    cookiebutton.getLayoutParams().width= 180;

                    cookiebutton.requestLayout();


                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //cookiebutton.setLayoutParams(new LinearLayout.LayoutParams(200,200));
                    cookiebutton.getLayoutParams().height = 200;//set appropriate sizes
                    cookiebutton.getLayoutParams().width= 200;
                    //cookiebutton.setLayoutParams(new LinearLayout.LayoutParams((new Random()).nextInt(330), 380+(new Random()).nextInt(300)));
                    cookiebutton.setX((new Random()).nextInt(330));
                    cookiebutton.setY(380+(new Random()).nextInt(300));
                    cookiebutton.requestLayout();
                    TextView score = getView().findViewById(R.id.cookiescore);
                    int score2 = Integer.parseInt(score.getText().toString()) + 1;
                    score.setText(Integer.toString(score2));
                }
                return true;
            }
        });
        final ImageButton pokerbutton = getView().findViewById(R.id.pokerbutton);
        pokerbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    //cookiebutton.setLayoutParams(new LinearLayout.LayoutParams(180,180));
                    pokerbutton.getLayoutParams().height = 180;//set appropriate sizes
                    pokerbutton.getLayoutParams().width= 180;
                    pokerbutton.requestLayout();
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //cookiebutton.setLayoutParams(new LinearLayout.LayoutParams(200,200));
                    pokerbutton.getLayoutParams().height = 200;//set appropriate sizes
                    pokerbutton.getLayoutParams().width= 200;
                    pokerbutton.requestLayout();
                    TextView score = getView().findViewById(R.id.pokercount);
                    if (pokercount<10)
                        pokercount++;
                    else
                        pokercount=0;
                    score.setText(Integer.toString(pokercount));
                }
                return true;
            }
        });

    }

}