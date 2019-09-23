package com.example.pickl.nirvanascouter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;

import static com.example.pickl.nirvanascouter.fragmentAuto.autocargo;
import static com.example.pickl.nirvanascouter.fragmentAuto.autocargot1;
import static com.example.pickl.nirvanascouter.fragmentAuto.autocargot2;
import static com.example.pickl.nirvanascouter.fragmentAuto.autocargot3;
import static com.example.pickl.nirvanascouter.fragmentAuto.autohatch;
import static com.example.pickl.nirvanascouter.fragmentAuto.autohatcht1;
import static com.example.pickl.nirvanascouter.fragmentAuto.autohatcht2;
import static com.example.pickl.nirvanascouter.fragmentAuto.autohatcht3;
import static com.example.pickl.nirvanascouter.fragmentPost.rob1climb;
import static com.example.pickl.nirvanascouter.fragmentPost.rob2climb;
import static com.example.pickl.nirvanascouter.fragmentPost.selfclimb;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telecargo;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telecargot1;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telecargot2;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telecargot3;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telehatch;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telehatcht1;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telehatcht2;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telehatcht3;

public class MainActivity extends AppCompatActivity {
    //public static InputMethodManager imm = null;
    private PagerAdapter pa;
    private ViewPager mViewPager;
    private TabLayout tabLayout;


