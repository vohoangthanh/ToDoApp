package com.example.thuchanhduan.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thuchanhduan.R;
import com.example.thuchanhduan.adapter.TasksAdapter;
import com.example.thuchanhduan.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase
        ;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TasksAdapter adapter;

    ArrayList<Task> list;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton floatadd = findViewById(R.id.floatAdd);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        floatadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtask();
            }
        });

        addlisttofirebase();

        showList();
        getListFromFirebase();
    }

    private void addtask() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("task");

        AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_addtask, null);
        EditText edtid = view.findViewById(R.id.edt_id);
        EditText edtName = view.findViewById(R.id.edt_name);
        builder.setView(view);
        builder.setNegativeButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer id = Integer.valueOf(edtid.getText().toString().trim());
                String name = edtName.getText().toString().trim();

                Task taskList = new Task(id, name);

                myRef.child(edtid.getText().toString()).setValue(taskList);
            }
        });
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void getListFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("task");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String a = "";
                    Task taskList = dataSnapshot.getValue(Task.class);
                    list.add(taskList);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TaskActivity.this, "Get list faild", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showList() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        list = new ArrayList<>();
        adapter = new TasksAdapter(TaskActivity.this, list);

//        adapter = new ListAdapter(list);
        recyclerView.setAdapter(adapter);
        // Toast.makeText(this, ""+list.size(), Toast.LENGTH_SHORT).show();


    }

    public void addlisttofirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("task");

        ArrayList<Task> list = new ArrayList<>();
        list.add(new Task(1, "Home"));
        list.add(new Task(2, "Work"));
        list.add(new Task(3, "Other"));
//        myRef.child(String.valueOf(1)).setValue("Home");
//        myRef.child(String.valueOf(2)).setValue("Work");
//        myRef.child(String.valueOf(3)).setValue("Other");


        myRef.setValue(list, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(TaskActivity.this, "Theem lisst thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}