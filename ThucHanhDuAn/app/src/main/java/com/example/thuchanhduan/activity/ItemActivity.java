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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuchanhduan.R;
import com.example.thuchanhduan.adapter.ItemAdapter;
import com.example.thuchanhduan.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    ItemAdapter adapter;
    ArrayList<Item> list;

    String title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView txttitle = findViewById(R.id.txtnameaacticỉy);
        Intent intent = getIntent();
        title = intent.getStringExtra("name");


        txttitle.setText(title);

//        addItemtoFirebase();
        showList();
        getListFromFirebase();
        FloatingActionButton floatadd = findViewById(R.id.floatAdd);

        floatadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();

            }
        });
    }
    private void addItem() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);
        EditText edtid = view.findViewById(R.id.edt_id);
        EditText edtName = view.findViewById(R.id.edt_name);
        RadioButton rdChecked = view.findViewById(R.id.rdChecked);
        builder.setView(view);
        builder.setNegativeButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer id = Integer.valueOf(edtid.getText().toString().trim());
                String name = edtName.getText().toString().trim();
                Boolean check = rdChecked.isChecked();
//                if (holder.rdcheck.isChecked()){
//                    myRef.child("Item").child(title).child(name).child("check").setValue(true);
//                }else {
//                    myRef.child("Item").child(title).child(name).child("check").setValue(false);
//                }
                if (check) {
                    Item item = new Item(name, id, true);
                    myRef.child(edtid.getText().toString()).setValue(item);
                } else {
                    Item item = new Item(name, id, false);
                    myRef.child(edtid.getText().toString()).setValue(item);
                }


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
        DatabaseReference myRef = database.getReference(title);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    list.add(item);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ItemActivity.this, "Get list faild", Toast.LENGTH_SHORT).show();
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
        adapter = new ItemAdapter(ItemActivity.this, list, title);

        recyclerView.setAdapter(adapter);


    }

    public void addItemtoFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(title);

        ArrayList<Item> list = new ArrayList<>();
        list.add(new Item("Buy milk", 1 , false));
        list.add(new Item("Call mom", 2, false));
        list.add(new Item("Check mail", 3, false));
        list.add(new Item("Walk the dog", 4, true));

//        myRef.child(String.valueOf(1)).setValue("Home");
//        myRef.child(String.valueOf(2)).setValue("Work");
//        myRef.child(String.valueOf(3)).setValue("Other");


        myRef.setValue(list, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(ItemActivity.this, "Theem lisst thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}