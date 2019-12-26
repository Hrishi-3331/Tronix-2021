package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class chat extends AppCompatActivity {

    private String sender_id;
    private static String sender_photo, my_photo;
    private String conversation_id;
    private DatabaseReference jRef;
    private TextView name;
    private RoundedImageView image;
    private RecyclerView messages;
    private EditText new_message;
    private String contact;
    private LinearLayoutManager manager;
    private static FirebaseUser user;
    private ScrollView scroller;
    private static String Sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        sender_id = intent.getStringExtra("sender");
        conversation_id = intent.getStringExtra("id");

        name = (TextView)findViewById(R.id.m_name);
        image = (RoundedImageView) findViewById(R.id.m_image);
        new_message = (EditText)findViewById(R.id.new_message);
        messages = (RecyclerView)findViewById(R.id.chat_messages);
        scroller = (ScrollView)findViewById(R.id.scroller) ;

        user = FirebaseAuth.getInstance().getCurrentUser();

        my_photo = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();

        FirebaseDatabase.getInstance().getReference().child("users").child(sender_id).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue().toString());
                Sender = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(sender_id).child("photourl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    sender_photo = dataSnapshot.getValue().toString();
                    Picasso.get().load(sender_photo).into(image);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(sender_id).child("contact").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contact = dataSnapshot.getValue().toString();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        manager = new LinearLayoutManager(chat.this, LinearLayoutManager.VERTICAL, false);
        manager.setSmoothScrollbarEnabled(true);
        manager.setStackFromEnd(true);
        messages.setLayoutManager(manager);

        jRef = FirebaseDatabase.getInstance().getReference().child("Chats").child(conversation_id).child("conversation");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Message, ConversationViewHolder> adapter = new FirebaseRecyclerAdapter<Message, ConversationViewHolder>(Message.class, R.layout.chat_message, ConversationViewHolder.class, jRef) {
            @Override
            protected void populateViewHolder(ConversationViewHolder viewHolder, Message model, int position) {
                viewHolder.setMessage(model.getSender(), model.getContent(), model.getDate());
            }
        };

        messages.setAdapter(adapter);
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private String sender;
        private TextView message, senderv, date;
        private RoundedImageView imageView;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            message = mView.findViewById(R.id.message_text);
            senderv = mView.findViewById(R.id.message_sender);
            date = mView.findViewById(R.id.message_date);
            imageView = (RoundedImageView)mView.findViewById(R.id.message_sender_image);
        }

        public void setMessage(String sender, String message, String date){
            this.message.setText(message);
            this.date.setText(date);
            this.sender = sender;
            if (sender.equals(user.getUid())){
               senderv.setText("You");
               try {
                   Picasso.get().load(my_photo).into(imageView);
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
            else {
                senderv.setText(Sender);
                try {
                    Picasso.get().load(sender_photo).into(imageView);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessage(View view){
        String message = new_message.getText().toString();
        FirebaseDatabase.getInstance().getReference().child("Chats").child(conversation_id).child("sender1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats").child(conversation_id);
                    reference.child("sender1").setValue(user.getUid());
                    reference.child("sender2").setValue(sender_id);
                    FirebaseDatabase.getInstance().getReference().child("users").child(sender_id).child("Messages").push().child("id").setValue(conversation_id);
                    FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Messages").push().child("id").setValue(conversation_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference aref = FirebaseDatabase.getInstance().getReference().child("Chats").child(conversation_id).child("conversation");
        if (!message.isEmpty()){
            DatabaseReference messageRef = aref.push();
            messageRef.child("sender").setValue(user.getUid());
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            java.util.Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String mDate = format.format(date);
            messageRef.child("date").setValue(mDate);
            FirebaseDatabase.getInstance().getReference().child("Chats").child(conversation_id).child("last_message").setValue(message);
            messageRef.child("content").setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        new_message.setText("");
                    }
                    else {
                        Toast.makeText(chat.this, "Message not sent...try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        scroller.fullScroll(ScrollView.FOCUS_DOWN);

    }

    public void finishActivity(View view){
        finish();
    }

    public void callUser(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ contact));
        startActivity(intent);
    }

}
