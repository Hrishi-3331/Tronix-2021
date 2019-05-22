package com.hrishi_3331.hrishi_studio.tronix2021;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class forum_fragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ImageView userImage;
    private View mView;
    private RecyclerView postView;
    private DatabaseReference mref;
    private LinearLayout addPost;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.attendance_fragment, container, false);
        userImage = mView.findViewById(R.id.myuser_image);
        postView = (RecyclerView) mView.findViewById(R.id.postsView);

        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_feed);

        addPost = (LinearLayout) mView.findViewById(R.id.addPost);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mref = FirebaseDatabase.getInstance().getReference().child("userposts");

        Picasso.get().load(mUser.getPhotoUrl()).into(userImage, new Callback() {
            @Override
            public void onSuccess() {
                Drawable drawable = userImage.getDrawable();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                userImage.setImageBitmap(getclip(bitmapDrawable.getBitmap()));
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });


        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        manager.setStackFromEnd(true);
        manager.setSmoothScrollbarEnabled(true);
        postView.setLayoutManager(manager);
        postView.setNestedScrollingEnabled(false);
        postView.hasFixedSize();


        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.hrishi_3331.hrishi_studio.tronix2021.addPost.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (haveNetworkConnection()) {
                    Toast.makeText(getContext(), "Refreshing Feed", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Unable to connect to internet!", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return mView;

    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<post, postViewHolder> adapter = new FirebaseRecyclerAdapter<post, postViewHolder>(post.class, R.layout.post_layout, postViewHolder.class, mref) {
            @Override
            protected void populateViewHolder(postViewHolder viewHolder, post model, int position) {
                viewHolder.setAuthor(model.getAuthor());
                viewHolder.setDate(model.getDate());
                viewHolder.setQuestion(model.getQuestion(), model.getDescription());
                viewHolder.setImage(model.getImage());
                viewHolder.setPoll(model.getOption1(), model.getOption2(), model.getOption3(), model.getOption4(), model.getOption5(), model.getOption1_strength(), model.getOption2_strength(), model.getOption3_strength(), model.getOption4_strength(), model.getOption5_strength());
                viewHolder.setLayout(model.getCategory(), getActivity(), model.getId());
                viewHolder.implementListners(getActivity(), model.getId(), model.getQuestion(), model.getImage(), model.getCategory());
            }
        };

        postView.setAdapter(adapter);

    }

    public static class postViewHolder extends RecyclerView.ViewHolder {

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("userposts");
        View mView;
        TextView author, date, question, description;
        ImageView image;
        LinearLayout view_post;
        LinearLayout write_post, poll_view;
        TextView write_what, view_what, statement;
        TextView option1_text, option2_text, option3_text, option4_text, option5_text;
        TextView option1_strength, option2_strength, option3_strength, option4_strength, option5_strength;
        LinearLayout option1_box, option2_box, option3_box, option4_box, option5_box;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            author = mView.findViewById(R.id.post_author);
            date = mView.findViewById(R.id.post_date);
            question = mView.findViewById(R.id.post_question);
            description = mView.findViewById(R.id.post_question_desc);
            image = mView.findViewById(R.id.post_image);
            view_post = mView.findViewById(R.id.view_post);
            write_post = mView.findViewById(R.id.write_post);
            statement = (TextView) mView.findViewById(R.id.card_statement);
            view_what = (TextView) mView.findViewById(R.id.view_what);
            write_what = (TextView) mView.findViewById(R.id.write_what);
            poll_view = (LinearLayout) mView.findViewById(R.id.poll_box);
            option1_text = (TextView) mView.findViewById(R.id.poll_option_1_text);
            option2_text = (TextView) mView.findViewById(R.id.poll_option_2_text);
            option3_text = (TextView) mView.findViewById(R.id.poll_option_3_text);
            option4_text = (TextView) mView.findViewById(R.id.poll_option_4_text);
            option5_text = (TextView) mView.findViewById(R.id.poll_option_5_text);
            option1_strength = (TextView) mView.findViewById(R.id.poll_option_1_strength);
            option2_strength = (TextView) mView.findViewById(R.id.poll_option_2_strength);
            option3_strength = (TextView) mView.findViewById(R.id.poll_option_3_strength);
            option4_strength = (TextView) mView.findViewById(R.id.poll_option_4_strength);
            option5_strength = (TextView) mView.findViewById(R.id.poll_option_5_strength);
            option1_box = (LinearLayout) mView.findViewById(R.id.poll_option_1);
            option2_box = (LinearLayout) mView.findViewById(R.id.poll_option_2);
            option3_box = (LinearLayout) mView.findViewById(R.id.poll_option_3);
            option4_box = (LinearLayout) mView.findViewById(R.id.poll_option_4);
            option5_box = (LinearLayout) mView.findViewById(R.id.poll_option_5);
        }

        public void setQuestion(String question, String description) {
            this.question.setText(question);
            this.description.setText(description);
            if (this.description.getText().toString().length() == 0) {
                this.description.setVisibility(View.GONE);
            }
        }

        public void setDate(String date) {
            this.date.setText(date);
        }

        public void setAuthor(String author) {
            this.author.setText(author);
        }

        public void setImage(String image) {
            try {
                Uri uri = Uri.parse(image);
                Picasso.get().load(uri).into(this.image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setLayout(String category, Context context, String id) {
            switch (category) {
                case "Post":
                    statement.setText("added a post");
                    view_what.setText("View comments");
                    write_what.setText("Write Comment");
                    break;

                case "Poll":
                    poll_view.setVisibility(View.VISIBLE);
                    statement.setText("started a poll");
                    view_what.setText("View Comments");
                    write_what.setText("Write Comment");
                    break;

                case "Birthday":
                    statement.setText("Today's Birthdays");
                    view_what.setText("View Wishes");
                    write_what.setText("Wish now");
                    break;

                default:
                    statement.setText("posted a question");
                    view_what.setText("View Answers");
                    write_what.setText("Write Answers");
                    break;
            }

            SharedPreferences preferences = context.getSharedPreferences("pollpreferences", Context.MODE_PRIVATE);
            int temp = preferences.getInt(id, 0);
            switch (temp) {
                case 0:
                    break;

                case 1:
                    option1_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    break;

                case 2:
                    option2_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    break;

                case 3:
                    option3_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    break;

                case 4:
                    option4_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    break;

                case 5:
                    option5_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    break;
            }
        }

        public void setPoll(String option1, String option2, String option3, String option4, String option5, String option1_strength, String option2_strength, String option3_strength, String option4_strength, String option5_strength) {
            option1_text.setText(option1);
            this.option1_strength.setText(option1_strength);
            option2_text.setText(option2);
            this.option2_strength.setText(option2_strength);
            option3_text.setText(option3);
            this.option3_strength.setText(option3_strength);
            option4_text.setText(option4);
            this.option4_strength.setText(option4_strength);
            option5_text.setText(option5);
            this.option5_strength.setText(option5_strength);

            if (option1_text.getText().length() == 0) {
                option1_box.setVisibility(View.GONE);
            }
            if (option2_text.getText().length() == 0) {
                option2_box.setVisibility(View.GONE);
            }
            if (option3_text.getText().length() == 0) {
                option3_box.setVisibility(View.GONE);
            }
            if (option4_text.getText().length() == 0) {
                option4_box.setVisibility(View.GONE);
            }
            if (option5_text.getText().length() == 0) {
                option5_box.setVisibility(View.GONE);
            }

        }

        public void implementListners(final Context context, final String id, final String question, final String image_url, final String category) {
            write_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (category.equals("Birthday")){
                        Intent intent = new Intent(context.getApplicationContext(), answer_question.class);
                        intent.putExtra("id", id);
                        intent.putExtra("question", "Happy Birthday!");
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context.getApplicationContext(), answer_question.class);
                        intent.putExtra("id", id);
                        intent.putExtra("question", question);
                        context.startActivity(intent);
                    }
                }
            });

            view_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), view_answers.class);
                    intent.putExtra("id", id);
                    intent.putExtra("question", question);
                    context.startActivity(intent);
                }
            });

            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final DownloadFile file = new DownloadFile(id, context);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Download Image")
                            .setMessage("Do you want to download this image?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    file.execute(image_url);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return false;
                }
            });

            option1_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = context.getSharedPreferences("pollprferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    int temp = preferences.getInt(id, 0);
                    int str1 = Integer.valueOf(option1_strength.getText().toString());
                    int str2 = Integer.valueOf(option2_strength.getText().toString());
                    int str3 = Integer.valueOf(option3_strength.getText().toString());
                    int str4 = Integer.valueOf(option4_strength.getText().toString());
                    int str5 = Integer.valueOf(option5_strength.getText().toString());
                    switch (temp) {
                        case 0:
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 + 1));
                            break;

                        case 1:
                            break;

                        case 2:
                            option2_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 + 1));
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 - 1));
                            break;

                        case 3:
                            option3_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 + 1));
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 - 1));
                            break;

                        case 4:
                            option4_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 + 1));
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 - 1));
                            break;

                        case 5:
                            option5_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 + 1));
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 - 1));
                            break;
                    }
                    option1_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    editor.putInt(id, 1);
                    editor.commit();
                }
            });

            option2_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = context.getSharedPreferences("pollprferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    int temp = preferences.getInt(id, 0);
                    int str1 = Integer.valueOf(option1_strength.getText().toString());
                    int str2 = Integer.valueOf(option2_strength.getText().toString());
                    int str3 = Integer.valueOf(option3_strength.getText().toString());
                    int str4 = Integer.valueOf(option4_strength.getText().toString());
                    int str5 = Integer.valueOf(option5_strength.getText().toString());
                    switch (temp) {
                        case 0:
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 + 1));
                            break;

                        case 1:
                            option1_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 + 1));
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 - 1));
                            break;

                        case 2:
                            break;

                        case 3:
                            option3_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 + 1));
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 - 1));
                            break;

                        case 4:
                            option4_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 + 1));
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 - 1));
                            break;

                        case 5:
                            option5_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 + 1));
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 - 1));
                            break;
                    }
                    option2_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    editor.putInt(id, 2);
                    editor.commit();
                }
            });

            option3_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = context.getSharedPreferences("pollprferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    int temp = preferences.getInt(id, 0);
                    int str1 = Integer.valueOf(option1_strength.getText().toString());
                    int str2 = Integer.valueOf(option2_strength.getText().toString());
                    int str3 = Integer.valueOf(option3_strength.getText().toString());
                    int str4 = Integer.valueOf(option4_strength.getText().toString());
                    int str5 = Integer.valueOf(option5_strength.getText().toString());
                    switch (temp) {
                        case 0:
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 + 1));
                            break;

                        case 1:
                            option1_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 + 1));
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 - 1));
                            break;

                        case 2:
                            option2_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 + 1));
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 - 1));
                            break;

                        case 3:
                            break;

                        case 4:
                            option4_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 + 1));
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 - 1));
                            break;

                        case 5:
                            option5_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 + 1));
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 - 1));
                            break;
                    }
                    option3_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    editor.putInt(id, 3);
                    editor.commit();
                }
            });

            option4_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = context.getSharedPreferences("pollprferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    int temp = preferences.getInt(id, 0);
                    int str1 = Integer.valueOf(option1_strength.getText().toString());
                    int str2 = Integer.valueOf(option2_strength.getText().toString());
                    int str3 = Integer.valueOf(option3_strength.getText().toString());
                    int str4 = Integer.valueOf(option4_strength.getText().toString());
                    int str5 = Integer.valueOf(option5_strength.getText().toString());
                    switch (temp) {
                        case 0:
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 + 1));
                            break;

                        case 1:
                            option1_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 + 1));
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 - 1));
                            break;

                        case 2:
                            option2_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 + 1));
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 - 1));
                            break;

                        case 3:
                            option3_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 + 1));
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 - 1));
                            break;

                        case 4:
                            break;

                        case 5:
                            option5_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 + 1));
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 - 1));
                            break;
                    }
                    option4_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    editor.putInt(id, 4);
                    editor.commit();
                }
            });

            option5_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = context.getSharedPreferences("pollprferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    int temp = preferences.getInt(id, 0);
                    int str1 = Integer.valueOf(option1_strength.getText().toString());
                    int str2 = Integer.valueOf(option2_strength.getText().toString());
                    int str3 = Integer.valueOf(option3_strength.getText().toString());
                    int str4 = Integer.valueOf(option4_strength.getText().toString());
                    int str5 = Integer.valueOf(option5_strength.getText().toString());
                    switch (temp) {
                        case 0:
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 + 1));
                            break;

                        case 1:
                            option1_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 + 1));
                            mref.child(id).child("option1_strength").setValue(String.valueOf(str1 - 1));
                            break;

                        case 2:
                            option2_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 + 1));
                            mref.child(id).child("option2_strength").setValue(String.valueOf(str2 - 1));
                            break;

                        case 3:
                            option3_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 + 1));
                            mref.child(id).child("option3_strength").setValue(String.valueOf(str3 - 1));
                            break;

                        case 4:
                            option4_box.setBackgroundColor(Color.WHITE);
                            mref.child(id).child("option5_strength").setValue(String.valueOf(str5 + 1));
                            mref.child(id).child("option4_strength").setValue(String.valueOf(str4 - 1));
                            break;

                        case 5:
                            break;
                    }
                    option5_box.setBackgroundColor(Color.rgb(159, 168, 218));
                    editor.putInt(id, 5);
                    editor.commit();
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


    public static class DownloadFile extends AsyncTask<String, Integer, Long> {

        ProgressDialog mProgressDialog;
        String foldername = "Tronix2021Images";
        String image_id;
        Context context;

        public DownloadFile(String image_id, Context context) {
            this.image_id = image_id;
            this.context = context;
            mProgressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Downloading");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }

        @Override
        protected Long doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                String targetFileName = image_id + ".jpg";
                int lenghtOfFile = conexion.getContentLength();
                String PATH = Environment.getExternalStorageDirectory() + "/" + foldername + "/";
                File folder = new File(PATH);
                if (!folder.exists()) {
                    folder.mkdir();
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH + targetFileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setProgress(progress[0]);
            if (mProgressDialog.getProgress() == mProgressDialog.getMax()) {
                mProgressDialog.dismiss();
                Toast.makeText(context, "Image downloaded", Toast.LENGTH_SHORT).show();
            }
        }

        protected void onPostExecute(String result) {
        }
    }
}
