package com.info121.imessenger.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.info121.imessenger.BaseActivity;
import com.info121.imessenger.R;
import com.info121.imessenger.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.signin)
    Button mSignin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");
//
//
//
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.e(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.e(TAG, "Failed to read value.", error.toException());
//            }
//        });
    }

    @OnClick({R.id.login, R.id.signin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:

                break;

            case R.id.signin:
                addUser();
                break;
        }
    }


    private void addUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("USERS");

//        User user = new User("3", "Htun", "2342342234", "ccc");
//        userRef.push().setValue(user);
//
//         user = new User("4", "Khaing", "74667473", "ddd");
//
//        userRef.push().setValue(user);
//
//         user = new User("5", "Thandar", "34323424", "eee");
//
//        userRef.push().setValue(user);
    }
}