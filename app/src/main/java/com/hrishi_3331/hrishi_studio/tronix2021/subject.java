package com.hrishi_3331.hrishi_studio.tronix2021;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.List;
import java.util.Objects;

public class subject extends AppCompatActivity implements View.OnClickListener{

    String code;
    RecyclerView m_assignments, m_notes, m_qpapers, m_prac;
    DatabaseReference mref;
    LinearLayout ass_tab, notes_tab, qpapers_tab, prac_tab;
    boolean temp;
    DatabaseReference ref1, ref2, ref3, ref4;
    private LinearLayout nodata;

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

        temp = true;

        Objects.requireNonNull(getSupportActionBar()).setTitle(intent.getStringExtra("sub"));
        code = intent.getStringExtra("code");

        m_assignments = (RecyclerView)findViewById(R.id.m_assignments);
        m_notes = (RecyclerView)findViewById(R.id.m_notes);
        m_qpapers = (RecyclerView)findViewById(R.id.m_qpapers);
        m_prac = (RecyclerView)findViewById(R.id.m_prac);

        ass_tab = (LinearLayout)findViewById(R.id.assignment_tab);
        notes_tab = (LinearLayout)findViewById(R.id.notes_tab);
        qpapers_tab = (LinearLayout)findViewById(R.id.qpapers_tab);
        prac_tab = (LinearLayout)findViewById(R.id.prac_tab);

        ass_tab.setOnClickListener(this);
        notes_tab.setOnClickListener(this);
        qpapers_tab.setOnClickListener(this);
        prac_tab.setOnClickListener(this);

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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.assignment_tab:
                if (temp) {
                    ass_tab.setVisibility(View.VISIBLE);
                    prac_tab.setVisibility(View.GONE);
                    notes_tab.setVisibility(View.GONE);
                    qpapers_tab.setVisibility(View.GONE);
                    m_assignments.setVisibility(View.VISIBLE);
                    m_prac.setVisibility(View.GONE);
                    m_notes.setVisibility(View.GONE);
                    m_qpapers.setVisibility(View.GONE);
                    temp = false;
                } else {
                    ass_tab.setVisibility(View.VISIBLE);
                    prac_tab.setVisibility(View.VISIBLE);
                    notes_tab.setVisibility(View.VISIBLE);
                    qpapers_tab.setVisibility(View.VISIBLE);
                    temp = true;
                    m_assignments.setVisibility(View.GONE);
                    m_prac.setVisibility(View.GONE);
                    m_notes.setVisibility(View.GONE);
                    m_qpapers.setVisibility(View.GONE);
                }
                break;

            case R.id.notes_tab:
                if (temp){
                    ass_tab.setVisibility(View.GONE);
                    prac_tab.setVisibility(View.GONE);
                    notes_tab.setVisibility(View.VISIBLE);
                    qpapers_tab.setVisibility(View.GONE);
                    m_assignments.setVisibility(View.GONE);
                    m_prac.setVisibility(View.GONE);
                    m_notes.setVisibility(View.VISIBLE);
                    m_qpapers.setVisibility(View.GONE);
                    temp = false;
                     }
                     else {
                    ass_tab.setVisibility(View.VISIBLE);
                    prac_tab.setVisibility(View.VISIBLE);
                    notes_tab.setVisibility(View.VISIBLE);
                    qpapers_tab.setVisibility(View.VISIBLE);
                    m_assignments.setVisibility(View.GONE);
                    m_prac.setVisibility(View.GONE);
                    m_notes.setVisibility(View.GONE);
                    m_qpapers.setVisibility(View.GONE);
                    nodata.setVisibility(View.GONE);
                    temp = true;
            }
                break;

            case R.id.qpapers_tab:
                if (temp) {
                    ass_tab.setVisibility(View.GONE);
                    prac_tab.setVisibility(View.GONE);
                    notes_tab.setVisibility(View.GONE);
                    qpapers_tab.setVisibility(View.VISIBLE);
                    m_assignments.setVisibility(View.GONE);
                    m_prac.setVisibility(View.GONE);
                    m_notes.setVisibility(View.GONE);
                    m_qpapers.setVisibility(View.VISIBLE);
                    temp = false;
                }
                else {
                    ass_tab.setVisibility(View.VISIBLE);
                    prac_tab.setVisibility(View.VISIBLE);
                    notes_tab.setVisibility(View.VISIBLE);
                    qpapers_tab.setVisibility(View.VISIBLE);
                    m_assignments.setVisibility(View.GONE);
                    m_prac.setVisibility(View.GONE);
                    m_notes.setVisibility(View.GONE);
                    m_qpapers.setVisibility(View.GONE);
                    temp = true;
                }
                break;

            case R.id.prac_tab:
                if (temp) {
                    ass_tab.setVisibility(View.GONE);
                    prac_tab.setVisibility(View.VISIBLE);
                    notes_tab.setVisibility(View.GONE);
                    qpapers_tab.setVisibility(View.GONE);
                    m_assignments.setVisibility(View.GONE);
                    m_prac.setVisibility(View.VISIBLE);
                    m_notes.setVisibility(View.GONE);
                    m_qpapers.setVisibility(View.GONE);
                    temp = false;
                }
               else {
                ass_tab.setVisibility(View.VISIBLE);
                prac_tab.setVisibility(View.VISIBLE);
                notes_tab.setVisibility(View.VISIBLE);
                qpapers_tab.setVisibility(View.VISIBLE);
                m_assignments.setVisibility(View.GONE);
                m_prac.setVisibility(View.GONE);
                m_notes.setVisibility(View.GONE);
                m_qpapers.setVisibility(View.GONE);
                temp = true;
            }
            break;
        }
    }

}
