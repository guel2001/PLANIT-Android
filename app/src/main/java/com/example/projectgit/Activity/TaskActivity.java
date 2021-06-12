package com.example.projectgit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectgit.Data.Model;
import com.example.projectgit.R;
import com.example.projectgit.main.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.TimeZone;

public class TaskActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference reference;
    private DatabaseReference yesterday_reference;
    private DatabaseReference today_reference;
    private DatabaseReference tomorrow_reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String onlineUserID;

    private Boolean yesterday = false;
    private Boolean today = false;
    private Boolean tomorrow = false;

    private ProgressDialog loader;

    private String key = "";
    private String task;
    private String description;

    private String db_column_key = "";
    private ArrayList<Model> al_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_task);

        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PLANIT");
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        onlineUserID = user.getUid();

        Intent intent = getIntent();
        yesterday = intent.getBooleanExtra("check_yesterday", false);
        today = intent.getBooleanExtra("check_today", false);
        tomorrow = intent.getBooleanExtra("check_tomorrow", false);

        if (yesterday) {
            //Toast.makeText(getApplicationContext(), "이거 어제 " + yesterday, Toast.LENGTH_LONG).show();
            db_column_key = "yesterday_task";
            al_list = MainActivity.sta_hmap_model.get("yesterday_task");
            yesterday_reference = FirebaseDatabase.getInstance().getReference().child(onlineUserID).child("yesterday_task");
            reference = yesterday_reference;

        } else if (today) {
            //Toast.makeText(getApplicationContext(), "이거 오늘 " + today, Toast.LENGTH_LONG).show();
            db_column_key = "today_task";
            al_list = MainActivity.sta_hmap_model.get("today_task");
            today_reference = FirebaseDatabase.getInstance().getReference().child(onlineUserID).child("today_task");
            reference = today_reference;
        } else if (tomorrow) {
           // Toast.makeText(getApplicationContext(), "이거 내일 " + tomorrow, Toast.LENGTH_LONG).show();
            db_column_key = "tomorrow_task";
            al_list = MainActivity.sta_hmap_model.get("tomorrow_task");
            tomorrow_reference = FirebaseDatabase.getInstance().getReference().child(onlineUserID).child("tomorrow_task");
            reference = tomorrow_reference;
        }

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        createAdapter();

        loader = new ProgressDialog(this);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText task = myView.findViewById(R.id.task);
        final EditText description = myView.findViewById(R.id.description);
        Button save = myView.findViewById(R.id.saveBtn);
        Button cancel = myView.findViewById(R.id.CancelBtn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTask = task.getText().toString().trim();
                String mDescription = description.getText().toString().trim();
                String id = reference.push().getKey();

                Calendar yesterday_cal = Calendar.getInstance();
                Calendar today_cal = Calendar.getInstance();
                Calendar tomorrow_cal = Calendar.getInstance();
                SimpleDateFormat dformat = new SimpleDateFormat("yyyy.MM.dd.");
                dformat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                yesterday_cal.add(Calendar.DATE, -1);
                tomorrow_cal.add(Calendar.DATE, 1);
                String yesterday_date = dformat.format(yesterday_cal.getTime());
                String today_date = dformat.format(today_cal.getTime());
                String tomorrow_date = dformat.format(tomorrow_cal.getTime());

                //String date = DateFormat.getDateInstance().format(new Date());

                if (TextUtils.isEmpty(mTask)) {
                    task.setError("Task Required");
                    return;
                }

                if (TextUtils.isEmpty(mDescription)) {
                    description.setError("Description Required");
                    return;

                } else {
                    loader.setMessage("잠시만 기다려주세요.");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    Model model;
                    if(reference == yesterday_reference) {
                        model = new Model(mTask, mDescription, id, yesterday_date, false);
                    }
                    else if(reference == today_reference) {
                        model = new Model(mTask, mDescription, id, today_date, false);
                    }
                    else{
                        model = new Model(mTask, mDescription, id, tomorrow_date, false);
                    }

                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (al_list != null) {
                                    al_list.add(model);
                                }
                                MainActivity.sta_hmap_model.put("db_column_key", al_list);
                                //Toast.makeText(TaskActivity.this, "Task has been inserted successfully", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(TaskActivity.this, "Failed: " + error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });

                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void createAdapter() {
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(reference, Model.class).build();

        FirebaseRecyclerAdapter<Model, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final Model model) {
                holder.setCom(model.isCheck());
                holder.setTask(model.getTask());
                holder.setDesc(model.getDescription());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        task = model.getTask();
                        description = model.getDescription();
                        updateTask();
                    }
                });
                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        key = getRef(position).getKey();
                        task = model.getTask();
                        description = model.getDescription();
                        checkselect();
                        return true;
                    }});
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout, parent, false);
                return new MyViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTask(String task) {
            TextView taskTextView = mView.findViewById(R.id.taskTv);
            taskTextView.setText(task);
        }

        public void setDesc(String desc) {
            TextView descTextView = mView.findViewById(R.id.descriptionTv);
            descTextView.setText(desc);
        }

        public void setCom(boolean tf) {
            TextView comTextView = mView.findViewById(R.id.comTv);
            if(tf == true)
            {
                comTextView.setText("완료");
            }
            else
            {
                comTextView.setText("미완료");
            }
        }
    }

    private void checkselect(){

        String date = DateFormat.getDateInstance().format(new Date());

        Model model = new Model(task, description, key, date,true);
        Model fmodel = new Model(task, description, key, date,false);

        reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //여기에 수정됐을때 string어케 할지 정해야하고
                    for (int i = 0; i < MainActivity.sta_hmap_model.get(db_column_key).size(); i++) {
                        if (key.equals(MainActivity.sta_hmap_model.get(db_column_key).get(i).getId())) {
                            if(((MainActivity.sta_hmap_model.get(db_column_key).get(i).isCheck())==false))
                            {
                                MainActivity.sta_hmap_model.get(db_column_key).get(i).setCheck(true);
                            }
                            else
                            {
                                MainActivity.sta_hmap_model.get(db_column_key).get(i).setCheck(false);
                                reference.child(key).setValue(fmodel);
                            }
                        }
                    }

                    Toast.makeText(TaskActivity.this, "업데이트 완료", Toast.LENGTH_SHORT).show();
                } else {
                    String err = task.getException().toString();
                    Toast.makeText(TaskActivity.this, "업데이트 실패했습니다." + err, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_task, null);
        myDialog.setView(view);

        final AlertDialog dialog = myDialog.create();

        final EditText mTask = view.findViewById(R.id.mEditTextTask);
        final EditText mDescription = view.findViewById(R.id.mEditTextDescription);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        Button delButton = view.findViewById(R.id.btnDelete);
        Button updateButton = view.findViewById(R.id.btnUpdate);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = mTask.getText().toString().trim();
                description = mDescription.getText().toString().trim();

                Calendar yesterday_cal = Calendar.getInstance();
                Calendar today_cal = Calendar.getInstance();
                Calendar tomorrow_cal = Calendar.getInstance();
                SimpleDateFormat dformat = new SimpleDateFormat("yyyy.MM.dd.");
                dformat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                yesterday_cal.add(Calendar.DATE, -1);
                tomorrow_cal.add(Calendar.DATE, 1);
                String yesterday_date = dformat.format(yesterday_cal.getTime());
                String today_date = dformat.format(today_cal.getTime());
                String tomorrow_date = dformat.format(tomorrow_cal.getTime());

                Model model;
                if(reference == yesterday_reference) {
                    model = new Model(task, description, key, yesterday_date, false);
                }
                else if(reference == today_reference) {
                    model = new Model(task, description, key, today_date, false);
                }
                else{
                    model = new Model(task, description, key, tomorrow_date, false);
                }

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //여기에 수정됐을때 string어케 할지 정해야하고
                            for (int i = 0; i < MainActivity.sta_hmap_model.get(db_column_key).size(); i++) {
                                if (key.equals(MainActivity.sta_hmap_model.get(db_column_key).get(i).getId())) {
                                    MainActivity.sta_hmap_model.get(db_column_key).get(i).setTask(model.getTask());
                                    MainActivity.sta_hmap_model.get(db_column_key).get(i).setDescription(model.getDescription());
                                }
                            }
                            Toast.makeText(TaskActivity.this, "업데이트 완료", Toast.LENGTH_SHORT).show();
                        } else {
                            String err = task.getException().toString();
                            Toast.makeText(TaskActivity.this, "업데이트 실패 " + err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //여기에 삭제했을때 전역변수 수정해야됨

                            Log.d("지우", String.valueOf(MainActivity.sta_hmap_model.get(db_column_key).size()));
                            for (int i = 0; i < MainActivity.sta_hmap_model.get(db_column_key).size(); i++) {
                                if (key.equals(MainActivity.sta_hmap_model.get(db_column_key).get(i).getId())) {
                                    MainActivity.sta_hmap_model.get(db_column_key).remove(i);
                                }
                            }

                            Toast.makeText(TaskActivity.this, "할일을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            String err = task.getException().toString();
                            Toast.makeText(TaskActivity.this, "삭제 실패 " + err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}