package com.example.thuchanhduan.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuchanhduan.R;
import com.example.thuchanhduan.activity.ItemActivity;
import com.example.thuchanhduan.model.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.VielHolder>{
    private Context context;
    private ArrayList<Task> list;

    public TasksAdapter(List<Task> list) {
    }

    public TasksAdapter(Context context, ArrayList<Task> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public VielHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_tasks,parent,false);
        return new VielHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VielHolder holder, int position) {

        holder.txtName.setText(list.get(position).getName());
        holder.txtID.setText(String.valueOf(list.get(position).getId()));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_addtask, null);
                EditText edtid = view.findViewById(R.id.edtid);
                EditText edtName = view.findViewById(R.id.edtName);

                edtid.setText(String.valueOf(list.get(position).getId()));
                edtName.setText(list.get(position).getName());

                builder.setView(view);
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String task = String.valueOf(list.get(position).getId());
                        DatabaseReference myRef = database.getReference("task/" + task);
                        myRef.removeValue();

                    }
                });
                builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String task = String.valueOf(list.get(position).getId());
                        DatabaseReference myRef = database.getReference("task/" + task);
                        Integer id = Integer.valueOf(edtid.getText().toString());
                        String name = edtName.getText().toString();
                        Task taskList = new Task(id,name);
                        myRef.setValue(taskList);
                    }
                });
                AlertDialog alertDialog = builder.create();
//                alertDialog.setCancelable(false);
                alertDialog.show();

            }
        });

        holder.btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("name", holder.txtName.getText().toString().trim());
                context.startActivity(intent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VielHolder extends RecyclerView.ViewHolder{
        TextView txtID,txtName;
        Button btnClick;

        LinearLayout linearLayout;
        public VielHolder(@NonNull View itemView) {

            super(itemView);
            txtID = itemView.findViewById(R.id.txtID);
            txtName = itemView.findViewById(R.id.txtName);
            btnClick = itemView.findViewById(R.id.btnClick);
            linearLayout = itemView.findViewById(R.id.linearlayout);
        }
    }
}
