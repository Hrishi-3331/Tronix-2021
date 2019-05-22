package com.hrishi_3331.hrishi_studio.tronix2021;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class contact extends AppCompatActivity {

    private DatabaseReference mref;
    private EditText subject;
    private EditText message;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.contact_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact Form");

        mref = FirebaseDatabase.getInstance().getReference().child("UserMessages");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        subject = (EditText)findViewById(R.id.contact_subject);
        message = (EditText)findViewById(R.id.contact_message);
    }

    public void sendMessage(View view){

        String msubject = subject.getText().toString();
        String mmessage = message.getText().toString();

        if(!mmessage.isEmpty() && !msubject.isEmpty()){
           DatabaseReference href1 = mref.push();
           href1.child("sender").setValue(mUser.getDisplayName());
           href1.child("subject").setValue(msubject);
           href1.child("message").setValue(mmessage).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       Toast.makeText(contact.this, "Message sent to developer. We will get back to you soon!", Toast.LENGTH_LONG).show();
                       finish();
                   }
                   else {
                       Toast.makeText(contact.this, "Message sending failed! Please try again later", Toast.LENGTH_LONG).show();
                   }
               }
           });

        }
        else {
            Toast.makeText(this, "Message and Subject fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
