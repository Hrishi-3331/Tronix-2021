package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class study_fragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_fragment, container, false);
       int[] ids = {
               R.id.ENL302, R.id.ENL302, R.id.ECL426, R.id.ECL303, R.id.ECL415, R.id.ECL304
       };

       for (int id : ids){
           view.findViewById(id).setOnClickListener(this);
       }
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), subject.class);
        switch (v.getId()){
            case R.id.ENL302:
                intent.putExtra("sub", "DEVICE MODELING");
                intent.putExtra("code", "ENL302");
                startActivity(intent);
                break;

            case R.id.ECL403:
                intent.putExtra("sub", "Embedded Systems");
                intent.putExtra("code", "ECL403");
                startActivity(intent);
                break;


            case R.id.ECL426:
                intent.putExtra("sub", "ADVANCED MICROPROCESSOR AND INTERFACING");
                intent.putExtra("code", "ECL426");
                startActivity(intent);
                break;

            case R.id.ECL303:
                intent.putExtra("sub", "Digital Communication");
                intent.putExtra("code", "ECL303");
                startActivity(intent);
                break;

            case R.id.ECL415:
                intent.putExtra("sub", "ELECTRONIC SYSTEM DESIGN");
                intent.putExtra("code", "ECL415");
                startActivity(intent);
                break;

            case R.id.ECL304:
                intent.putExtra("sub", "Digital Signal Processing");
                intent.putExtra("code", "ECL304");
                startActivity(intent);
                break;

            case R.id.ECL404:
                intent.putExtra("sub", "RF & MICROWAVE ENGINEERING");
                intent.putExtra("code", "ECL404");
                startActivity(intent);
                break;

            case R.id.CSL312:
                intent.putExtra("sub", "OPERATING SYSTEMS");
                intent.putExtra("code", "CSL312");
                startActivity(intent);
                break;

        }
    }
}
