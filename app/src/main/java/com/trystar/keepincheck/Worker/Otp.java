package com.trystar.keepincheck.Worker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.trystar.keepincheck.R;

import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {

    EditText otp;
    ImageButton btn2;
    String phonenumber;
    FirebaseAuth mAuth;
    String otpid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        phonenumber = getIntent().getStringExtra("mobile");
        otp = findViewById(R.id.OTP);
        btn2 = findViewById(R.id.btn2);
        mAuth = FirebaseAuth.getInstance();

        initiateotp();

        try {
            btn2.setOnClickListener(view -> {
                if (otp.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else if (otp.getText().toString().length() != 6) {
                    Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }


            });

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    void initiateotp() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        otpid=s;
                                    }

                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        signInWithPhoneAuthCredential(phoneAuthCredential);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(Otp.this, BottomNav.class));


                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"Signin Code Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}