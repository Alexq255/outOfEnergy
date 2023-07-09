package com.example.outofenergy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class menu extends AppCompatActivity {

    private DatabaseReference mBase;
    private DatabaseReference mbase_active_task_reference;
    private DatabaseReference mBase_view;
    private Uri uploadUri, uploadUri2, uploadUri3;
    private StorageReference mStorageRef;
    private String GROUPKEY;
    private String TASKKEY;
    private String SpinnerKey;
    private int mah_add_of_category=0;
    private int clicks = 0;
    private int clicker = 1;
    private MediaPlayer mPlayer;
    private int  star=0;
    private int i=1;
    private int sound_code =0;
    private ConstraintLayout menu_bank_layout;
    private Button menu_all_active_tasks,all_tasks_btn_click,completed_task_list;


    private Button menu_admin_add_task,menu_admin_saveall,menu_start_task_user,menu_list_user_task,menu_end_task,menu_list_task_end;
    private FirebaseFirestore fstore;
    private FirebaseAuth mAuth;
    private TextView menu_balance_view, menu_energy_view,menu_task_full_nazvanie,menu_task_full_time,menu_task_full_reward,menu_task_full_instruction,menu_main_header_task_aact_text,bank_header;
    private LinearLayout menu_admin_add_layout;
    private ImageView menu_admin_imageView,menu_task_full_image;
    private EditText menu_admin_ed_nazvanie, menu_admin_ed_instruction, menu_admin_ed_time, menu_admin_ed_reward;
    private Spinner menu_task_cat_spinner;
    private ListView menu_task_list;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private List<task_Add_Class> listTemp;

    private String oldRole_isAdmin;
    private String str_isUser;
    private String oldRole_notValid;
    private String idFordel_active_task;
    private int ruble_add_when_selected_category=0;
    private String remove_from_task_id;
    private Button bank_bonus_button;

    public menu() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initForBank();
        mAuth = FirebaseAuth.getInstance();//for FirebaseAuth working
        fstore = FirebaseFirestore.getInstance();//fstore storage the balance data()
        menu_balance_view = findViewById(R.id.menu_balance_view);
        menu_energy_view = findViewById(R.id.menu_energy_view);
        menu_task_cat_spinner = findViewById(R.id.menu_task_cat_spinner);//spinner cats
        background_music_chooser();//Randomizing lobby music method
        get_balance();//gather balance for current Authorized account
        menu_bank_layout = findViewById(R.id.menu_bank_layout);// лайяут для банка и трат

        menu_all_active_tasks = findViewById(R.id.menu_all_active_tasks);
        all_tasks_btn_click = findViewById(R.id.all_tasks_btn_click);
        menu_task_full_nazvanie = findViewById(R.id.menu_task_full_nazvanie);
        menu_task_full_instruction = findViewById(R.id.menu_task_full_instruction);
        menu_task_full_time= findViewById(R.id.menu_task_full_time);
        menu_task_full_reward= findViewById(R.id.menu_task_full_reward);
        menu_task_full_image = findViewById(R.id.menu_task_full_image);
        menu_list_user_task = findViewById(R.id.menu_list_user_task);
        menu_start_task_user = findViewById(R.id.menu_start_task_user);
        menu_main_header_task_aact_text = findViewById(R.id.menu_main_header_task_aact_text);
        completed_task_list =findViewById(R.id.completed_task_list);

        menu_list_user_task.setVisibility(View.GONE);
        menu_start_task_user.setVisibility(View.GONE);
        menu_task_full_nazvanie.setVisibility(View.GONE);
        menu_task_full_instruction.setVisibility(View.GONE);
        menu_task_full_time.setVisibility(View.GONE);
        menu_task_full_reward.setVisibility(View.GONE);
        menu_task_full_image.setVisibility(View.GONE);

        menu_end_task = findViewById(R.id.menu_end_task);
        menu_list_task_end = findViewById(R.id.menu_list_task_end);
        init_menu_disvisible();
        GROUPKEY = "Tasks";//LateInit string for add_data_from_db
        TASKKEY =menu_task_cat_spinner.getSelectedItem().toString();//gathering cat from spinner
        menu_task_cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TASKKEY =menu_task_cat_spinner.getSelectedItem().toString();
                adapter.clear();
                listTemp.clear();
                list_of_task_method();
                mBase_view = FirebaseDatabase.getInstance().getReference(TASKKEY);//listview filter_by spinner selected cat
                  get_tasks_from_db();
                Toast.makeText(menu.this,"Выбрано: "+ TASKKEY, Toast.LENGTH_SHORT).show();
                if (TASKKEY.contains("Ежедневные задачи")){
                    mah_add_of_category = 200;
                    ruble_add_when_selected_category = 20;
                }else{
                    if (TASKKEY.contains("Достижения")){
                        mah_add_of_category = 10000;
                        ruble_add_when_selected_category=500;
                    }else{
                        if (TASKKEY.contains("Спецпредложения")){
                          mah_add_of_category = 10000;
                            ruble_add_when_selected_category = 1000;
                        }else{
                            if (TASKKEY.contains("Челленджи")){
                                mah_add_of_category = 1200;
                                ruble_add_when_selected_category = 1000;

                            }
                        }
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);//for add data reference
        mBase_view = FirebaseDatabase.getInstance().getReference(TASKKEY);//for view data
        admin_funcs();//Функции админа
        ///\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
        String admCheck=null;
        Intent intent = getIntent();
        if (intent!=null){
            admCheck=intent.getStringExtra("UserData");
            if (admCheck!=null){
             menu_admin_add_task.setVisibility(View.VISIBLE);
            }else{//admin function: add_data_from_db
                menu_admin_add_task.setVisibility(View.GONE);
            }
        }
        menu_admin_saveall.setEnabled(false);
        menu_admin_saveall.setText("Картинка не загружена");
        get_tasks_from_db();
    }



    private void init_menu_disvisible() {
        menu_list_task_end.setVisibility(View.GONE);
        menu_end_task.setVisibility(View.GONE);
    }
    private void get_tasks_from_db() {//init for get data from db & get data
        menu_task_list = findViewById(R.id.menu_task_list);
        listdata = new ArrayList<>();
        listTemp = new ArrayList<task_Add_Class>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listdata);
        menu_task_list.setAdapter(adapter);
        get_tasks_FromDB2();
        init_full_info_from_selected_task();
    }//init for get data from db & get data
    private void get_tasks_FromDB2(){//dataGatherer
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listdata.size()<0)listdata.clear();
                if (listTemp.size()<0)listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    task_Add_Class Cart = ds.getValue(task_Add_Class.class);
                    assert Cart != null;
                    listdata.add(Cart.nazvanie);
                    listTemp.add(Cart);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mBase_view.addValueEventListener(vListener);
    }//gather data tasks user
    private void init_full_info_from_selected_task() {//info of task
      menu_task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              menu_task_full_nazvanie.setVisibility(View.VISIBLE);
              menu_task_full_instruction.setVisibility(View.VISIBLE);
              menu_task_full_time.setVisibility(View.VISIBLE);
              menu_task_full_reward.setVisibility(View.VISIBLE);
              menu_task_full_image.setVisibility(View.VISIBLE);
              menu_task_list.setVisibility(View.GONE);
              menu_list_user_task.setVisibility(View.VISIBLE);
              menu_start_task_user.setVisibility(View.VISIBLE);

              task_Add_Class tovarAdd = listTemp.get(i);
              menu_task_full_nazvanie.setText(tovarAdd.nazvanie.toString());
              menu_task_full_instruction.setText(tovarAdd.instruction.toString());
              menu_task_full_time.setText(tovarAdd.time.toString());
              menu_task_full_reward.setText(tovarAdd.reward.toString());
              remove_from_task_id= tovarAdd.id.toString();
              Picasso.get().load(tovarAdd.uploadUri).into(menu_task_full_image);
              uploadUri = Uri.parse(tovarAdd.uploadUri);
          }
      });
    }
    private void background_music_chooser() {
        if (sound_code == 1){
            mPlayer = MediaPlayer.create(this, R.raw.quest_completed);
            mPlayer.start();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sound_code = 0;
                    background_music_chooser();
                }
            },2000);
        }else{
            int a = (int) (Math.random()*5);
            if (a==1){
                mPlayer = MediaPlayer.create(this, R.raw.shop_background_music1);
            }else{
                if (a==2){
                    mPlayer = MediaPlayer.create(this, R.raw.shop_background2);
                }else{
                    if (a==3){
                        mPlayer = MediaPlayer.create(this, R.raw.lobby);
                    }else{
                        if (a==4){
                            mPlayer = MediaPlayer.create(this,R.raw.lobby4);
                        }else{
                            mPlayer = MediaPlayer.create(this,R.raw.lobby);
                        }
                    }
                }
            }
            mPlayer.start();
        }
    }//music
    private void admin_funcs() {//Funcs of Admin
        menu_admin_add_layout = findViewById(R.id.menu_admin_add_layout); // main layout
        menu_admin_add_layout.setVisibility(View.GONE);
        menu_admin_add_task = findViewById(R.id.menu_admin_add_task);
        menu_admin_imageView = findViewById(R.id.menu_admin_imageView);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
        menu_admin_saveall= findViewById(R.id.menu_admin_saveall);
        menu_admin_ed_nazvanie = findViewById(R.id.menu_admin_ed_nazvanie);
        menu_admin_ed_instruction = findViewById(R.id.menu_admin_ed_instruction);
        menu_admin_ed_time = findViewById(R.id.menu_admin_ed_time);
        menu_admin_ed_reward = findViewById(R.id.menu_admin_ed_reward);
    }//admin func init
    public void get_balance() {
        DocumentReference df = fstore.collection("Users").document(mAuth.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {      //Получаем баланс с бдшки
                Log.d("TAG", "onSuccess" + documentSnapshot.getData());
                menu_balance_view.setText(documentSnapshot.getString("account_balance"));
                menu_energy_view.setText(documentSnapshot.getString("mah_balance") + " mah");
            }
        });
    }//get balance
    public void AddImg(View view) {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }//addimg intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) ;
        {
            if (resultCode == RESULT_OK) {
                Log.d("Mylog", "Image URI : " + data.getData());
                menu_admin_imageView.setImageURI(data.getData());
                uploadImage();
            }
        }
    }//upload img
    public void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) menu_admin_imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + "MyImg");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (uploadUri == null) {
                    uploadUri = task.getResult();
                    Toast.makeText(menu.this, "Загружено", Toast.LENGTH_SHORT).show();
                    menu_admin_saveall.setEnabled(true);
                    menu_admin_saveall.setText("Cохранить все");
                }
            }
        });
    }
    public void add_task(View view) {// Кнопка с функцией отжатия..добавить
        clicks += 1;
        menu_task_list.setVisibility(View.GONE);
        if (clicks >= 1) {
            menu_admin_add_layout.setVisibility(View.VISIBLE);
            menu_admin_add_task.setText("Hide");
            if (clicks >= 2) {
                menu_admin_add_layout.setVisibility(View.GONE);
                menu_task_list.setVisibility(View.VISIBLE);
                clicks = 0;
                menu_admin_add_task.setText("Add");
            }
        }
    }
    public void menu_admin_save(View view) {
        if (menu_admin_ed_nazvanie.getText().toString().isEmpty()){
            Toast.makeText(menu.this, "Поле название пустое", Toast.LENGTH_SHORT).show();
        }else{
            if (menu_admin_ed_instruction.getText().toString().isEmpty()){
                Toast.makeText(menu.this, "Поле инструкция пустое", Toast.LENGTH_SHORT).show();
            }else{
                if (menu_admin_ed_time.getText().toString().isEmpty()){
                    Toast.makeText(menu.this, "Поле время пустое", Toast.LENGTH_SHORT).show();
                }else{

                }if (menu_admin_ed_reward.getText().toString().isEmpty()){
                    Toast.makeText(menu.this, "Поле награда пустое", Toast.LENGTH_SHORT).show();
                }else{
                    saveData();
                }
            }
        }
}//savebtnadmin
    private void saveData()//savedataadmin
    {
        GROUPKEY =menu_task_cat_spinner.getSelectedItem().toString();
        mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
        String id = mBase.push().getKey();
        String nazvanie = menu_admin_ed_nazvanie.getText().toString();
        String instruction = menu_admin_ed_instruction.getText().toString();
        String time = menu_admin_ed_time.getText().toString();
        String reward = menu_admin_ed_reward.getText().toString();
        String category_of_task=menu_task_cat_spinner.getSelectedItem().toString();
        ///\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
        task_Add_Class newTaskAdd = new task_Add_Class(id,nazvanie,category_of_task,instruction,time,reward,uploadUri.toString());
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(instruction)&&!TextUtils.isEmpty(time)
                ){
            if (id != null)mBase.child(id).setValue(newTaskAdd);
            Toast.makeText(menu.this,"Данные отправлены на сервер!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(menu.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }
    public void list_of_task(View view) {

        list_of_task_method();

    }

    private void list_of_task_method() {
        menu_task_full_nazvanie.setVisibility(View.GONE);
        menu_task_full_instruction.setVisibility(View.GONE);
        menu_task_full_time.setVisibility(View.GONE);
        menu_task_full_reward.setVisibility(View.GONE);
        menu_task_full_image.setVisibility(View.GONE);
        menu_task_list.setVisibility(View.VISIBLE);
        menu_list_user_task.setVisibility(View.GONE);
        menu_start_task_user.setVisibility(View.GONE);

        menu_all_active_tasks.setEnabled(true);
        completed_task_list.setEnabled(true);
        all_tasks_btn_click.setEnabled(true);
        menu_admin_add_task.setEnabled(true);
        menu_task_list.setVisibility(View.VISIBLE);
        menu_list_task_end.setVisibility(View.GONE);
        menu_end_task.setVisibility(View.GONE);
    }

    public void menu_task_add_to_active_dialog_start(View view) {//Add task to active
        AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
        builder.setTitle("Добавление задачи")
                .setCancelable(false)
                .setIcon(R.drawable.plusadd)
                .setMessage("Добавить эту задачу в активные?, действие нельзя отменить, одновременно можно выполнять не более 5 задач!")
                .setPositiveButton("Начать выполнение",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                GROUPKEY = "ActiveTasks"+mAuth.getUid();
                                 mbase_active_task_reference= FirebaseDatabase.getInstance().getReference(GROUPKEY);

                                 if (TASKKEY.contains("Ежедневные задачи")){
                                     saveData_toActive();//method
                                 }else{
                                     saveData_toActive_andRemove();//method
                                 }

                                list_of_task_method();
                                //saveData_toActive_and_del_from_quests();
                            }
                        })
                .setNegativeButton("Закрыть диалог",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void saveData_toActive()//save task to ActiveQuests from current Users
    {
        String id = mbase_active_task_reference.push().getKey();
        String nazvanie = menu_task_full_nazvanie.getText().toString();
        String instruction = menu_task_full_instruction.getText().toString();
        String time = menu_task_full_time.getText().toString();
        String reward = menu_task_full_reward.getText().toString();
        String category_of_task=menu_task_cat_spinner.getSelectedItem().toString();

        ///\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
        task_Add_Class newTaskAdd = new task_Add_Class(id,nazvanie,category_of_task,instruction,time,reward,uploadUri.toString());
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(instruction)&&!TextUtils.isEmpty(time)
        ){
            if (id != null)mbase_active_task_reference.child(id).setValue(newTaskAdd);

            Toast.makeText(menu.this,"Данные отправлены на сервер!",Toast.LENGTH_SHORT).show();
            
        }else{
            Toast.makeText(menu.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }
    private void saveData_toActive_andRemove()//save task to ActiveQuests from current Users and remove from list
    {
        String id = mbase_active_task_reference.push().getKey();
        String nazvanie = menu_task_full_nazvanie.getText().toString();
        String instruction = menu_task_full_instruction.getText().toString();
        String time = menu_task_full_time.getText().toString();
        String reward = menu_task_full_reward.getText().toString();
        String category_of_task=menu_task_cat_spinner.getSelectedItem().toString();

        ///\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
        task_Add_Class newTaskAdd = new task_Add_Class(id,nazvanie,category_of_task,instruction,time,reward,uploadUri.toString());
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(instruction)&&!TextUtils.isEmpty(time)
        ){
            if (id != null)mbase_active_task_reference.child(id).setValue(newTaskAdd);

            Toast.makeText(menu.this,"Данные отправлены на сервер!",Toast.LENGTH_SHORT).show();
            remove_data_from_TASKLIST();
        }else{
            Toast.makeText(menu.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }

    public void all_active_tasks(View view) {//all_active_tasks_filter_by
        all_active_task_method();
    }
    private void remove_data_from_TASKLIST()//delete dataFROM_ACTIVE
    {
        GROUPKEY = menu_task_cat_spinner.getSelectedItem().toString();
        mbase_active_task_reference = FirebaseDatabase.getInstance().getReference(GROUPKEY);
        String id = remove_from_task_id;
        mbase_active_task_reference.child(id).removeValue();
    }

    private void all_active_task_method() {
        menu_task_list.setVisibility(View.VISIBLE);
        menu_main_header_task_aact_text.setText("Активные задачи");
        TASKKEY = "ActiveTasks"+mAuth.getUid();
        adapter.clear();
        listTemp.clear();
        mBase_view = FirebaseDatabase.getInstance().getReference(TASKKEY);//listview filter_by spinner selected cat
         get_tasks_from_db();
        Toast.makeText(menu.this,"Выбрано: "+ TASKKEY, Toast.LENGTH_SHORT).show();
        menu_task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                menu_task_full_nazvanie.setVisibility(View.VISIBLE);
                menu_task_full_instruction.setVisibility(View.VISIBLE);
                menu_task_full_time.setVisibility(View.VISIBLE);
                menu_task_full_reward.setVisibility(View.VISIBLE);
                menu_task_full_image.setVisibility(View.VISIBLE);
                menu_task_list.setVisibility(View.GONE);
                //menu_list_user_task.setVisibility(View.VISIBLE);
                //menu_start_task_user.setVisibility(View.VISIBLE);
                menu_list_task_end.setVisibility(View.VISIBLE);
                menu_end_task.setVisibility(View.VISIBLE);
                menu_all_active_tasks.setEnabled(false);
                completed_task_list.setEnabled(false);
                all_tasks_btn_click.setEnabled(false);
                menu_admin_add_task.setEnabled(false);
                menu_task_list.setVisibility(View.GONE);
                task_Add_Class tovarAdd = listTemp.get(i);
                idFordel_active_task = tovarAdd.id.toString();
                menu_task_full_nazvanie.setText(tovarAdd.nazvanie.toString());
                menu_task_full_instruction.setText(tovarAdd.instruction.toString());
                menu_task_full_time.setText(tovarAdd.time.toString());
                menu_task_full_reward.setText(tovarAdd.reward.toString());
                Picasso.get().load(tovarAdd.uploadUri).into(menu_task_full_image);
                uploadUri = Uri.parse(tovarAdd.uploadUri);
            }
        });
    }

    private void remove_data_from_active_quest()//remove
    {
        String id = mbase_active_task_reference.push().getKey();
        String nazvanie = menu_task_full_nazvanie.getText().toString();
        String instruction = menu_task_full_instruction.getText().toString();
        String time = menu_task_full_time.getText().toString();
        String reward = menu_task_full_reward.getText().toString();
        String category_of_task=menu_task_cat_spinner.getSelectedItem().toString();



        ///\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
        task_Add_Class newTaskAdd = new task_Add_Class(id,nazvanie,category_of_task,instruction,time,reward,uploadUri.toString());
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(instruction)&&!TextUtils.isEmpty(time)
        ){
            if (id != null)mbase_active_task_reference.child(id).setValue(newTaskAdd);
            Toast.makeText(menu.this,"Данные отправлены на сервер!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(menu.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }
    public void menu_task_add_to_active_dialog_start_to_end_of_quest(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
        builder.setTitle("Cдать задачу?")
                .setCancelable(false)
                .setIcon(R.drawable.plusadd)
                .setMessage("Если задача выполнена нажав выполнено вы сможете получить вашу награду")
                .setPositiveButton("Выполнено",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                DocumentReference df = fstore.collection("Users").document(mAuth.getUid());
                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {      //gathering balance from_db
                                        Log.d("TAG", "onSuccess" + documentSnapshot.getData());
                                        int ac_bal = Integer.parseInt((documentSnapshot.getString("account_balance")));
                                        ac_bal += Integer.parseInt(menu_task_full_reward.getText().toString());
                                        int ac_bal_mah = Integer.parseInt((documentSnapshot.getString("mah_balance")));
                                        ac_bal_mah+=mah_add_of_category;
                                         int ruble_balance = Integer.parseInt((documentSnapshot.getString("ruble_balance")));
                                           ruble_balance+=ruble_add_when_selected_category;
                                          String star_bal = (documentSnapshot.getString("stars_balance"));
                                        String ac_bal_push = String.valueOf(ac_bal);
                                        String mah_balance_push = String.valueOf(ac_bal_mah);
                                        if (documentSnapshot.getString("isAdmin") !=null){
                                            oldRole_isAdmin =(documentSnapshot.getString("isAdmin"));
                                        }
                                        if (documentSnapshot.getString("isUser") !=null){
                                            str_isUser = (documentSnapshot.getString("isUser"));
                                        }
                                        if (documentSnapshot.getString("NotValid") !=null){
                                            oldRole_notValid = (documentSnapshot.getString("NotValid"));
                                        }
                                        list_of_task_method();
                                        FirebaseUser users = mAuth.getCurrentUser();
                                        DocumentReference df = fstore.collection("Users").document(users.getUid());
                                        Map<String,Object> userinfo = new HashMap<>();
                                        if (documentSnapshot.getString("isAdmin") !=null){
                                            userinfo.put("isAdmin",oldRole_isAdmin);
                                        }
                                        if (documentSnapshot.getString("isUser") !=null){
                                            userinfo.put("isAdmin",str_isUser);
                                        }
                                        if (documentSnapshot.getString("NotValid") !=null){
                                            userinfo.put("isAdmin",oldRole_notValid);
                                        }
                                        userinfo.put("account_balance",ac_bal_push);
                                        userinfo.put("mah_balance",mah_balance_push);
                                        star = Integer.parseInt(star_bal);
                                        star++;
                                        userinfo.put("stars_balance",Integer.toString(star));
                                        userinfo.put("ruble_balance",Integer.toString(ruble_balance));
                                        df.set(userinfo);
                                        get_balance();
                                        mPlayer.stop();
                                        sound_code = 1;
                                        background_music_chooser();
                                        saveData_toComplete();
                                        all_active_task_method();
                                    }
                                });
                            }
                        })
                .setNegativeButton("Закрыть диалог",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void all_task_filter(View view) {
        get_tasks_from_db();
        init_full_info_from_selected_task();
        menu_main_header_task_aact_text.setText("Выбранный");

    }
    private void saveData_toComplete()//savedatatofisinshed
    {
        TASKKEY ="FinishedTasks"+mAuth.getUid();
        mBase_view = FirebaseDatabase.getInstance().getReference(TASKKEY);
        String id = idFordel_active_task;
        String nazvanie = menu_task_full_nazvanie.getText().toString();
        String instruction = menu_task_full_instruction.getText().toString();
        String time = menu_task_full_time.getText().toString();
        String reward = menu_task_full_reward.getText().toString();
        String category_of_task=menu_task_cat_spinner.getSelectedItem().toString();

        ///\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
        task_Add_Class newTaskAdd = new task_Add_Class(id,nazvanie,category_of_task,instruction,time,reward,uploadUri.toString());
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(instruction)&&!TextUtils.isEmpty(time)
        ){
            if (id != null)mBase_view.child(id).setValue(newTaskAdd);
            remove_data_from_activeTASK();
            Toast.makeText(menu.this,"Данные отправлены на сервер!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(menu.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }
    private void remove_data_from_activeTASK()//delete dataFROM_ACTIVE
    {
        TASKKEY = "ActiveTasks"+mAuth.getUid();
        mBase_view = FirebaseDatabase.getInstance().getReference(TASKKEY);
        String id = idFordel_active_task;
        mBase_view.child(id).removeValue();
    }

    public void completed_task_list(View view) {
        TASKKEY = "FinishedTasks"+mAuth.getUid();
        menu_task_list.setVisibility(View.VISIBLE);
        menu_main_header_task_aact_text.setText("Завершено");
        adapter.clear();
        listTemp.clear();
        mBase_view = FirebaseDatabase.getInstance().getReference(TASKKEY);//listview filter_by spinner selected cat
        get_tasks_from_db();
        menu_task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
                builder.setTitle("Выполненная задача")
                        .setCancelable(false)
                        .setIcon(R.drawable.plusadd)
                        .setMessage("Вы выполнили эту задачу!, находящиеся в списке позиция не более, чем история задач, которые вы когда-либо выполняли!, награда за это задание получена")
                        .setPositiveButton("Хорошо",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        dialogInterface.cancel();
                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    private void initForBank() {
        bank_bonus_button = findViewById(R.id.bank_bonus_button);
        bank_header = findViewById(R.id.bank_header);
        bank_bonus_button.setText("Потратить бонусы");

    }
    public void bank_multibtn_bonus(View view) {//Кнопка с юзом махов и бонусов
        clicker += 1;
        if (clicker >= 1) {
           // menu_admin_add_layout.setVisibility(View.VISIBLE);
            bank_bonus_button.setText("Потратить бонусы");
            bank_header.setText("Что будем тратить?");
            if (clicker >= 2) {
                //menu_admin_add_layout.setVisibility(View.GONE);
                clicker = 0;
                bank_bonus_button.setText("Банк");
                bank_header.setText("Заработок и ежедневные бонусы");
            }
        }
    }
}






