package com.hrishi_3331.hrishi_studio.tronix2021;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailedPost extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private static FirebaseUser mUser;
    private TextView postTitle;
    private ImageView postImage;
    private EditText comment_box;
    private RecyclerView commentsView;
    private LinearLayoutManager manager;
    private TextView Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        mRef = FirebaseDatabase.getInstance().getReference().child("userposts").child(id);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        postTitle = (TextView)findViewById(R.id.title_detail_feed);
        Description = (TextView)findViewById(R.id.content_detail_feed);
        postImage = (ImageView)findViewById(R.id.image_detail_feed);

        comment_box = (EditText)findViewById(R.id.comment_box);
        commentsView = (RecyclerView)findViewById(R.id.comments_view);

        manager = new LinearLayoutManager(DetailedPost.this, LinearLayoutManager.VERTICAL, true);
        manager.setStackFromEnd(true);
        commentsView.setLayoutManager(manager);

        updateView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<forumAnswer, CommentViewHolder> adapter = new FirebaseRecyclerAdapter<forumAnswer, CommentViewHolder>(forumAnswer.class, R.layout.comment_layout, CommentViewHolder.class, mRef.child("answers")) {
            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, forumAnswer model, int position) {
                viewHolder.setView(model.getAuthor(), model.getAns());
                viewHolder.setDeleteButton(model.getAuthor_id(), getRef(position), DetailedPost.this);
            }
        };

        commentsView.setAdapter(adapter);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView poster;
        private TextView content;
        private ImageButton deleteButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            poster = view.findViewById(R.id.comment_poster);
            content = view.findViewById(R.id.comment_content);
            deleteButton = view.findViewById(R.id.delete_btn);
        }

        public void setView(String poster, String content){
            this.poster.setText(poster);
            this.content.setText(content);
        }

        public void setDeleteButton(String poster_id, final DatabaseReference reference, final Context context){
            if (poster_id == null || !poster_id.equals(mUser.getUid())){
                deleteButton.setVisibility(View.GONE);
            }
            else {
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference.removeValue();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setTitle("Delete Comment").setMessage("Do you want to delete your comment?");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        }
    }

    public void postComment(View view){
        final String comment = comment_box.getText().toString().trim();
        InputMethodManager imm = (InputMethodManager) getSystemService(DetailedPost.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (!comment.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailedPost.this);
            builder.setTitle("Post comment?");
            builder.setMessage("Do you want to post this comment?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadComment(comment);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void uploadComment(String comment){
        final ProgressDialog dialog = new ProgressDialog(DetailedPost.this);
        dialog.setTitle("Please wait..");
        dialog.setCanceledOnTouchOutside(false);
        DatabaseReference ref = mRef.child("answers").push();
        ref.child("author").setValue(mUser.getDisplayName());
        ref.child("author_id").setValue(mUser.getUid());
        ref.child("ans").setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (!task.isSuccessful()){
                    Toast.makeText(DetailedPost.this, "Error in uploading your comment. Please try again later!", Toast.LENGTH_SHORT).show();
                }
                comment_box.setText(" ");
                comment_box.clearFocus();
            }
        });
    }

    private void updateView() {
        mRef.child("question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postTitle.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    Description.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(postImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
