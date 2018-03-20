package com.example.pickl.samsarascouter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import net.qiujuer.genius.res.Resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.example.pickl.samsarascouter.fragmentAuto.ascale;
import static com.example.pickl.samsarascouter.fragmentAuto.aswitch;
import static com.example.pickl.samsarascouter.fragmentAuto.avault;
import static com.example.pickl.samsarascouter.fragmentTeleop.tscale;
import static com.example.pickl.samsarascouter.fragmentTeleop.tswitchenemy;
import static com.example.pickl.samsarascouter.fragmentTeleop.tswitchself;
import static com.example.pickl.samsarascouter.fragmentTeleop.tvault;


public class MainActivity extends AppCompatActivity {
    String gamblecolor = "";

    private boolean timerOn = false;
    private PagerAdapter pa;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ArrayList<Long> switchSelfCycle, switchEnemyCycle, scaleCycle, vaultCycle;

    String present = "";
    String flag = "";
    String breakdown = "";
    int climb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchSelfCycle = new ArrayList<>();
        switchEnemyCycle = new ArrayList<>();
        scaleCycle = new ArrayList<>();
        vaultCycle = new ArrayList<>();
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
            case R.id.aswitchplus:
                crementText(aswitch, 1);
                break;
            case R.id.aswitchminus:
                crementText(aswitch, -1);
                break;
            case R.id.ascaleplus:
                crementText(ascale, 1);
                break;
            case R.id.ascaleminus:
                crementText(ascale, -1);
                break;
            case R.id.avaultplus:
                crementText(avault, 1);
                break;
            case R.id.avaultminus:
                crementText(avault, -1);
                break;

            case R.id.tswitchselfminus:
                crementText(tswitchself, -1);
                break;

            case R.id.tswitchenemyminus:
                crementText(tswitchenemy, -1);
                break;

            case R.id.tscaleminus:
                crementText(tscale, -1);
                break;

