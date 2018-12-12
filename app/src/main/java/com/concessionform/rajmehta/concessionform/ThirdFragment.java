package com.concessionform.rajmehta.concessionform;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThirdFragment extends Fragment {
    @Nullable
    TextView noticetext, fullnamedisp, blogtitledisp, blogcontentdisp;
    EditText fullname, blogtitle, blogcontent;
    String fullnamefinal, blogtitlefinal, blogcontentfinal, uid;
    boolean approved, comments;
    CheckBox checkBox;
    Button submitbutton;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("blogs");
    View view;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.third_fragment, container, false);

        noticetext = view.findViewById(R.id.noticetext);
        fullnamedisp = view.findViewById(R.id.fullnamedisp);
        blogtitledisp = view.findViewById(R.id.blogtitledisp);
        blogcontentdisp = view.findViewById(R.id.blogcontentdisp);

        fullname = view.findViewById(R.id.fullname);
        blogtitle = view.findViewById(R.id.blogtitle);
        blogcontent = view.findViewById(R.id.blogcontent);
        checkBox = view.findViewById(R.id.checkbox);
        submitbutton = view.findViewById(R.id.submitbutton);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = prefs.edit();

        blogcontent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                if(view.getId() == R.id.blogcontent){
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch(event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        comments = checkBox.isChecked();

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullnamefinal = fullname.getText().toString();
                blogtitlefinal = blogtitle.getText().toString();
                blogcontentfinal = blogcontent.getText().toString();

                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String userID = mDatabase.push().getKey();

                Blog blog = new Blog(approved,comments,fullnamefinal,blogcontentfinal,blogtitlefinal,uid);
                mDatabase.child(userID).setValue(blog);

                editor.putBoolean("approved",approved);
                editor.putBoolean("comments",comments);
                editor.putString("fullname", fullnamefinal);
                editor.putString("mainblog",blogcontentfinal);
                editor.putString("title",blogtitlefinal);
                editor.commit();
            }
        });
        return view;
    }
}