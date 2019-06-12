package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class chat extends AppCompatActivity {

    private String sender_id;
    private String conversation_id;
    private DatabaseReference jRef;
    private TextView name;
    private ImageView image;
    private RecyclerView messages;
    private EditText new_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        sender_id = intent.getStringExtra("sender");
        conversation_id = intent.getStringExtra("id");

        name = (TextView)findViewById(R.id.m_name);
        image = (ImageView)findViewById(R.id.m_image);

        FirebaseDatabase.getInstance().getReference().child("users").child(sender_id).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(sender_id).child("photourl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(image);
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

}