    String present = "";
    int[] climbIncrements = {0, 0, 0};
    String[] habClimbTiers = {"L0", "L1", "L2", "L3"};
    int[] habClimbValues = {0, 3, 6, 12};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Pre-Match"));
        tabLayout.addTab(tabLayout.newTab().setText("Auto"));
        tabLayout.addTab(tabLayout.newTab().setText("Teleop"));
        tabLayout.addTab(tabLayout.newTab().setText("Post-Match"));
        pa = new PagerAdapter(getSupportFragmentManager(), 5);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(pa);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });

    }


    public void noShowClick(View view) {
        mViewPager.setCurrentItem(4);
    }

    public void goBack(View view) {
        mViewPager.setCurrentItem(0);
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new fragmentPre();
                case 1:
                    return new fragmentAuto();
                case 2:
                    return new fragmentTeleop();
                case 3:
                    return new fragmentPost();
                case 4:
                    return new NoShowActivity();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    public void onCrementClick(View v) {
        switch (v.getId()) {
            case R.id.autohatchplus:
                crementText(autohatch, 1);
                break;
            case R.id.autohatchminus:
                crementText(autohatch, -1);
                break;
            case R.id.autocargoplus:
                crementText(autocargo, 1);
                break;
            case R.id.autocargominus:
                crementText(autocargo, -1);
                break;

            case R.id.autohatcht1minus:
                decrementButton(autohatcht1);
                break;
            case R.id.autohatcht2minus:
                decrementButton(autohatcht2);
                break;
            case R.id.autohatcht3minus:
                decrementButton(autohatcht3);
                break;

            case R.id.autocargot1minus:
                decrementButton(autocargot1);
                break;
            case R.id.autocargot2minus:
                decrementButton(autocargot2);
                break;
            case R.id.autocargot3minus:
                decrementButton(autocargot3);
                break;

            case R.id.telehatchplus:
                crementText(telehatch, 1);
                break;
            case R.id.telehatchminus:
                crementText(telehatch, -1);
                break;
            case R.id.telecargoplus:
                crementText(telecargo, 1);
                break;
            case R.id.telecargominus:
                crementText(telecargo, -1);
                break;

            case R.id.telehatcht1minus:
                decrementButton(telehatcht1);
                break;
            case R.id.telehatcht2minus:
                decrementButton(telehatcht2);
                break;
            case R.id.telehatcht3minus:
                decrementButton(telehatcht3);
                break;

            case R.id.telecargot1minus:
                decrementButton(telecargot1);
                break;
            case R.id.telecargot2minus:
                decrementButton(telecargot2);
                break;
            case R.id.telecargot3minus:
                decrementButton(telecargot3);
                break;

            case R.id.climbselfminus:
                habDecrement(selfclimb, 0);
                break;
            case R.id.climbself:
                habCrement(selfclimb, 0);
                break;
            case R.id.climbrob1minus:
                habDecrement(rob1climb, 1);
                break;
            case R.id.climbrob1:
                habCrement(rob1climb, 1);
                break;
            case R.id.climbrob2minus:
                habDecrement(rob2climb, 2);
                break;
            case R.id.climbrob2:
                habCrement(rob2climb, 2);
                break;

            default:
                Toast.makeText(this, "weird", Toast.LENGTH_LONG);
        }
    }
    public void crementText(TextView counter, int increment) {
        int num = Integer.parseInt((String) counter.getText());
        if (!(num == 0 && increment == -1) && !(num==12 && increment == 1)){
            num += increment;
            counter.setText(Integer.toString(num));
        }
    }

    public void decrementButton(Button button){
        int num = Integer.parseInt((String)button.getText());
        if (num!=0) {
            num--;
            button.setText(Integer.toString(num));
        }
    }
    public void onCrementClick2(View v) {
        Button button = findViewById(v.getId());
        int num = Integer.parseInt((String)button.getText());
        if(num!=4){
            num++;
            button.setText(Integer.toString(num));
        }
    }
    public void habCrement(Button button, int index){
        if(climbIncrements[index]!=3) {
            climbIncrements[index]++;
            button.setText(habClimbTiers[climbIncrements[index]]);
        }
    }
    public void habDecrement(Button button, int index){
        if (climbIncrements[index]!=0) {
            climbIncrements[index]--;
            button.setText(habClimbTiers[climbIncrements[index]]);
        }
    }

    public void appendComment(View view){
        EditText commentText = findViewById(R.id.comments);
        commentText.append(((Spinner) findViewById(R.id.comment_spinner)).
                getSelectedItem().toString() + ".");
    }


    public void saveData(View view) {
        String[] teams = {"0000","180","233","253","538","604","624","842","846","932",
                "973","1323","1539","1540","1706","1806","1817","1902","1983",
                "2073","2471","2659","2682","2910","3005","3045","3245","3354",
                "3374","3598","3674","4068","4153","4191","4201","4265","4415",
                "4504","4635","5015","5026","5052","5109","5312","5437","5499",
                "5854","6059","6068","6106","6200","6348","6388","6429","6652",
                "6833","6841","7042","7161","7500","7514","7521","7583","7653",
                "7667","7761","7843","7887"};


        int[] ids = { R.id.autohatch, R.id.autocargo,
                R.id.autohatcht1, R.id.autohatcht2, R.id.autohatcht3,
                R.id.autocargot1, R.id.autocargot2, R.id.autocargot3,
                R.id.telehatch, R.id.telecargo,
                R.id.telehatcht1, R.id.telehatcht2, R.id.telehatcht3,
                R.id.telecargot1, R.id.telecargot2, R.id.telecargot3};
        String teamnumber = getStringEditText(R.id.tm);
        String matchnumber = getStringEditText(R.id.mn);
        long millis = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        int days = c.get(Calendar.DAY_OF_YEAR);
        String submissionID = "team" + teamnumber + "-" + matchnumber + "-" + days;
        String comments = getStringEditText(R.id.comments);


        if (view.getId() == R.id.button_submit_noshow) {
            present = "_no show_";
        }


        try {
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/Einstein/", submissionID + ".txt");

            f.getParentFile().mkdirs();
            if (!Arrays.asList(teams).contains(teamnumber) || matchnumber.equals("")) {
                Toast.makeText(MainActivity.this, "Fill out the variables correctly. Ok?", Toast.LENGTH_LONG).show();
                return;
            }
            BufferedWriter fw = new BufferedWriter(new FileWriter(f, false));

            fw.append(submissionID + ",");
            fw.append(getBoolean(R.id.hablevel2 ) + ",");
            for (int id : ids)
                fw.append(diverge(id) + ",");
            fw.append(climbPoints()  + ",");
            fw.append(getNumStars(R.id.driverrating) + ",");
            fw.append(commasRemoved(comments)+present);//need to remove commas so our CSV doesn't get messed up
            fw.close();


            scanFile(getApplicationContext(), f, "text/plain");

            Intent intent = getIntent();
            intent.putExtra("matchnumberplusone", Integer.parseInt(matchnumber));
            intent.putExtra("points", Integer.parseInt(((TextView)findViewById(R.id.points)).
                    getText().toString()) );

            Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_LONG).show();
            finish();
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "something fail", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public String diverge(int id) { //gets text from various types of widgets
        String str;
        if (findViewById(id) instanceof EditText)
            str = getStringEditText(id);
        else if (findViewById(id) instanceof AppCompatTextView)//for some reason TextView doesn't work
            str = getStringTextView(id);
        else if (findViewById(id) instanceof AutoCompleteTextView)
            str = getStringAutoComplete(id);
        else if (findViewById(id) instanceof Button)
            str = getStringButton(id);
        else
            str = getBoolean(id);
        return str;
    }

    public String getStringEditText(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }

    public String getStringTextView(int id) {
        return ((TextView) findViewById(id)).getText().toString();
    }
    public String getStringAutoComplete(int id){
        return ((AutoCompleteTextView) findViewById(id)).getText().toString();
    }

    public String getStringButton(int id){
        return ((Button) findViewById(id)).getText().toString();
    }

    public String getBoolean(int id) {
        if(((CheckBox) findViewById(id)).isChecked())
            return "1";
        else
            return "0";
    }
    public String getNumStars(int id){
        return Integer.toString((int)((RatingBar) findViewById(R.id.driverrating)).getRating());
    }

    static String commasRemoved(String str) {
        return str.replace(",", ".");
    }
    int climbPoints(){
        int points = 0;
        for(int i:climbIncrements){
            points+=habClimbValues[i];
        }
        return points;
    }


    public void scanFile(Context ctxt, File f, String mimeType) { //makes file visible to computer
        MediaScannerConnection
                .scanFile(ctxt, new String[]{f.getAbsolutePath()},
                        new String[]{mimeType}, null);
    }
}

