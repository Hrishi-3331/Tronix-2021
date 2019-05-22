package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class overall_attendance extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText maths_total, sns_total, mni_total, acd_total, emf_total;
    EditText maths_att, sns_att, mni_att, acd_att, emf_att;
    TextView maths_perc, sns_perc, mni_perc, acd_perc, emf_perc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_attendance);

        Toolbar toolbar = (Toolbar)findViewById(R.id.atool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("My Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        maths_att = (EditText)findViewById(R.id.maths_att_class);
        maths_total  = (EditText)findViewById(R.id.maths_total_class);
        maths_perc = (TextView) findViewById(R.id.maths_pr);

        sns_att = (EditText)findViewById(R.id.sns_att_class);
        sns_total  = (EditText)findViewById(R.id.sns_total_class);
        sns_perc = (TextView) findViewById(R.id.sns_pr);

        mni_att = (EditText)findViewById(R.id.mni_att_class);
        mni_total  = (EditText)findViewById(R.id.mni_total_class);
        mni_perc = (TextView) findViewById(R.id.mni_pr);

        acd_att = (EditText)findViewById(R.id.acd_att_class);
        acd_total  = (EditText)findViewById(R.id.acd_total_class);
        acd_perc = (TextView) findViewById(R.id.acd_pr);

        emf_att = (EditText)findViewById(R.id.emf_att_class);
        emf_total  = (EditText)findViewById(R.id.emf_total_class);
        emf_perc = (TextView) findViewById(R.id.emf_pr);


        sharedPreferences = getSharedPreferences("User_Attendance", Context.MODE_PRIVATE);

        int maths_attClass = sharedPreferences.getInt("MAL" + "att", 0);
        int maths_totClass = sharedPreferences.getInt("MAL" + "tot", 0);
        double maths_perct = 0;
        if (maths_totClass > 0){
            maths_perct = Math.round((double)maths_attClass / (double)maths_totClass * 100);
        }

        int sns_attClass = sharedPreferences.getInt("SNS" + "att", 0);
        int sns_totClass = sharedPreferences.getInt("SNS" + "tot", 0);
        double sns_perct = 0;
        if (sns_totClass > 0){
            sns_perct = Math.round((double)sns_attClass / (double)sns_totClass * 100);
        }

        int mni_attClass = sharedPreferences.getInt("MCP" + "att", 0);
        int mni_totClass = sharedPreferences.getInt("MCP" + "tot", 0);
        double mni_perct = 0;
        if (mni_totClass > 0){
            mni_perct = Math.round((double)mni_attClass / (double)mni_totClass * 100);
        }

        int acd_attClass = sharedPreferences.getInt("ACD" + "att", 0);
        int acd_totClass = sharedPreferences.getInt("ACD" + "tot", 0);
        double acd_perct = 0;
        if (acd_totClass > 0){
            acd_perct = Math.round((double)acd_attClass / (double)acd_totClass * 100);
        }

        int emf_attClass = sharedPreferences.getInt("EMF" + "att", 0);
        int emf_totClass = sharedPreferences.getInt("EMF" + "tot", 0);
        double emf_perct = 0;
        if (emf_totClass > 0){
            emf_perct = Math.round((double)emf_attClass / (double)emf_totClass * 100);
        }

        maths_att.setText(String.valueOf(maths_attClass));
        maths_total.setText(String.valueOf(maths_totClass));
        maths_perc.setText(String.valueOf(maths_perct) + "%");

        sns_att.setText(String.valueOf(sns_attClass));
        sns_total.setText(String.valueOf(sns_totClass));
        sns_perc.setText(String.valueOf(sns_perct) + "%");

        mni_att.setText(String.valueOf(mni_attClass));
        mni_total.setText(String.valueOf(mni_totClass));
        mni_perc.setText(String.valueOf(mni_perct) + "%");

        acd_att.setText(String.valueOf(acd_attClass));
        acd_total.setText(String.valueOf(acd_totClass));
        acd_perc.setText(String.valueOf(acd_perct) + "%");

        emf_att.setText(String.valueOf(emf_attClass));
        emf_total.setText(String.valueOf(emf_totClass));
        emf_perc.setText(String.valueOf(emf_perct) + "%");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void updateAtt(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("MAL" + "att", Integer.valueOf(maths_att.getText().toString()));
        editor.putInt("MAL" + "tot", Integer.valueOf(maths_total.getText().toString()));

        editor.putInt("SNS" + "att", Integer.valueOf(sns_att.getText().toString()));
        editor.putInt("SNS" + "tot", Integer.valueOf(sns_total.getText().toString()));

        editor.putInt("MCP" + "att", Integer.valueOf(mni_att.getText().toString()));
        editor.putInt("MCP" + "tot", Integer.valueOf(mni_total.getText().toString()));

        editor.putInt("ACD" + "att", Integer.valueOf(acd_att.getText().toString()));
        editor.putInt("ACD" + "tot", Integer.valueOf(acd_total.getText().toString()));

        editor.putInt("EMF" + "att", Integer.valueOf(emf_att.getText().toString()));
        editor.putInt("EMF" + "tot", Integer.valueOf(emf_total.getText().toString()));

        editor.commit();
        finish();
    }
}
