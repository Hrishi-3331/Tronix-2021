package com.hrishi_3331.hrishi_studio.tronix2021;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class about_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_activity);

        Toolbar toolbar = (Toolbar)findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About");

    }
}
