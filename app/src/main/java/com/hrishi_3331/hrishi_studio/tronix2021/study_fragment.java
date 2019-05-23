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
               R.id.ecl301, R.id.eel310, R.id.ecl204, R.id.ecl405, R.id.ecl401, R.id.csll01
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
            case R.id.ecl301:
                intent.putExtra("sub", "Analog Communication");
                intent.putExtra("code", "ecl 301");
                startActivity(intent);
                break;

            case R.id.eel310:
                intent.putExtra("sub", "Control Systems");
                intent.putExtra("code", "eel 310");
                startActivity(intent);
                break;


            case R.id.ecl204:
                intent.putExtra("sub", "Measurements and Instrumentations");
                intent.putExtra("code", "ecl 204");
                startActivity(intent);
                break;

            case R.id.ecl405:
                intent.putExtra("sub", "Wave Guides and Antennas");
                intent.putExtra("code", "ecl 405");
                startActivity(intent);
                break;

            case R.id.ecl401:
                intent.putExtra("sub", "Hardware Description Language");
                intent.putExtra("code", "ecl 401");
                startActivity(intent);
                break;

            case R.id.csll01:
                intent.putExtra("sub", "Computer Architecture and Organization");
                intent.putExtra("code", "csl 101");
                startActivity(intent);
                break;

        }
    }
}
