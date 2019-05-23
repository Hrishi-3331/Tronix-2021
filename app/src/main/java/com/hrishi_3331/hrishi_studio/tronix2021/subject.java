package com.hrishi_3331.hrishi_studio.tronix2021;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class subject extends AppCompatActivity {

    String code;
    RecyclerView m_assignments, m_notes, m_qpapers, m_prac;
    DatabaseReference mref;
    DatabaseReference ref1, ref2, ref3, ref4;
    private LinearLayout nodata;
    private TabLayout sub_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Toolbar toolbar = (Toolbar)findViewById(R.id.subtoolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        if(ContextCompat.checkSelfPermission(subject.this,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(subject.this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0 );

            if (ContextCompat.checkSelfPermission(subject.this,  Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                finish();
            }
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(intent.getStringExtra("sub"));
        code = intent.getStringExtra("code");

        m_assignments = (RecyclerView)findViewById(R.id.m_assignments);
        m_notes = (RecyclerView)findViewById(R.id.m_notes);
        m_qpapers = (RecyclerView)findViewById(R.id.m_qpapers);
        m_prac = (RecyclerView)findViewById(R.id.m_prac);

        sub_tab = (TabLayout)findViewById(R.id.sub_tabs);

        m_assignments.setLayoutManager(new LinearLayoutManager(subject.this));
        m_qpapers.setLayoutManager(new LinearLayoutManager(subject.this));
        m_notes.setLayoutManager(new LinearLayoutManager(subject.this));
        m_prac.setLayoutManager(new LinearLayoutManager(subject.this));

        mref = FirebaseDatabase.getInstance().getReference().child("study").child(code);
        ref1 = mref.child("assignments");
        ref2 = mref.child("notes");
        ref3 = mref.child("qpapers");
        ref4 = mref.child("pracs");

        nodata = (LinearLayout)findViewById(R.id.no_data);
        nodata.setVisibility(View.GONE);

        m_assignments.setVisibility(View.VISIBLE);

        sub_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        m_assignments.setVisibility(View.VISIBLE);
                        m_qpapers.setVisibility(View.GONE);
                        m_notes.setVisibility(View.GONE);
                        m_prac.setVisibility(View.GONE);
                        break;

                    case 1:
                        m_assignments.setVisibility(View.GONE);
                        m_qpapers.setVisibility(View.GONE);
                        m_notes.setVisibility(View.VISIBLE);
                        m_prac.setVisibility(View.GONE);
                        break;

                    case 2:
                        m_assignments.setVisibility(View.GONE);
                        m_qpapers.setVisibility(View.VISIBLE);
                        m_notes.setVisibility(View.GONE);
                        m_prac.setVisibility(View.GONE);
                        break;

                    case 3:
                        m_assignments.setVisibility(View.GONE);
                        m_qpapers.setVisibility(View.GONE);
                        m_notes.setVisibility(View.GONE);
                        m_prac.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<studyStuff, studyViewHolder> ass_adapter = new FirebaseRecyclerAdapter<studyStuff, studyViewHolder>(studyStuff.class, R.layout.studystuff_card, studyViewHolder.class, ref1) {
            @Override
            protected void populateViewHolder(studyViewHolder viewHolder, studyStuff model, int position) {
                viewHolder.setTitleView(model.getTitle(), model.getAuthor(), model.getDescription());
                viewHolder.setDownload_url(model.getUrl());
                if (!model.getTitle().equals("nodatafound")) {
                    viewHolder.setListners(subject.this);
                }
            }
        };

        FirebaseRecyclerAdapter<studyStuff, studyViewHolder> notes_adapter = new FirebaseRecyclerAdapter<studyStuff, studyViewHolder>(studyStuff.class, R.layout.studystuff_card, studyViewHolder.class, ref2) {
            @Override
            protected void populateViewHolder(studyViewHolder viewHolder, studyStuff model, int position) {
                viewHolder.setTitleView(model.getTitle(), model.getAuthor(), model.getDescription());
                viewHolder.setDownload_url(model.getUrl());
                if (!model.getTitle().equals("nodatafound")) {
                    viewHolder.setListners(subject.this);
                }
            }
        };

        FirebaseRecyclerAdapter<studyStuff, studyViewHolder> qpaper_adapter = new FirebaseRecyclerAdapter<studyStuff, studyViewHolder>(studyStuff.class, R.layout.studystuff_card, studyViewHolder.class, ref3) {
            @Override
            protected void populateViewHolder(studyViewHolder viewHolder, studyStuff model, int position) {
                viewHolder.setTitleView(model.getTitle(), model.getAuthor(), model.getDescription());
                viewHolder.setDownload_url(model.getUrl());
                if (!model.getTitle().equals("nodatafound")) {
                    viewHolder.setListners(subject.this);
                }
            }
        };

        FirebaseRecyclerAdapter<studyStuff, studyViewHolder> prac_adapter = new FirebaseRecyclerAdapter<studyStuff, studyViewHolder>(studyStuff.class, R.layout.studystuff_card, studyViewHolder.class, ref4) {
            @Override
            protected void populateViewHolder(studyViewHolder viewHolder, studyStuff model, int position) {
                viewHolder.setTitleView(model.getTitle(), model.getAuthor(), model.getDescription());
                viewHolder.setDownload_url(model.getUrl());
                if (!model.getTitle().equals("nodatafound")) {
                    viewHolder.setListners(subject.this);
                }
            }
        };

        m_assignments.setAdapter(ass_adapter);
        m_prac.setAdapter(prac_adapter);
        m_notes.setAdapter(notes_adapter);
        m_qpapers.setAdapter(qpaper_adapter);

    }
}