            case R.id.tvaultminus:
                crementText(tvault, -1);
                break;
            default:
                Toast.makeText(this, "weird", Toast.LENGTH_LONG);
        }
    }

    public void onChronoClick(View v) {
        Chronometer c = findViewById(R.id.chronometer);
        if (!timerOn) {
            timerOn = true;
            c.setBase(SystemClock.elapsedRealtime());
            c.start();
            ((Button)v).setBackgroundColor(Color.RED);
        } else{
            timerOn = false;
            c.stop();
            c.setBase(SystemClock.elapsedRealtime());
            ((Button)v).setBackgroundColor(Color.parseColor("#00C853"));
        }
    }

    public void onCrementClick2(View v) {
        teleOpProcess(v.getId());
    }

    public void teleOpProcess(int id) {

        switch (id) {
            case R.id.tswitchselfplus:
                crementText(tswitchself, 1);
                break;
            case R.id.tswitchenemyplus:
                crementText(tswitchenemy, 1);
                break;
            case R.id.tscaleplus:
                crementText(tscale, 1);
                break;
            case R.id.tvaultplus:
                crementText(tvault, 1);
                break;
        }
        ((Button)findViewById(R.id.chronobutton)).setBackgroundColor(Color.parseColor("#00C853"));
        addCycleTime(id);

    }

    public void addCycleTime(int id) {
        Chronometer temp = findViewById(R.id.chronometer);
        temp.stop();
        timerOn = false;
        switch (id) {
            case R.id.tswitchselfplus:
                switchSelfCycle.add(SystemClock.elapsedRealtime() - temp.getBase());
                break;
            case R.id.tswitchenemyplus:
                switchEnemyCycle.add(SystemClock.elapsedRealtime() - temp.getBase());
                break;
            case R.id.tscaleplus:
                scaleCycle.add(SystemClock.elapsedRealtime() - temp.getBase());
                break;
            case R.id.tvaultplus:
                scaleCycle.add(SystemClock.elapsedRealtime() - temp.getBase());
                break;
        }
        temp.setBase(SystemClock.elapsedRealtime());
    }

    public void radioClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        if (checked) {
            switch (v.getId()) {
                case R.id.nothing:
                    flag = "";
                    break;
                case R.id.yellow:
                    flag = "flag(yellow)_";
                    break;
                case R.id.red:
                    flag = "flag(red)_";
                    break;
            }
        }
    }
    public void radioClick2(View v){
        if(((RadioButton)v).isChecked()){
            switch (v.getId()){
                case R.id.zero:
                    climb = 0;
                    break;
                case R.id.one:
                    climb = 1;
                    break;
                case R.id.two:
                    climb = 2;
                    break;
                case R.id.three:
                    climb = 3;
                    break;
            }
        }
    }
    public void onGambleClick(View v){
        if(((RadioButton)v).isChecked()){
            switch (v.getId()){
                case R.id.red:
                    gamblecolor = "red";
                case R.id.blue:
                    gamblecolor = "blue";
            }
        }
    }

    public void crementText(TextView counter, int increment) {
        int num = Integer.parseInt((String) counter.getText());
        if (!(num == 0 && increment == -1)) {
            num += increment;
            counter.setText(Integer.toString(num));
        }
    }

    public void saveData(View view) {
        String[] teams = {"847","955","957","997","1359","1432","1510","1571","2411","2471","2521","2550","2635","2733","2811","2898","2915","3024",
                "3131","3636","3674","3812","4043","4057","4110","4662","4692","5198","5450","5970","5975","6343","6465","7032","7034","7204"};
        int[] ids = {R.id.linecross, R.id.aswitch, R.id.ascale, R.id.avault, R.id.tswitchself, R.id.tswitchenemy, R.id.tscale, R.id.tvault};
        String teamnumber = getStringEditText(R.id.tm);
        String matchnumber = getStringEditText(R.id.mn);
        long millis = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        int days = c.get(Calendar.DAY_OF_YEAR);
        String submissionID = "team" + teamnumber + "-" + matchnumber + "-" + days;

        String strategy = getStringEditText(R.id.strategy);
        String comments = getStringEditText(R.id.comments);

        String gamblersName = getStringEditText(R.id.pokername);
        if (view.getId() == R.id.noshowbutton) {
            present = "no show_";
        }
        if ( ((CheckBox)findViewById(R.id.breakdown)).isChecked() ){
            breakdown = "BD(" + ((EditText)findViewById(R.id.bdtext)).getText().toString() + ")_";
        }

        try {
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/Einstein/", submissionID + ".txt");
            File f2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/poker/",  gamblersName + matchnumber + ".txt");
            f.getParentFile().mkdirs();
            if (!Arrays.asList(teams).contains(teamnumber) || matchnumber.equals("")) {
                Toast.makeText(MainActivity.this, "Fill out the variables correctly. Ok?", Toast.LENGTH_LONG).show();
                return;
            }
            BufferedWriter fw = new BufferedWriter(new FileWriter(f, false));
            BufferedWriter fw2 = new BufferedWriter(new FileWriter(f2, false));
            fw.append(submissionID + ",");
            for (int id : ids)
                fw.append(diverge(id) + ",");
            fw.append(climb + ",");
            fw.append(calculateAverageCycleTime(switchSelfCycle) + ",");
            fw.append(calculateAverageCycleTime(switchEnemyCycle) + ",");
            fw.append(calculateAverageCycleTime(scaleCycle) + ",");
            fw.append(calculateAverageCycleTime(vaultCycle) + ",");
            if (!present.equals("") || !flag.equals("") || !breakdown.equals(""))
                fw.append(present + flag + breakdown + "match(" + matchnumber + ")");
            fw.append(",");
            fw.append(commasRemoved(strategy) + ",");
            fw.append(commasRemoved(comments));
            fw.close();

            fw2.append(gamblersName + "," + gamblecolor + "," + getStringTextView(R.id.pokercount));
            fw2.close();
            scanFile(getApplicationContext(), f, "text/plain");
            scanFile(getApplicationContext(), f2, "text/plain");
            Intent intent = getIntent();
            intent.putExtra("matchnumberplusone", Integer.parseInt(matchnumber));
            intent.putExtra("cookies", Integer.parseInt(((TextView)findViewById(R.id.cookiescore)).getText().toString())+
                    Integer.parseInt(((TextView)findViewById(R.id.cachedcookiescore)).getText().toString()));
            intent.putExtra("pokername", gamblersName);


            Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_LONG).show();
            finish();
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "something fail", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public String diverge(int id) {
        String str;
        if (findViewById(id) instanceof EditText)
            str = getStringEditText(id);
        else if (findViewById(id) instanceof AppCompatTextView)
            str = getStringTextView(id);
        else str = getBoolean(id);
        return str;
    }

    public String getStringEditText(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }

    public String getStringTextView(int id) {
        return ((TextView) findViewById(id)).getText().toString();
    }

    public String getBoolean(int id) {
        return Boolean.toString(((CheckBox) findViewById(id)).isChecked());
    }

    static String commasRemoved(String str) {
        return str.replace(",", ".");
    }

    public void scanFile(Context ctxt, File f, String mimeType) {
        MediaScannerConnection
                .scanFile(ctxt, new String[]{f.getAbsolutePath()},
                        new String[]{mimeType}, null);
    }

    public String calculateAverageCycleTime(ArrayList<Long> a) {
        long num = 0;
        if (a.isEmpty()) {
            return "0_0";
        } else {
            for (Long item : a) {
                num += item;
            }
            return Double.toString(num/1000.0) + "_" +  Integer.toString(a.size());// /1000 converts to sec
        }
    }
}

