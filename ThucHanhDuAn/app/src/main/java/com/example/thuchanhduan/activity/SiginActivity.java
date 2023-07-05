package com.example.thuchanhduan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thuchanhduan.R;
import com.example.thuchanhduan.model.Task;
import com.example.thuchanhduan.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SiginActivity extends AppCompatActivity {

    EditText edtUsername,edtPassword;
    Button btnSigin,btnToLogin;

    ArrayList<User> list = new ArrayList<>();

    boolean check = true;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);

        anhxa();

        mAuth = FirebaseAuth.getInstance();
        dangki();

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SiginActivity.this,LoginActivity.class));
                addUser();
            }
        });
    }

    private void dangki() {
        btnSigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
    }

    public void anhxa(){
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSigin = findViewById(R.id.btnSigin);
        btnToLogin = findViewById(R.id.btnToLogin);
    }

    private void addUser() {

        String user = edtUsername.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        if (user.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Bạn cần nhập đầy đủ", Toast.LENGTH_SHORT).show();
        }else {
            mAuth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(user);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (dataSnapshot.getKey().equals(user)){
                            check = false;
                        }
                    }
                    if (check){
                        myRef.child("pass").setValue(pass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private void getListFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SiginActivity.this, "Get list faild", Toast.LENGTH_SHORT).show();
            }
        });
    }
}