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

    public double cookieIncrement = 1;
    public double score2 = 0;
    public double cost = 30;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_pre, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        matchNumber = getView().findViewById(R.id.mn);
        cookies = getActivity().findViewById(R.id.cachedcookiescore);

        try {
            Bundle bundle = getActivity().getIntent().getExtras();
            int matchnum = bundle.getInt("matchnumberplusone");
            matchNumber.setText(Integer.toString(matchnum + 1));
            cookies.setText(Double.toString(bundle.getDouble("cookies")));


        }catch (Exception e){matchNumber.setText("1");};

        matchNumber.requestFocus();

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
                    /*cookiebutton.setX((new Random()).nextInt(370));
                    cookiebutton.setY(380+(new Random()).nextInt(300));
                    cookiebutton.requestLayout();*/
                    TextView score = getView().findViewById(R.id.cookiescore);
                    score2 = Double.parseDouble(score.getText().toString()) + cookieIncrement;
                    score.setText(Double.toString(score2));
                }
                return true;
            }
        });
        final ImageButton chocoButton =  getView().findViewById(R.id.choco);
        chocoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(score2>=cost) {
                    score2 -= cost;
                    cookieIncrement += (double) Math.round((cost + 50) * 2 ) / 2000;
                    cost+=10;
                    ((TextView)getView().findViewById(R.id.cookiescore)).setText(Double.toString(score2));
                }
            }
        });

    }

}
