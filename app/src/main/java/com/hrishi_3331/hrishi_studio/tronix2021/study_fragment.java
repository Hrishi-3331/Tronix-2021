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
               R.id.ecl308, R.id.ecl211, R.id.ecl306, R.id.ecl305, R.id.mal205
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
            case R.id.ecl308:
                intent.putExtra("sub", "Analog Circuit Design");
                intent.putExtra("code", "ecl 308");
                startActivity(intent);
                break;

            case R.id.ecl211:
                intent.putExtra("sub", "Signals and Systems");
                intent.putExtra("code", "ecl 211");
                startActivity(intent);
                break;


            case R.id.ecl306:
                intent.putExtra("sub", "Microprocessors and Interfacing");
                intent.putExtra("code", "ecl 306");
                startActivity(intent);
                break;

            case R.id.ecl305:
                intent.putExtra("sub", "Electromagnetic Fields");
                intent.putExtra("code", "ecl 305");
                startActivity(intent);
                break;

            case R.id.mal205:
                intent.putExtra("sub", "Numerical Methods and Probability");
                intent.putExtra("code", "mal 205");
                startActivity(intent);
                break;

        }
    }
}
