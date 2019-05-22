package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class attendance_fragment extends Fragment {

    TextView dateV;
    TextView dayV;
    RecyclerView recyclerView;
    DatabaseReference mref;
    SharedPreferences mpreferences;
    ImageButton managebutton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forum_fragment, container, false);
        AttendanceDataManager manager  = new AttendanceDataManager(getActivity());

        dateV = (TextView)view.findViewById(R.id.today_date);
        dayV = (TextView)view.findViewById(R.id.today_day);
        managebutton = (ImageButton)view.findViewById(R.id.manage_action);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dayformat = new SimpleDateFormat("EEEE");

        String date_today = dateFormat.format(date);
        String day_today = dayformat.format(date);

        dateV.setText(date_today);
        dayV.setText(day_today);

        recyclerView = (RecyclerView)view.findViewById(R.id.scheduleView);
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager1);

        mref = FirebaseDatabase.getInstance().getReference().child("timetable").child(day_today.toLowerCase());

        mpreferences = getActivity().getSharedPreferences("User_attendance", Context.MODE_PRIVATE);

        managebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), overall_attendance.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<msubjects, mViewHolder> adapter =new FirebaseRecyclerAdapter<msubjects, mViewHolder>(msubjects.class, R.layout.attendance_card, mViewHolder.class, mref) {
            @Override
            protected void populateViewHolder(mViewHolder viewHolder, msubjects model, int position) {
                viewHolder.setSubject(model.getName());
                viewHolder.fillDetails(model.getCode(), getActivity());
                viewHolder.implementListners(model.getCode(), getActivity());
                viewHolder.updateUI(model.getCode(), getActivity());
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public static class mViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView subject, total_classes, attended_classes, percent_attendance;
        TextView present_btn, absent_btn, off_btn;
        ImageView spclImage;
        LinearLayout holidaybox;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            subject = (TextView)mView.findViewById(R.id.class_name);
            total_classes = (TextView)mView.findViewById(R.id.tt_class);
            attended_classes = (TextView)mView.findViewById(R.id.att_class);
            percent_attendance = (TextView)mView.findViewById(R.id.pr_class);
            present_btn = (TextView)mView.findViewById(R.id.btn_present);
            absent_btn = (TextView)mView.findViewById(R.id.btn_absent);
            off_btn = (TextView)mView.findViewById(R.id.btn_off);
            spclImage = (ImageView) mView.findViewById(R.id.spcl_image);
            holidaybox = (LinearLayout)mView.findViewById(R.id.holiday_box);
        }

        public void setSubject(String subject) {
            if (subject.equals("Holiday")) {
                spclImage.setImageResource(R.drawable.chilltime);
                present_btn.setVisibility(View.GONE);
                absent_btn.setVisibility(View.GONE);
                off_btn.setVisibility(View.GONE);
                holidaybox.setVisibility(View.GONE);
            }
            else {
                this.subject.setText(subject);
            }
        }

        public void updateUI(String code, Context context ){

            if (!isUptoDate(code, context)){
                off_btn.setBackgroundColor(Color.TRANSPARENT);
                present_btn.setBackgroundColor(Color.TRANSPARENT);
                absent_btn.setBackgroundColor(Color.TRANSPARENT);
            }

            else {
                SharedPreferences preferences = context.getSharedPreferences("User_Attendance", Context.MODE_PRIVATE);
                String last_operation = preferences.getString(code + "lo", "o");

                switch (last_operation){
                    case "o":
                    {
                        off_btn.setBackgroundColor(Color.rgb(224, 224, 224));
                        break;
                    }

                    case "a":
                    {
                        absent_btn.setBackgroundColor(Color.rgb(224, 224, 224));
                        break;
                    }

                    case "p":
                    {
                        present_btn.setBackgroundColor(Color.rgb(224, 224, 224));
                        break;
                    }
                }
            }
        }


        public void fillDetails(String code, Context context){
            SharedPreferences sharedPreferences = context.getSharedPreferences("User_Attendance", Context.MODE_PRIVATE);
            int attClass = sharedPreferences.getInt(code + "att", 0);
            int totClass = sharedPreferences.getInt(code + "tot", 0);
            double perc = 0;
            if (totClass > 0){
                perc = Math.round((double)attClass / (double)totClass * 100);
            }

            total_classes.setText(String.valueOf(totClass));
            attended_classes.setText(String.valueOf(attClass));
            percent_attendance.setText(String.valueOf(perc) + "%");

        }

        boolean isUptoDate(final String code, final Context context){
            SharedPreferences sharedPreferences = context.getSharedPreferences("User_Attendance", Context.MODE_PRIVATE);
            String last_updated = sharedPreferences.getString(code + "lu", "never");
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String mDate = dateFormat.format(date);
            return mDate.equals(last_updated);
        }


        public void implementListners(final String code, final Context context){

            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            final String mDate = dateFormat.format(date);

            present_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    present_btn.setBackgroundColor(Color.rgb(224, 224, 224));
                    SharedPreferences preferences = context.getSharedPreferences("User_Attendance", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    if (!isUptoDate(code, context)){
                        int temp = preferences.getInt(code + "tot", 0);
                        int temp2 = preferences.getInt(code + "att", 0);

                        editor.putInt(code + "tot", temp + 1).commit();
                        editor.putInt(code + "att", temp2 + 1).commit();
                        editor.putString(code + "lu",mDate ).commit();
                        editor.putString(code + "lo","p" ).commit();
                        editor.commit();
                        fillDetails(code, context);
                    }

                    else {
                        String last_operation = preferences.getString(code + "lo", "o");
                        int temp = preferences.getInt(code + "tot", 0);
                        int temp2 = preferences.getInt(code + "att", 0);

                        switch (last_operation){

                            case "o":
                            {
                                off_btn.setBackgroundColor(Color.TRANSPARENT);
                                editor.putInt(code + "tot", temp + 1).commit();
                                editor.putInt(code + "att", temp2 + 1).commit();
                                editor.putString(code + "lo","p" ).commit();
                                editor.commit();
                                fillDetails(code, context);
                                break;
                            }

                            case "a":
                            {
                                absent_btn.setBackgroundColor(Color.TRANSPARENT);
                                editor.putInt(code + "tot", temp);
                                editor.putInt(code + "att", temp2 + 1);
                                editor.putString(code + "lo","p" );
                                editor.commit();
                                fillDetails(code, context);
                                break;

                            }

                            case "p":
                            {
                                break;
                            }
                        }
                    }
                }
            });

            absent_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    absent_btn.setBackgroundColor(Color.rgb(224, 224, 224));
                    SharedPreferences preferences = context.getSharedPreferences("User_Attendance", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    if (!isUptoDate(code, context)){
                        int temp = preferences.getInt(code + "tot", 0);

                        editor.putInt(code + "tot", temp + 1);
                        editor.putString(code + "lu",mDate );
                        editor.putString(code + "lo","a" );
                        editor.commit();
                        fillDetails(code, context);
                    }
                    else {
                        String last_operation = preferences.getString(code + "lo", "o");
                        int temp = preferences.getInt(code + "tot", 0);
                        int temp2 = preferences.getInt(code + "att", 0);

                        switch (last_operation){
                            case "o":
                            {
                                off_btn.setBackgroundColor(Color.TRANSPARENT);
                                editor.putInt(code + "tot", temp + 1);
                                editor.putInt(code + "att", temp2);
                                editor.putString(code + "lo","a" );
                                editor.commit();
                                fillDetails(code, context);
                                break;
                            }

                            case "a":
                            {
                                break;
                            }

                            case "p":
                            {
                                present_btn.setBackgroundColor(Color.TRANSPARENT);
                                editor.putInt(code + "tot", temp);
                                editor.putInt(code + "att", temp2 - 1);
                                editor.putString(code + "lo","a" );
                                editor.commit();
                                fillDetails(code, context);
                                break;
                            }
                        }
                    }
                }
            });

            off_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    off_btn.setBackgroundColor(Color.rgb(224, 224, 224));
                    SharedPreferences preferences = context.getSharedPreferences("User_Attendance", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    if (!isUptoDate(code, context)){
                        editor.putString(code + "lu",mDate );
                        editor.putString(code + "lo","o" );
                        editor.commit();
                    }
                    else {
                        String last_operation = preferences.getString(code + "lo", "o");
                        int temp = preferences.getInt(code + "tot", 0);
                        int temp2 = preferences.getInt(code + "att", 0);
                        switch (last_operation){
                            case "o":
                            {
                                break;
                            }

                            case "a":
                            {
                                absent_btn.setBackgroundColor(Color.TRANSPARENT);
                                editor.putInt(code + "tot", temp - 1);
                                editor.putInt(code + "att", temp2);
                                editor.putString(code + "lo","o" );
                                editor.commit();
                                fillDetails(code, context);
                                break;
                            }

                            case "p":
                            {
                                present_btn.setBackgroundColor(Color.TRANSPARENT);
                                editor.putInt(code + "tot", temp - 1);
                                editor.putInt(code + "att", temp2 - 1);
                                editor.putString(code + "lo","o" );
                                editor.commit();
                                fillDetails(code, context);
                                break;
                            }
                        }
                    }
                }
            });

        }
    }

    public void manageAttendance(View view){

    }
}
