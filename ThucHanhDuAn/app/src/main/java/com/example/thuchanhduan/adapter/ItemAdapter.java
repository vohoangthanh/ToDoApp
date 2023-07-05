package com.example.thuchanhduan.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuchanhduan.R;
import com.example.thuchanhduan.model.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Item> list;

    String tilte;


    public ItemAdapter(Context context, ArrayList<Item> list, String tilte) {
        this.context = context;
        this.list = list;
        this.tilte = tilte;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtName.setText(list.get(position).getName());
        holder.txtid.setText(String.valueOf(list.get(position).getId()));
        holder.rdcheck.setChecked(list.get(position).isCheck());
        holder.rdcheck.setEnabled(false);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_add_item, null);
                EditText edtid = view.findViewById(R.id.edt_id);
                EditText edtName = view.findViewById(R.id.edt_name);
                TextView txtTitle = view.findViewById(R.id.txtTitle);
                RadioButton rdcheck = view.findViewById(R.id.rdChecked);
                txtTitle.setText("Xóa Sửa");
                edtid.setText(String.valueOf(list.get(position).getId()));
                edtName.setText(list.get(position).getName());
                rdcheck.setChecked(false);

                rdcheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rdcheck.setChecked(true);
                    }
                });

                builder.setView(view);
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String task = String.valueOf(list.get(position).getId());
                        DatabaseReference myRef = database.getReference(tilte+"/" + task);
                        myRef.removeValue();

                    }
                });
                builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String task = String.valueOf(list.get(position).getId());
                        DatabaseReference myRef = database.getReference(tilte+"/" + task);
                        Integer id = Integer.valueOf(edtid.getText().toString());
                        String name = edtName.getText().toString();
                        boolean check = rdcheck.isChecked();
                        Item item = new Item(name, id, check);
                        myRef.setValue(item);
                    }
                });
                AlertDialog alertDialog = builder.create();
//                alertDialog.setCancelable(false);
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtid, txttitle;
        RadioButton rdcheck;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            rdcheck = itemView.findViewById(R.id.rdCheck);
            txtid = itemView.findViewById(R.id.txtID);
            linearLayout = itemView.findViewById(R.id.linearlayout);
            txttitle = itemView.findViewById(R.id.txtnameaacticỉy);
        }
    }
}
