package com.example.pickl.nirvanascouter;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by pickl on 1/16/2018.
 */

public class fragmentPre extends android.support.v4.app.Fragment {
    public EditText matchNumber = null;
    String[] teams = {"0000","180","233","253","538","604","624","842","846","932",
            "973","1323","1539","1540","1706","1806","1817","1902","1983",
            "2073","2471","2659","2682","2910","3005","3045","3245","3354",
            "3374","3598","3674","4068","4153","4191","4201","4265","4415",
            "4504","4635","5015","5026","5052","5109","5312","5437","5499",
            "5854","6059","6068","6106","6200","6348","6388","6429","6652",
            "6833","6841","7042","7161","7500","7514","7521","7583","7653",
            "7667","7761","7843","7887"};

    TextView pointsView, moneyView, eggsView, milkView;
    enum GrowthState {GROW,GROWING,GROWN}

    int points = 0;
    int money = 0;
    int cropCost = -5;
    int cropYield = 10;
    int growTimeInMillis = 10000;
    int eggs = 0;
    int milk = 0;
    int upgradeCost = -100;
    int eggCost = -20;
    int milkCost = -20;
    final int upgradeCostIncrement = -20;
    final int eggMilkCostIncrement = -7;
    int buyEggMilkCost = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_pre, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        matchNumber = getView().findViewById(R.id.mn);

        try {
            Bundle bundle = getActivity().getIntent().getExtras();
            int matchnum = bundle.getInt("matchnumberplusone");
            matchNumber.setText(Integer.toString(matchnum + 1));
            points = bundle.getInt("points");


        }catch (Exception e){matchNumber.setText("1");};

        AutoCompleteTextView teamNumber = getView().findViewById(R.id.tm);
        teamNumber.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, teams));
        teamNumber.requestFocus();

        pointsView = getView().findViewById(R.id.points);
        moneyView = getView().findViewById(R.id.money);
        eggsView = getView().findViewById(R.id.egg_score);
        milkView = getView().findViewById(R.id.milk_score);
        int[] ids = {R.id.crop1,R.id.crop2,R.id.crop3,R.id.crop4,R.id.crop5,R.id.crop6,
                R.id.crop7,R.id.crop8,R.id.crop9};
        for(int id:ids)
            new CropField((ImageButton)getView().findViewById(id));
        ((ImageButton)getView().findViewById(R.id.milk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasMoney(buyEggMilkCost)){
                    milkView.setText(Integer.toString(++milk));
                    money+=buyEggMilkCost;
                    moneyView.setText(parse(money));
                }
            }
        });
        ((ImageButton)getView().findViewById(R.id.egg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasMoney(buyEggMilkCost)){
                    eggsView.setText(Integer.toString(++eggs));
                    money+=buyEggMilkCost;
                    moneyView.setText(parse(money));
                }

            }
        });
        ((ImageButton)getView().findViewById(R.id.cookie)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasResources(upgradeCost, eggCost, milkCost)){
                    transaction(upgradeCost, eggCost, milkCost);
                    upgradeCost+=upgradeCostIncrement;
                    eggCost+=eggMilkCostIncrement;
                    milkCost+=eggMilkCostIncrement;
                    setViewText(R.id.money_cost, Math.abs(upgradeCost));
                    setViewText(R.id.egg_cost, Math.abs(eggCost));
                    setViewText(R.id.milk_cost, Math.abs(milkCost));
                    cropYield++;
                    setNewGrowTime();
                }
            }
        });
        money = (int)Math.pow(points,.7)/5;
        if(money<25)
            money = 25;
        moneyView.setText(Integer.toString(money));
    }
    void setViewText(int id, int text){
        ((TextView)getView().findViewById(id)).setText(Integer.toString(text));
    }
    void setNewGrowTime(){
        growTimeInMillis = 20000 +  80*(growTimeInMillis-20000)/100;
    }
    void transaction(int amount, int eggs0, int milk0){
        money += amount;
        moneyView.setText(parse(money));
        eggs += eggs0;
        eggsView.setText(parse(eggs));
        milk += milk0;
        milkView.setText(parse(milk));
        points+= Math.abs(amount + eggs0*3 + milk0*3);
        pointsView.setText(parse(points));
    }
    void transaction(int amount){
        money += amount;
        moneyView.setText(parse(money));
        points+= Math.abs(amount)/5;
        pointsView.setText(parse(points));
    }
    boolean hasMoney(int amount){
        return hasResources(amount,0,0);
    }
    boolean hasResources(int amount, int eggs0, int milk0){
        if(money+amount>=0 && eggs+eggs0>=0 && milk+milk0>=0)
            return true;
        else
            return false;
    }
    String parse(int i){
        return Integer.toString(i);
    }
    class CropField{

        GrowthState growthState = GrowthState.GROW;

        CropField(final ImageButton button){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(growthState == GrowthState.GROW && hasMoney(cropCost)){
                        transaction(cropCost);
                        growthState = GrowthState.GROWING;
                        button.setBackgroundResource(R.drawable.crop_growing);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                growthState = GrowthState.GROWN;
                                button.setBackgroundResource(R.drawable.crop_grown);
                                //Do something after specified time
                            }
                        }, growTimeInMillis);

                    } else if(growthState == GrowthState.GROWN){
                        transaction(cropYield);
                        growthState = GrowthState.GROW;
                        button.setBackgroundResource(R.drawable.crop_start);
                    }
                }
            });
        }
    }
}

