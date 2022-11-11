package com.trystar.keepincheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.jar.Attributes;


public class Register extends AppCompatActivity {

    //int ans=0;
    //static int ans = 0;
    EditText number,name,inviteCode;
    Button btn;
    FirebaseAuth fAuth;
    String code, mNumber ,Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        number=findViewById(R.id.number);
        name = findViewById(R.id.name);
        btn =  findViewById(R.id.btn);
        inviteCode = findViewById(R.id.invite);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = inviteCode.getText().toString();
                Name = name.getText().toString();
                mNumber = number.getText().toString();
                if(Name.length()<1){
                    name.setError("Name can not be NULL");
                    name.requestFocus();
                }
                if (mNumber.length() != 10)
                {
                    number.setError("Length of Number should be 10");
                    number.requestFocus();
                }
                if(code.length()==6)
                {
                    int c = checkExist(code);
                    //creating Delay
                   /* try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        Toast.makeText(Register.this,e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }*/
                    /*if(c==1)
                    {
                        if(Name.length()>1 && mNumber.length()==10)
                        {
                           // saveOnFirestore();
                            Intent intent = new Intent(Register.this, Otp.class);
                            intent.putExtra("mobile", "+91" + number.getText().toString());
                            startActivity(intent);
                            ans=0;
                        }
                    }
                    else {
                        Toast.makeText(Register.this,"Incorrect invite code", Toast.LENGTH_SHORT).show();
                    }*/
                }
                else{
                    inviteCode.setError("Length should be 6");
                    inviteCode.requestFocus();
                }

            }

            private void saveOnFirestore() {

            }

            private int checkExist(String companyCode) {
                try {
                    code = inviteCode.getText().toString();
                    Name = name.getText().toString();
                    mNumber = number.getText().toString();
                    db.collection("Owner detail")
                            .whereEqualTo("Invite Code",companyCode)//this is for checking the existence of company code
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.exists()) {

                                                if (Name.length() > 1 && mNumber.length() == 10) {
                                                    // saveOnFirestore();
                                                    Intent intent = new Intent(Register.this, Otp.class);
                                                    intent.putExtra("mobile", "+91" + number.getText().toString());
                                                    startActivity(intent);
                                                    Toast.makeText(Register.this, document.getString("Invite Code"), Toast.LENGTH_SHORT).show();
                                                }

                                                //ans = 1;
                                                else {
                                                    Toast.makeText(Register.this, "Incorrect invite code", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(Register.this, "error: code doesnt exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(Register.this,"error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });}
                catch (Exception e)
                {
                    Toast.makeText(Register.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return 0;
            }
        });
    }
}