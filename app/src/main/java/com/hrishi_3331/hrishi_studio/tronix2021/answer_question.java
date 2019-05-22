package com.hrishi_3331.hrishi_studio.tronix2021;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class answer_question extends AppCompatActivity {

    EditText answer;
    private final int REQUEST = 7;
    private Uri image_uri;
    ImageView ansImage;
    boolean imageAttached;
    TextView question_view;
    DatabaseReference ref, ref1;
    StorageReference mref, mref1;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        answer = (EditText)findViewById(R.id.answer);
        ansImage = (ImageView)findViewById(R.id.ans_image);
        imageAttached = false;
        question_view = (TextView)findViewById(R.id.ques);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        ref = FirebaseDatabase.getInstance().getReference().child("userposts").child(id).child("answers");
        question_view.setText(intent.getStringExtra("question"));

        mref = FirebaseStorage.getInstance().getReference().child("ansImages");

        dialog = new ProgressDialog(answer_question.this);
        dialog.setTitle("Uploading your answer");
        dialog.setMessage("Please wait..");
        dialog.setCanceledOnTouchOutside(false);
    }

    public void cancelAns(View view){
        finish();
    }

    public void submitAns(View view){
        String ans = answer.getText().toString();

        if (!ans.isEmpty()){
            dialog.show();
            ref1 = ref.push();
            ref1.child("ans").setValue(ans);
            ref1.child("author").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            if (!imageAttached){
            ref1.child("image").setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    if (task.isSuccessful()){
                        Toast.makeText(answer_question.this, "Answer uploded!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(answer_question.this, "Error in uploading answer", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }
            else {
                mref1 = mref.child(image_uri.getLastPathSegment());
                mref1.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            mref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String download_url = uri.toString();
                                    ref1.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialog.dismiss();
                                            if (task.isSuccessful()){
                                                Toast.makeText(answer_question.this, "Answer uploded!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(answer_question.this, "Error in uploading answer", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }



        }
        else {
            Toast.makeText(this, "Answer field empty!", Toast.LENGTH_SHORT).show();
        }

    }

    public void selectAnsPic(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK){
            if (data != null) {
                image_uri = data.getData();
                ansImage.setImageURI(image_uri);
                imageAttached = true;
            }
        }
    }
}
