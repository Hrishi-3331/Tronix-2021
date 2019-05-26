package com.hrishi_3331.hrishi_studio.tronix2021;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {

    private TextView profile_name, profile_email, profile_contact, profile_mob, prof_enroll;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mref;
    private ImageView prof_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_email = (TextView)findViewById(R.id.prof_email);
        profile_name = (TextView)findViewById(R.id.prof_name);
        prof_image = (ImageView)findViewById(R.id.profile_image);
        profile_contact = (TextView)findViewById(R.id.prof_contact);
        prof_enroll = (TextView)findViewById(R.id.prof_enrol);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        mref.child("contact").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile_contact.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mref.child("enrol").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prof_enroll.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        assert user != null;
        profile_name.setText(user.getDisplayName());
        profile_email.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).into(prof_image, new Callback() {
            @Override
            public void onSuccess() {
                Drawable drawable = prof_image.getDrawable();
                BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
                prof_image.setImageBitmap(getclip(bitmapDrawable.getBitmap()));
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
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
}
