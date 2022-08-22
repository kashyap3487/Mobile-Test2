package com.example.test2;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SubCatAdaptor extends ArrayAdapter {
    private List<SubClass> categories_list;
    private Context context;
    private int layoutRes;
    private MainActivity2 act;
    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;


    public SubCatAdaptor(@NonNull Context context, int resource, @NonNull List<SubClass> categoryList, MainActivity2 mainActivity) {
        super(context, resource, categoryList);
        this.context = context;
        this.layoutRes = resource;
        this.categories_list = categoryList;
        this.act = mainActivity;
    }
    @Override
    public int getCount() {
        return categories_list.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        if (v == null) v = inflater.inflate(layoutRes, null);
        Button btn = v.findViewById(R.id.list_btn);
        TextView btn_del = v.findViewById(R.id.btn_delete);
        TextView btn_edit = v.findViewById(R.id.btn_edit);
        dbHelper = new DbHelper(act);

        btn.setText(categories_list.get(position).getName());

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DbHelper(act);
                int cat_id = categories_list.get(position).getId();
                dbHelper.deleteSubCat(cat_id);
                categories_list.remove(position);
                notifyDataSetChanged();
            }
        });


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                LayoutInflater inflater = LayoutInflater.from(act);
                View view = inflater.inflate(R.layout.edit_sub,null);
                builder.setView(view);
                Button btn = view.findViewById(R.id.btn_subcat_add_note);

                AlertDialog dialog = builder.create();
                int old_subcat_id = categories_list.get(position).getId();

                dialog.show();

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText note = view.findViewById(R.id.dialogbox_sub_note_edt);
                        if(note.getText().toString().trim().isEmpty()){
                            Toast.makeText(act, "Please Enter Note", Toast.LENGTH_SHORT).show();
                        }
                        else{
//                            Toast.makeText(act, "id : "+old_subcat_id+ "  name : " +  note.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                            dbHelper.updateSubCat( old_subcat_id , note.getText().toString().trim() );
                            dialog.dismiss();
                            notifyDataSetChanged();
                            act.loadact2();
                        }


                    }
                });


            }
        });


        return v;
    }


}
