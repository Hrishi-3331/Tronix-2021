package com.hrishi_3331.hrishi_studio.tronix2021;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class addPost extends AppCompatActivity {

    private EditText questionbox, descriptionbox;
    private final int IMAGE_REQUEST_CODE = 8;
    boolean ImageAttached;
    private Uri imaage_uri;
    ImageView attImage;
    CoordinatorLayout cody;
    DatabaseReference aref;
    FirebaseAuth aAuth;
    FirebaseUser aUser;
    StorageReference aStore;
    ProgressDialog dialog;
    private RadioGroup post_category;
    private RadioButton qtype_post;
    private RadioButton ptype_post;
    private String postCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        questionbox = (EditText)findViewById(R.id.question);
        descriptionbox = (EditText)findViewById(R.id.question_desc);
        ImageAttached = false;
        attImage = (ImageView)findViewById(R.id.add_post_image);
        cody = (CoordinatorLayout)findViewById(R.id.cod);

        aref = FirebaseDatabase.getInstance().getReference().child("userposts");
        aAuth = FirebaseAuth.getInstance();
        aUser = aAuth.getCurrentUser();
        aStore = FirebaseStorage.getInstance().getReference().child("postImages");
        dialog = new ProgressDialog(addPost.this);

        post_category = (RadioGroup)findViewById(R.id.post_type);
        qtype_post = (RadioButton)findViewById(R.id.post_question_type);
        ptype_post = (RadioButton)findViewById(R.id.post_post_type);

        postCategory = "Question";
        radioListner();

    }

    public void radioListner(){
        post_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.post_post_type:
                        questionbox.setHint("Whats in your mind?");
                        postCategory = "Post";
                        break;

                    case R.id.post_question_type:
                        questionbox.setHint("Type your question or query here");
                        postCategory = "Question";
                        break;
                }
            }
        });
    }

    public void cancel(View view){
        finish();
    }

    public void selectPic(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    public void submit(View view){

        dialog.setTitle("Uploading Post");
        dialog.setMessage("Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        String question = questionbox.getText().toString();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        java.util.Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String mDate = format.format(date);

        if (!question.isEmpty()){
            dialog.show();
            final DatabaseReference ref1 = aref.push();
            DatabaseReference ref6 = ref1.child("date");
            ref6.setValue(mDate);
            DatabaseReference ref2 = ref1.child("author");
            ref2.setValue(aUser.getDisplayName());
            DatabaseReference ref3 = ref1.child("id");
            ref3.setValue(ref2.getParent().getKey());
            DatabaseReference ref4 = ref1.child("question");
            ref4.setValue(question);
            ref1.child("description").setValue(descriptionbox.getText().toString());
            ref1.child("category").setValue(postCategory);
            final DatabaseReference ref5 = ref1.child("image");
            if (ImageAttached){
                final StorageReference sref = aStore.child(imaage_uri.getLastPathSegment());
                sref.putFile(imaage_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String download_uri = uri.toString();

                                    ref5.setValue(download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialog.dismiss();
                                            if (task.isSuccessful()){
                                                finish();
                                            }
                                            else {
                                                Snackbar snackbar = Snackbar.make(cody, "Error in uploading post", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
            else {
                ref5.setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()){
                            finish();
                        }
                        else {
                            Snackbar snackbar = Snackbar.make(cody, "Error in uploading post", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
            }

        }
        else {
            Snackbar snackbar = Snackbar.make(cody, "Please enter your question", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null) {
                imaage_uri = data.getData();
                attImage.setImageURI(imaage_uri);
                ImageAttached = true;
            }

        }
    }
}
