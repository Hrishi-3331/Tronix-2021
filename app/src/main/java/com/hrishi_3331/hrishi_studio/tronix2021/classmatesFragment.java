package com.hrishi_3331.hrishi_studio.tronix2021;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


public class classmatesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View mView;
    private RecyclerView ClassmatesView;
    private DatabaseReference mref;
    private static FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_classmates, container, false);

        ClassmatesView = (RecyclerView)mView.findViewById(R.id.classmateList);
        mref = FirebaseDatabase.getInstance().getReference().child("users");

        user = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        manager.setSmoothScrollbarEnabled(true);
        manager.setStackFromEnd(true);
        ClassmatesView.setLayoutManager(manager);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Somophore, peopleViewHolder> adapter = new FirebaseRecyclerAdapter<Somophore, peopleViewHolder>(Somophore.class, R.layout.profile_card_contact, peopleViewHolder.class, mref) {
            @Override
            protected void populateViewHolder(peopleViewHolder viewHolder, Somophore model, int position) {
                viewHolder.setContact(model.getContact());
                viewHolder.getImageUrl(model.getPhotourl());
                viewHolder.setName(model.getName());
                viewHolder.setEnrollment(model.getEnrol());
                viewHolder.setProfile_picture();
                String id = getRef(position).getKey();
                viewHolder.setListner(getActivity(), id);
                viewHolder.implementListner(getActivity(), model.getPhotourl());
            }
        };

        ClassmatesView.setAdapter(adapter);

    }

    public static class peopleViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView name;
        TextView enrollment;
        String contact;
        ImageButton callbtn, msgbtn;
        String imageUrl;
        RoundedImageView profile_picture;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            name = (TextView)mView.findViewById(R.id.user_import_name);
            enrollment = (TextView)mView.findViewById(R.id.user_import_enroll);
            callbtn = (ImageButton)mView.findViewById(R.id.user_import_call);
            msgbtn = (ImageButton)mView.findViewById(R.id.user_import_message);
            profile_picture = (RoundedImageView) mView.findViewById(R.id.user_import_image);
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

        public void setListner(final Context context, final String id){
            callbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ contact));
                    context.startActivity(intent);
                }
            });

            msgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, chat.class);
                    intent.putExtra("sender", id);
                    MessageUIDgenerator uiDgenerator = new MessageUIDgenerator(user.getUid(), id);
                    uiDgenerator.generateMessageId();
                    intent.putExtra("id",uiDgenerator.getMessageId());
                    context.startActivity(intent);
                }
            });
        }

        public void getImageUrl(String imageUrl){
            this.imageUrl = imageUrl;
        }

        public void setProfile_picture(){
            Picasso.get().load(Uri.parse(imageUrl)).into(profile_picture);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
