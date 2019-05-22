package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class view_answers extends AppCompatActivity {

    private TextView question_view;
    private RecyclerView answers_view;
    private String qid;
    private String mQuestion;
    private DatabaseReference aDatabase;
    private LinearLayout nullbox;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answers);

        count = 3331;
        question_view = (TextView)findViewById(R.id.question_for_ans);
        answers_view = (RecyclerView)findViewById(R.id.ansView);
        Intent viewIntent = getIntent();
        qid = viewIntent.getStringExtra("id");
        mQuestion = viewIntent.getStringExtra("question");
        question_view.setText(mQuestion);
        aDatabase = FirebaseDatabase.getInstance().getReference().child("userposts").child(qid).child("answers");

        answers_view.setLayoutManager(new LinearLayoutManager(view_answers.this, LinearLayoutManager.VERTICAL, false));

        nullbox = (LinearLayout)findViewById(R.id.null_box);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<forumAnswer, ansViewHolder> adapter = new FirebaseRecyclerAdapter<forumAnswer, ansViewHolder>(forumAnswer.class, R.layout.answercard_layout, ansViewHolder.class, aDatabase) {
            @Override
            protected void populateViewHolder(ansViewHolder viewHolder, forumAnswer model, int position) {
                viewHolder.setAns(model.getAns());
                viewHolder.setAuthorname(model.getAuthor());
                viewHolder.setAnsImage(model.getImage());
                count = position;
            }

            @Override
            public void onBindViewHolder(ansViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
                if (position >= 0){
                    nullbox.setVisibility(View.GONE);
                }
            }
        };

        answers_view.setAdapter(adapter);
    }

    public static class ansViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView authorname;
        TextView ans;
        ImageView ansImage;

        public ansViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            authorname = (TextView)mView.findViewById(R.id.someone);
            ans = (TextView)mView.findViewById(R.id.ans_area);
            ansImage = (ImageView)mView.findViewById(R.id.answer_image);
        }

        public void  setAuthorname(String author){
            authorname.setText(author);
        }

        public void setAns(String ans1){
            ans.setText(ans1);
        }

        public void setAnsImage(String url){
            if (url.equals("null")){
                ansImage.setVisibility(View.GONE);
            }
            else {
                Picasso.get().load(url).into(ansImage);
            }
        }
    }
}
