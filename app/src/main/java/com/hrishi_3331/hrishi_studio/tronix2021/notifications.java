package com.hrishi_3331.hrishi_studio.tronix2021;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class notifications extends AppCompatActivity {

    private RecyclerView noticeView;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        Toolbar toolbar = (Toolbar)findViewById(R.id.notifications_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student Notices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noticeView = (RecyclerView)findViewById(R.id.noticeView);
        mref = FirebaseDatabase.getInstance().getReference().child("notices");

        LinearLayoutManager manager = new LinearLayoutManager(notifications.this, LinearLayoutManager.VERTICAL, true);
        manager.setStackFromEnd(true);
        manager.setSmoothScrollbarEnabled(true);
        noticeView.setLayoutManager(manager);
        noticeView.hasFixedSize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<notice, noticeViewHolder> adapter = new FirebaseRecyclerAdapter<notice, noticeViewHolder>(notice.class, R.layout.notice_board, noticeViewHolder.class, mref) {
            @Override
            protected void populateViewHolder(noticeViewHolder viewHolder, notice model, int position) {
                viewHolder.setTitleView(model.getTitle());
                viewHolder.setContentView(model.getContent());
                viewHolder.setDateView(model.getDate());

            }
        };

        noticeView.setAdapter(adapter);
    }

    public static class noticeViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView title;
        TextView date;
        TextView content;


        public noticeViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            title = (TextView)mView.findViewById(R.id.notice_tt);
            date = (TextView)mView.findViewById(R.id.notice_dt);
            content = (TextView)mView.findViewById(R.id.notice_text);
        }

        public void setContentView(String text) {
            content.setText(text);
        }

        public void setTitleView(String title) {
            this.title.setText(title);
        }

        public void setDateView(String date) {
            this.date.setText(date);
        }
    }


}
