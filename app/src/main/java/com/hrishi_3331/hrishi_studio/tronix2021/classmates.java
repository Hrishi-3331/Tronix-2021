package com.hrishi_3331.hrishi_studio.tronix2021;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class classmates extends AppCompatActivity {

    private RecyclerView ClassmatesView;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmates);

        Toolbar toolbar = (Toolbar)findViewById(R.id.classmates_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Classmates");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ClassmatesView = (RecyclerView)findViewById(R.id.classmateList);
        mref = FirebaseDatabase.getInstance().getReference().child("users");

       LinearLayoutManager manager = new LinearLayoutManager(classmates.this, LinearLayoutManager.VERTICAL, true);
       manager.setSmoothScrollbarEnabled(true);
       manager.setStackFromEnd(true);
       ClassmatesView.setLayoutManager(manager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Somophore, peopleViewHolder> adapter = new FirebaseRecyclerAdapter<Somophore, peopleViewHolder>(Somophore.class, R.layout.profile_card_contact, peopleViewHolder.class, mref) {
            @Override
            protected void populateViewHolder(peopleViewHolder viewHolder, Somophore model, int position) {
                viewHolder.setContact(model.getContact());
                viewHolder.getImageUrl(model.getPhotourl());
                viewHolder.setName(model.getName());
                viewHolder.setEnrollment(model.getEnrol());
                viewHolder.setProfile_picture();
                viewHolder.setListner(getApplicationContext());
                viewHolder.implementListner(classmates.this, model.getPhotourl());
            }
        };

        ClassmatesView.setAdapter(adapter);

    }

    public static class peopleViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView name;
        TextView enrollment;
        String contact;
        ImageButton callbtn;
        String imageUrl;
        ImageView profile_picture;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            name = (TextView)mView.findViewById(R.id.user_import_name);
            enrollment = (TextView)mView.findViewById(R.id.user_import_enroll);
            callbtn = (ImageButton)mView.findViewById(R.id.user_import_call);
            profile_picture = (ImageView)mView.findViewById(R.id.user_import_image);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setEnrollment(String enrollment) {
            this.enrollment.setText(enrollment);
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public void setListner(final Context context){
            callbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ contact));
                    context.startActivity(intent);
                }
            });
        }

        public void getImageUrl(String imageUrl){
            this.imageUrl = imageUrl;
        }

        public void setProfile_picture(){
            Picasso.get().load(Uri.parse(imageUrl)).into(profile_picture);
            /*try {
                Picasso.get().load(Uri.parse(imageUrl)).into(profile_picture, new Callback() {
                    @Override
                    public void onSuccess() {
                        Drawable drawable = profile_picture.getDrawable();
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                        try {
                            profile_picture.setImageBitmap(getclip(bitmapDrawable.getBitmap()));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            } */
        }

        public void implementListner(final Context context, final String image){

            profile_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog builder = new Dialog(context);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            //nothing;
                        }
                    });

                    ImageView imageView = new ImageView(context);
                    imageView.setAdjustViewBounds(true);
                    try {
                        Picasso.get().load(Uri.parse(image)).into(imageView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }
            });
        }



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
