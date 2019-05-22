package com.hrishi_3331.hrishi_studio.tronix2021;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class authentication extends AppCompatActivity {

    EditText login_email, login_password, signup_email, signup_password, signup_confpassword, signup_id, signup_name, signup_mobile;
    ImageView signup_image;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorage;
    private DatabaseReference mref;
    boolean imageAttached, internalImage;
    private final static int REQUEST_CODE = 6;
    LinearLayout login_interface;
    LinearLayout signup_interface;
    CoordinatorLayout coordinatorLayout;
    ProgressDialog dialog;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        login_email = (EditText)findViewById(R.id.user_email);
        login_password = (EditText)findViewById(R.id.user_password);
        signup_email = (EditText)findViewById(R.id.auth_signup_email);
        signup_password= (EditText)findViewById(R.id.auth_signup_pass);
        signup_confpassword = (EditText)findViewById(R.id.auth_signup_conf_pass);
        signup_id = (EditText)findViewById(R.id.auth_id);
        signup_mobile = (EditText)findViewById(R.id.auth_signup_phone);
        signup_name = (EditText)findViewById(R.id.auth_name);

        signup_image = (ImageView)findViewById(R.id.auth_image);

        signup_interface = (LinearLayout)findViewById(R.id.signup_interface);
        login_interface = (LinearLayout)findViewById(R.id.login_interface);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.colayout);

        imageAttached = false;
        internalImage = false;

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("UserImages");

        dialog = new ProgressDialog(authentication.this);
        dialog.setTitle("Authenticating user");

        mref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            imageAttached = true;
            internalImage = false;
            image_uri = data.getData();
            signup_image.setImageURI(image_uri);
            cropImage();
        }
    }

    private void cropImage() {
        Drawable drawable = signup_image.getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        signup_image.setImageBitmap(getclip(bitmapDrawable.getBitmap()));
    }

    public static Bitmap getclip(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public void loginTab(View view){
        signup_interface.animate().alpha(0).setDuration(250).withEndAction(new Runnable() {
            @Override
            public void run() {
                signup_interface.setVisibility(View.GONE);
                login_interface.setAlpha(0);
                login_interface.setVisibility(View.VISIBLE);
                login_interface.animate().alpha(1).setDuration(250);
            }
        });
    }

    public void signupTab(View view) {
        login_interface.animate().alpha(0).setDuration(250).withEndAction(new Runnable() {
            @Override
            public void run() {
                login_interface.setVisibility(View.GONE);
                signup_interface.setAlpha(0);
                signup_interface.setVisibility(View.VISIBLE);
                signup_interface.animate().alpha(1).setDuration(250);
            }
        });
    }

    public void loginUser(View view){


        String email = login_email.getText().toString();
        String pass = login_password.getText().toString();
        dialog.setMessage("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        if (!email.isEmpty() && !pass.isEmpty()){
            dialog.show();

            try {
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (!task.isSuccessful()){
                            Exception exception = task.getException();
                            assert exception != null;
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, exception.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        else if (task.isSuccessful()){
                            Intent intent = new Intent(authentication.this, Main2Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }catch (Exception e){
                dialog.dismiss();
                e.printStackTrace();
            }

        }
        else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please enter email and password", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    public void pickImage(View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(authentication.this);
        LayoutInflater inflater = getLayoutInflater();
        View DialogLayout = inflater.inflate(R.layout.image_select_dialog, null);
        builder.setView(DialogLayout);

        ImageButton mav1 = (ImageButton)DialogLayout.findViewById(R.id.male_avtar_1);
        ImageButton mav2 = (ImageButton)DialogLayout.findViewById(R.id.male_avtar_2);
        ImageButton mav3 = (ImageButton)DialogLayout.findViewById(R.id.male_avtar_3);
        ImageButton mav4 = (ImageButton)DialogLayout.findViewById(R.id.male_avtar_4);
        ImageButton mav5 = (ImageButton)DialogLayout.findViewById(R.id.male_avtar_5);
        ImageButton mav6 = (ImageButton)DialogLayout.findViewById(R.id.male_avtar_6);
        ImageButton uploadImage = (ImageButton)DialogLayout.findViewById(R.id.upload_image_button);

        final AlertDialog Idialog = builder.create();

        mav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_image.setImageResource(R.drawable.mav);
                imageAttached = true;
                internalImage = true;
                image_uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/tronix2021-e751b.appspot.com/o/avtars%2Fmav.png?alt=media&token=5a78ad75-d258-47ec-8706-159e59f5505a");
                cropImage();
                Idialog.dismiss();
            }
        });

        mav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_image.setImageResource(R.drawable.mav1);
                imageAttached = true;
                internalImage = true;
                image_uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/tronix2021-e751b.appspot.com/o/avtars%2Fmav1.png?alt=media&token=5832a175-c5a3-4e2b-921c-21a49eb54d7d");
                cropImage();
                Idialog.dismiss();
            }
        });

        mav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_image.setImageResource(R.drawable.mav2);
                imageAttached = true;
                internalImage = true;
                image_uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/tronix2021-e751b.appspot.com/o/avtars%2Fmav2.png?alt=media&token=23d48f64-8d43-480e-8222-35c28f4cd92e");
                cropImage();
                Idialog.dismiss();
            }
        });

        mav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_image.setImageResource(R.drawable.mav4);
                imageAttached = true;
                internalImage = true;
                image_uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/tronix2021-e751b.appspot.com/o/avtars%2Fmav4.png?alt=media&token=e350fc5f-38a4-4651-b2fe-64413e2c1dd4");
                cropImage();
                Idialog.dismiss();
            }
        });

        mav5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_image.setImageResource(R.drawable.avfm);
                imageAttached = true;
                internalImage = true;
                image_uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/tronix2021-e751b.appspot.com/o/avtars%2Favfm.jpg?alt=media&token=981f97ce-43cd-47e8-ae38-a019d25c3af0");
                cropImage();
                Idialog.dismiss();
            }
        });

        mav6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_image.setImageResource(R.drawable.mav5);
                imageAttached = true;
                internalImage = true;
                image_uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/tronix2021-e751b.appspot.com/o/avtars%2Fmav5.png?alt=media&token=b9b6dc7d-dbe2-4165-8c8b-6e971cef06cc");
                cropImage();
                Idialog.dismiss();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
                Idialog.dismiss();
            }
        });
        Idialog.show();

        /* */
    }

    public void signupUser(View view){
        String email, pass, confpass, enrollment, name, mob;
        email = signup_email.getText().toString();
        pass = signup_password.getText().toString();
        confpass = signup_confpassword.getText().toString();
        enrollment = signup_id.getText().toString();
        name = signup_name.getText().toString();
        mob = signup_mobile.getText().toString().trim();



        if(!email.isEmpty() && !pass.isEmpty() && !name.isEmpty()){
            if(!confpass.isEmpty() && !enrollment.isEmpty() && imageAttached){
                if (confpass.equals(pass)){
                    signUp(email, pass, name, enrollment, mob);
                }
                else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Passwords did not match!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
            else {
                if(!imageAttached){
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Select profile image to signup", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Fields cannot be empty", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }
        else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please fill all details!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

    private void signUp(String email, String pass, final String name, final String enrol, final String mob) {
        dialog.setMessage("Creating account..");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        try {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mUser = mAuth.getCurrentUser();
                        if (!internalImage) {
                            final StorageReference temp = mStorage.child(image_uri.getLastPathSegment());
                            temp.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    temp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name).setPhotoUri(uri).build();
                                            mUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        DatabaseReference userref = mref.child("users").child(mUser.getUid());
                                                        userref.child("name").setValue(mUser.getDisplayName());
                                                        userref.child("contact").setValue(mob);
                                                        userref.child("enrol").setValue(enrol);
                                                        userref.child("photourl").setValue(Objects.requireNonNull(mUser.getPhotoUrl()).toString().trim());
                                                        userref.child("email").setValue(mUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                dialog.dismiss();
                                                                if (task.isSuccessful()) {
                                                                    Intent intent = new Intent(authentication.this, Main2Activity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "SignUp failed, Please try later", Snackbar.LENGTH_LONG);
                                                                    snackbar.show();
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "SignUp failed, Please try later", Snackbar.LENGTH_LONG);
                                                        snackbar.show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                        else {
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).setPhotoUri(image_uri).build();
                            mUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        DatabaseReference userref = mref.child("users").child(mUser.getUid());
                                        userref.child("name").setValue(mUser.getDisplayName());
                                        userref.child("contact").setValue(mob);
                                        userref.child("enrol").setValue(enrol);
                                        userref.child("photourl").setValue(Objects.requireNonNull(mUser.getPhotoUrl()).toString().trim());
                                        userref.child("email").setValue(mUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(authentication.this, Main2Activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "SignUp failed, Please try later", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "SignUp failed, Please try later", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }
                            });


                        }

                    }
                    else {
                        dialog.dismiss();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "SignUp Failed!, try again", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
