package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class Messages extends AppCompatActivity {

    private RecyclerView message_list;
    private DatabaseReference jRef, hRef;
    private FirebaseAuth jAuth;
    private static FirebaseUser jUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Messages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        message_list = (RecyclerView)findViewById(R.id.message_list);

        jAuth = FirebaseAuth.getInstance();
        jUser = jAuth.getCurrentUser();
        jRef = FirebaseDatabase.getInstance().getReference().child("users").child(jUser.getUid()).child("Messages");

        LinearLayoutManager manager = new LinearLayoutManager(Messages.this, LinearLayoutManager.VERTICAL, true);
        manager.setStackFromEnd(true);
        manager.setSmoothScrollbarEnabled(true);
        message_list.setLayoutManager(manager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ChatEntry, MessageViewHolder> adapter = new FirebaseRecyclerAdapter<ChatEntry, MessageViewHolder>(ChatEntry.class, R.layout.message_tab, MessageViewHolder.class, jRef) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, ChatEntry model, int position) {
                viewHolder.setMessageBox(model.getId());
                viewHolder.setListners(Messages.this);
            }
        };
        message_list.setAdapter(adapter);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        private View jView;
        private RoundedImageView jImage;
        private TextView sender_name;
        private TextView Message;
        private String sender_id;
        private String id;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            jView = itemView;
            jImage = (RoundedImageView) jView.findViewById(R.id.sender_image);
            sender_name = (TextView)jView.findViewById(R.id.sender_name);
            Message = (TextView)jView.findViewById(R.id.sender_last_message);
        }

        public void setMessageBox(String id){
            this.id = id;
            DatabaseReference tRef = FirebaseDatabase.getInstance().getReference().child("Chats").child(id);
            tRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String sender = dataSnapshot.child("sender1").getValue().toString();
                    if (!sender.equals(jUser.getUid())){
                        sender_id = sender;
                        FirebaseDatabase.getInstance().getReference().child("users").child(sender).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                sender_name.setText(dataSnapshot.getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference().child("users").child(sender).child("photourl").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                    Picasso.get().load(dataSnapshot.getValue().toString()).into(jImage);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else {
                            sender = dataSnapshot.child("sender2").getValue().toString();
                            sender_id = sender;
                            FirebaseDatabase.getInstance().getReference().child("users").child(sender).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    sender_name.setText(dataSnapshot.getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            FirebaseDatabase.getInstance().getReference().child("users").child(sender).child("photourl").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try{
                                        Picasso.get().load(dataSnapshot.getValue().toString()).into(jImage);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    }

                    Message.setText(dataSnapshot.child("last_message").getValue().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setListners(final Context context){
            jView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, chat.class);
                    intent.putExtra("sender", sender_id);
                    intent.putExtra("id",id);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void New(View view){
        Intent intent = new Intent(Messages.this, classmates.class);
        startActivity(intent);
    }
}
