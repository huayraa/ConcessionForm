package com.concessionform.rajmehta.concessionform;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UserDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText namefield, dobfield, addressfield, phonefield, sourcefield;
    TextView dobdisp, namedisp, addressdisp, coursedisp, genderdisp, phonedisp, sourcedisp;
    Spinner coursespinner, streamspinner;
    Button submit;
    RadioGroup radioGroup;
    String DOB, coursefinal,gender, namefinal, address, phonenum, source;
    String uid;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PermanentUserDetails");
    final Calendar myCalendar = Calendar.getInstance();

    private int mYear, mMonth, mDay;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_details);

        namefield = findViewById(R.id.namefield);
        dobfield = findViewById(R.id.dobfield);
        addressfield = findViewById(R.id.addressfield);
        phonefield = findViewById(R.id.phonefield);
        sourcefield = findViewById(R.id.sourcefield);
        coursedisp = findViewById(R.id.coursedisp);
        genderdisp = findViewById(R.id.genderdisp);

        submit = findViewById(R.id.submit);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();

        mYear = myCalendar.get(Calendar.YEAR);
        mMonth = myCalendar.get(Calendar.MONTH);
        mDay = myCalendar.get(Calendar.DAY_OF_MONTH);

        coursespinner = findViewById(R.id.coursespinner);
        streamspinner = findViewById(R.id.streamspinner);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        coursespinner.setOnItemSelectedListener(this);

        dobfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new DatePickerDialog(UserDetails.this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                DateDialog();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb1 = (RadioButton) group.findViewById(R.id.maleradio);
                RadioButton rb2 = (RadioButton) group.findViewById(R.id.femaleradio);
                RadioButton rb3 = (RadioButton) group.findViewById(R.id.othersradio);

                if(rb1.isChecked()){ gender = "Male";}
                if(rb2.isChecked()){ gender = "Female"; }
                if(rb3.isChecked()){ gender = "Others";}
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namefinal = namefield.getText().toString();
                address = addressfield.getText().toString();
                phonenum = phonefield.getText().toString();
                source = sourcefield.getText().toString();

                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(UserDetails.this, "No user logged in", Toast.LENGTH_LONG).show();;
                }
                else {
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                }

                String userID = mDatabase.push().getKey();
                User user = new User(DOB, address, coursefinal, namefinal, phonenum, gender, source, uid);
                mDatabase.child(userID).setValue(user);

                editor.putString("DOB",DOB);
                editor.putString("address",address);
                editor.putString("course", coursefinal);
                editor.putString("name", namefinal);
                editor.putString("PhoneNumber",phonenum);
                editor.putString("sexMF",gender);
                editor.putString("SourceStation",source);
                editor.commit();

                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });
    }

    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dobfield.setText(dayOfMonth + "-" + month + "-" + year);
                DOB = dobfield.getText().toString();
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, mYear, mMonth, mDay);
        dpDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sp1 = String.valueOf(coursespinner.getSelectedItem());
        if(sp1.contentEquals("B. Tech")){
            List<String> list = new ArrayList<String>();
            list.add("Electrical");
            list.add("Data Science");
            list.add("IT");
            list.add("Computer");
            list.add("Electronics and Telecommunication");
            list.add("Mechanical");
            list.add("Civil");
            list.add("Mechatronics");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            streamspinner.setAdapter(dataAdapter);

            coursefinal = coursespinner.getSelectedItem().toString();
            coursefinal = coursefinal+" "+streamspinner.getSelectedItem().toString();
        }

        if(sp1.contentEquals("B. Tech Integrated")){
            List<String> list = new ArrayList<String>();
            list.add("Computer");
            list.add("Mechanical");
            list.add("Civil");
            list.add("Electronics and Telecommunication");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            streamspinner.setAdapter(dataAdapter);

            coursefinal = coursespinner.getSelectedItem().toString();
            coursefinal = coursefinal+" "+streamspinner.getSelectedItem().toString();
        }

        if(sp1.contentEquals("MBA (Tech)")){
            List<String> list = new ArrayList<String>();
            list.add("Information Technology");
            list.add("Electronics & Telecommunications ");
            list.add("Chemical");
            list.add("Computer");
            list.add("Mechanical");
            list.add("Civil");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            streamspinner.setAdapter(dataAdapter);

            coursefinal = coursespinner.getSelectedItem().toString();
            coursefinal = coursefinal+" "+streamspinner.getSelectedItem().toString();
        }

        if(sp1.contentEquals("M. Tech")){
            List<String> list = new ArrayList<String>();
            list.add("Artificial Intelligence");
            list.add("Computer Engineering");
            list.add("Industry Automation");
            list.add("Electronics & Telecommunications");
            list.add("Data Sciences (Business Analytics)");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            streamspinner.setAdapter(dataAdapter);

            coursefinal = coursespinner.getSelectedItem().toString();
            coursefinal = coursefinal+" "+streamspinner.getSelectedItem().toString();
        }

        if(sp1.contentEquals("MCA")){
            coursefinal = coursespinner.getSelectedItem().toString();
        }
        if(sp1.contentEquals("Ph. D.")){
            coursefinal = coursespinner.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
