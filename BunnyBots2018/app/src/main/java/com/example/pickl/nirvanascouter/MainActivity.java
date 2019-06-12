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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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
import static com.example.pickl.nirvanascouter.fragmentTeleop.telecargo;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telecargot1;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telecargot2;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telecargot3;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telehatch;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telehatcht1;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telehatcht2;
import static com.example.pickl.nirvanascouter.fragmentTeleop.telehatcht3;





public class MainActivity extends AppCompatActivity {

    private PagerAdapter pa;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    String present = "";
    String flag = "";
    String breakdown = "";
    int climb = 0;

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

            default:
                Toast.makeText(this, "weird", Toast.LENGTH_LONG);
        }
    }
    public void crementText(TextView counter, int increment) {
        int num = Integer.parseInt((String) counter.getText());
        if (!(num == 0 && increment == -1)) {
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
        num++;
        button.setText(Integer.toString(num));

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

    public void saveData(View view) {
        String[] teams = {"120","360","364","568","842","987","1102","1296",
                "1318","1533","1538","1744","1806","2046","2059","2102","2122",
                "2130","2231","2341","2471","2478","2557","2630","2637","2642",
                "2655","2658","2848","3218","3478","3512","3593","3663","3847",
                "4146","4189","4561","4603","4627","4698","4941","4984","5045",
                "5344","5417","5449","5453","5468","5522","5584","5716","5818",
                "5908","5986","6131","6314","6445","6822","6888","6941","6952",
                "7057","70631","7079","7179","7287"};
        int[] ids = {R.id.autohatch, R.id.autocargo};
        String teamnumber = getStringEditText(R.id.tm);
        String matchnumber = getStringEditText(R.id.mn);
        long millis = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        int days = c.get(Calendar.DAY_OF_YEAR);
        String submissionID = "team" + teamnumber + "-" + matchnumber + "-" + days;
        String comments = getStringEditText(R.id.comments);


        if (view.getId() == R.id.noshowbutton) {
            present = "no show_";
        }
        if ( ((CheckBox)findViewById(R.id.breakdown)).isChecked() ){
            breakdown = "BD(" + ((EditText)findViewById(R.id.bdtext)).getText().toString() + ")_";
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
            for (int id : ids)
                fw.append(diverge(id) + ",");
            fw.append(climb + ",");

            if (!present.equals("") || !flag.equals("") || !breakdown.equals(""))
                fw.append(present + flag + breakdown + "match(" + matchnumber + ")");
            fw.append(",");
            fw.append(commasRemoved(comments));
            fw.close();


            scanFile(getApplicationContext(), f, "text/plain");

            Intent intent = getIntent();
            intent.putExtra("matchnumberplusone", Integer.parseInt(matchnumber));
            intent.putExtra("cookies", Double.parseDouble(((TextView)findViewById(R.id.cookiescore)).getText().toString())+
                    Double.parseDouble(((TextView)findViewById(R.id.cachedcookiescore)).getText().toString()));



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

    public String getStringButton(int id){
        return ((Button) findViewById(id)).getText().toString();
    }

    public String getBoolean(int id) {
        if(((CheckBox) findViewById(id)).isChecked())
            return "1";
        else
            return "0";
    }

    static String commasRemoved(String str) {
        return str.replace(",", ".");
    }

    public void scanFile(Context ctxt, File f, String mimeType) {
        MediaScannerConnection
                .scanFile(ctxt, new String[]{f.getAbsolutePath()},
                        new String[]{mimeType}, null);
    }


}

