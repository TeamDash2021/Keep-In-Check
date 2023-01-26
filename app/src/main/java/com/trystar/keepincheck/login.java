package com.trystar.keepincheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trystar.keepincheck.OwnerPart.OwnerOtp;
import com.trystar.keepincheck.WorkerPart.Otp;

public class login extends AppCompatActivity {

    EditText number,name,inviteCode,companyName;
    Button btn;
    FirebaseAuth fAuth;
    String mNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        number = findViewById(R.id.number_login);
        btn = findViewById(R.id.btn_login);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    mNumber = number.getText().toString();

                    if(mNumber.length() == 10) {
                        checkOnOwner();
                        checkOnWorker();
                    }
                    else {
                        number.setError("Length of Number should be 10");
                        number.requestFocus();
                    }
            }

            private void checkOnWorker() {
                FirebaseFirestore db;
                db = FirebaseFirestore.getInstance();
                db.collection("Worker detail")
                        .whereEqualTo("Phone Number",mNumber)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.exists())
                                        {
                                            Intent intent = new Intent(login.this, Otp.class);
                                            intent.putExtra("mobile", "+91" + mNumber);
                                            startActivity(intent);
                                        }
                                    }
                                } else {

                                }
                            }
                        });
            }

            private void checkOnOwner() {
                FirebaseFirestore db;
                db = FirebaseFirestore.getInstance();
                db.collection("Owner detail")
                        .whereEqualTo("Phone Number",mNumber)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.exists())
                                        {
                                            Intent intent = new Intent(login.this, OwnerOtp.class);
                                            intent.putExtra("mobile", "+91" + mNumber);
                                            startActivity(intent);
                                        }
                                    }
                                } else {

                                }
                            }
                        });
            }
        });

    }
}