package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    LinearLayout anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        anim = (LinearLayout)findViewById(R.id.splsh_anim);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        anim.animate().scaleX(300).setDuration(1000).start();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (mUser != null) {
                    Intent intent = new Intent(splash.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                }

                else {
                    Intent intent = new Intent(splash.this, authentication.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

    }
}
