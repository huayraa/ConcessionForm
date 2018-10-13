package com.concessionform.rajmehta.concessionform;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstFragment extends Fragment{
    @Nullable
    TextView agedisp, classdisp, perioddisp;
    EditText agefield;
    Spinner spinnerclass, spinnerperiod;
    Button submitbtn;
    String age;
    String uid, PeriodSelect, ClassSelect;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("newFormApplication");

    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.first_fragment, container, false);
        agedisp = view.findViewById(R.id.agedisp);
        classdisp = view.findViewById(R.id.classdisp);
        perioddisp = view.findViewById(R.id.perioddisp);
        agefield = view.findViewById(R.id.agefield);
        spinnerclass = view.findViewById(R.id.spinnerclass);
        spinnerperiod = view.findViewById(R.id.spinnerperiod);
        submitbtn = view.findViewById(R.id.submitbtn);
        age = agefield.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String DOB = prefs.getString("DOB", "");
        final String PhoneNumber = prefs.getString("PhoneNumber", "");
        final String SourceStation = prefs.getString("SourceStation", "");
        final String address = prefs.getString("address", "");
        final String course = prefs.getString("course", "");
        final String name = prefs.getString("name", "");
        final String sexMF = prefs.getString("sexMF", "");

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = agefield.getText().toString();
                PeriodSelect = spinnerperiod.getSelectedItem().toString();
                ClassSelect = spinnerclass.getSelectedItem().toString();

                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(getActivity(),"No user", Toast.LENGTH_LONG).show();
                }
                else {
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                }

                String userID = mDatabase.push().getKey();
                FomSubmit entry = new FomSubmit(ClassSelect,DOB,PeriodSelect,PhoneNumber,SourceStation,address,age,course,name,sexMF,uid);
                mDatabase.child(userID).setValue(entry);

            }
        });
        return view;
    }

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState){

    }*/
}
