package com.example.marcu.health;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import SignUp_SignIn.User;

public class LoginActivity extends AppCompatActivity {

    // for Sign Up
    MaterialEditText NewUser, NewPassword, NewEmail;

    // for Sign In
    MaterialEditText User, Password;

    // buttons
    Button btnSignUp, btnSignIn, btnRegister;

    FirebaseDatabase database;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        User = (MaterialEditText) findViewById(R.id.User);
        Password = (MaterialEditText) findViewById(R.id.Password);

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(User.getText().toString().toLowerCase(), Password.getText().toString());
            }
        });

    }


    private void signIn(final String username, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(username).exists()) {
                    if (!username.isEmpty()) {
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if (login.getPassword().equals(password))
                            Toast.makeText(LoginActivity.this, "Login correct!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(LoginActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showSignUpDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill in your information");

        LayoutInflater inflater = getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        NewUser = (MaterialEditText) sign_up_layout.findViewById(R.id.NewUserName);
        NewEmail = (MaterialEditText) sign_up_layout.findViewById(R.id.NewEmail);
        NewPassword = (MaterialEditText) sign_up_layout.findViewById(R.id.NewPassword);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

/*
        btnRegister = (Button) sign_up_layout.findViewById(R.id.btn_register);
        btnRegister(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final User user = new User(Objects.requireNonNull(NewUser.getText()).toString().toLowerCase(),
                                    Objects.requireNonNull(NewPassword.getText()).toString(),
                                    Objects.requireNonNull(NewEmail.getText()).toString().toLowerCase());

                            users.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(user.getUserName()).exists())
                                        Toast.makeText(LoginActivity.this, "User is already registered", Toast.LENGTH_SHORT).show();
                                    else {
                                        users.child(user.getUserName())
                                                .setValue(user);
                                        Toast.makeText(LoginActivity.this, "User registration successful", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            dialogInterface.dismiss();
                        }
                    });
        alertDialog.show();
    }
}
*/


        alertDialog.setPositiveButton("Register", new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                final User user = new User(Objects.requireNonNull(NewUser.getText()).toString().toLowerCase(),
                        Objects.requireNonNull(NewPassword.getText()).toString(),
                        Objects.requireNonNull(NewEmail.getText()).toString().toLowerCase());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(LoginActivity.this, "User is already registered", Toast.LENGTH_SHORT).show();
                        else {
                            users.child(user.getUserName())
                                    .setValue(user);
                            Toast.makeText(LoginActivity.this, "User registration successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }
}