package com.example.outofenergy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore fstore;
    private FirebaseAuth mAuth;

    private LinearLayout linearLayout2;
    private TextView main_circle_1, main_circle_2, main_circle_3, main_AuthText, main_email_x_pswd, main_choosed_provider_text, main_email_password2;
    private EditText main_ed_mail, main_ed_password, main_ed_confirm_password;
    private Button main_btn_next, main_next_btn2;
    private ImageView main_google_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        main_email_password2 = findViewById(R.id.main_email_password2);
        main_next_btn2 = findViewById(R.id.main_next_btn2);

        linearLayout2 = findViewById(R.id.linearLayout2);
       // FirebaseAuth.getInstance().signOut();
        main_ed_mail.setVisibility(View.GONE);
        main_ed_password.setVisibility(View.GONE);
        main_email_password2.setVisibility(View.GONE);
        main_ed_mail.setVisibility(View.GONE);
        main_ed_mail.setVisibility(View.GONE);
        main_next_btn2.setVisibility(View.GONE);
        main_ed_confirm_password.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

    }



    public void init() {   // init all Funcs of this application

        main_google_img = findViewById(R.id.main_google_img);
        main_circle_1 = findViewById(R.id.main_circle_1);
        main_circle_2 = findViewById(R.id.main_circle_2);
        main_circle_3 = findViewById(R.id.main_circle_3);
        main_ed_mail = findViewById(R.id.main_ed_mail);
        main_ed_password = findViewById(R.id.main_ed_password);
        main_ed_confirm_password = findViewById(R.id.main_ed_confirm_password);
        main_AuthText = findViewById(R.id.main_AuthText);
        main_email_x_pswd = findViewById(R.id.main_email_x_pswd);
        main_choosed_provider_text = findViewById(R.id.main_choosed_provider_text);
        main_btn_next = findViewById(R.id.main_btn_next);


        TranslateAnimation animation = new TranslateAnimation(600, -15, 50, 50);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        main_AuthText.startAnimation(animation);
        main_email_x_pswd.startAnimation(animation);
        main_choosed_provider_text.startAnimation(animation);
        main_btn_next.startAnimation(animation);
        main_google_img.startAnimation(animation);


        main_ed_mail = findViewById(R.id.main_ed_mail);
        main_ed_password = findViewById(R.id.main_ed_password);
        main_ed_confirm_password = findViewById(R.id.main_ed_confirm_password);
        main_next_btn2 = findViewById(R.id.main_next_btn2);

        main_ed_password.setText("123123");
        main_ed_mail.setText("test@gmail.com");

    }

    public void init_point_one() {
        //--main_Actions
        TranslateAnimation animation2 = new TranslateAnimation(-600, -15, 50, 50);
        animation2.setDuration(1000);
        animation2.setFillAfter(true);
        main_AuthText.startAnimation(animation2);
        main_email_x_pswd.startAnimation(animation2);
        main_choosed_provider_text.startAnimation(animation2);
        main_btn_next.startAnimation(animation2);
        main_google_img.startAnimation(animation2);

    }

    public void init_point_two() {

        main_ed_mail.setVisibility(View.VISIBLE);
        main_ed_password.setVisibility(View.VISIBLE);
        main_email_password2.setVisibility(View.VISIBLE);
        main_ed_mail.setVisibility(View.VISIBLE);
        main_ed_mail.setVisibility(View.VISIBLE);
        main_next_btn2.setVisibility(View.VISIBLE);
        main_ed_confirm_password.setVisibility(View.VISIBLE);

        TranslateAnimation animation3 = new TranslateAnimation(600, 10, 50, 50);
        animation3.setDuration(1000);
        animation3.setFillAfter(true);
        main_ed_mail.startAnimation(animation3);
        main_ed_password.startAnimation(animation3);
        main_email_password2.startAnimation(animation3);
        main_ed_mail.startAnimation(animation3);
        main_next_btn2.startAnimation(animation3);
        main_ed_confirm_password.startAnimation(animation3);
        linearLayout2.startAnimation(animation3);


    }
    public void init_point_three() {
    }
    public void one_circle_next(View view) {


        TranslateAnimation animation2 = new TranslateAnimation(-1300, -15, 50, 50);
        animation2.setDuration(1000);
        animation2.setFillAfter(true);
        main_AuthText.startAnimation(animation2);
        main_email_x_pswd.startAnimation(animation2);
        main_choosed_provider_text.startAnimation(animation2);
        main_btn_next.startAnimation(animation2);
        main_google_img.startAnimation(animation2);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                main_AuthText.clearAnimation();
                main_email_x_pswd.clearAnimation();
                main_choosed_provider_text.clearAnimation();
                main_btn_next.clearAnimation();
                main_google_img.clearAnimation();
                main_AuthText.setVisibility(View.GONE);
                main_email_x_pswd.setVisibility(View.GONE);
                main_choosed_provider_text.setVisibility(View.GONE);
                main_btn_next.setVisibility(View.GONE);
                main_google_img.setVisibility(View.GONE);

                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main_circle_1.setBackgroundResource(R.drawable.main_circle_register_no_colored);
                        main_circle_2.setBackgroundResource(R.drawable.main_circle_register_colored);
                    }
                }, 1000);


                init_point_two();
            }
        }, 120);

    }

    public void AuthAndOpt(View view) {
        if (main_ed_mail.getText().toString().isEmpty()|| main_ed_password.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this,"Поля пустые",Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(main_ed_mail.getText().toString(), main_ed_password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser users = mAuth.getCurrentUser();

                            if (task.isSuccessful()){
                                DocumentReference df = fstore.collection("Users").document(users.getUid());
                                Map<String,Object> userinfo = new HashMap<>();
                                userinfo.put("NotValid","1");
                                userinfo.put("account_balance","0");
                                userinfo.put("ruble_balance","0");
                                userinfo.put("mah_balance","0");
                                userinfo.put("stars_balance","0");

                                df.set(userinfo);
                                Intent intent = new Intent(MainActivity.this, menu.class);
                                startActivity(intent);

                            }else{  //LOGIN
                                if (main_ed_mail.getText().toString().isEmpty()|| main_ed_password.getText().toString().isEmpty()){
                                    Toast.makeText(MainActivity.this, "Поля не могут быть пусты",Toast.LENGTH_SHORT).show();
                                }else{

                                    mAuth.signInWithEmailAndPassword(main_ed_mail.getText().toString(),main_ed_password.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()){

                                                        checkUserAcessLevel();

                                                    }else{
                                                        Toast.makeText(MainActivity.this, "Неверные данные или вы не зарегистрированы",Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                }
                            }

                        }

                        private void checkUserAcessLevel(){
                            DocumentReference df = fstore.collection("Users").document(mAuth.getUid());
                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Log.d("TAG","onSuccess"+ documentSnapshot.getData());
                                    if (documentSnapshot.getString("isAdmin") !=null){

                                        Intent intent = new Intent(MainActivity.this, menu.class);
                                        intent.putExtra("UserData","ewewweewew");
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(MainActivity.this, "testuspeh",Toast.LENGTH_SHORT).show();

                                    }
                                    if (documentSnapshot.getString("isUser")!= null){
                                        Toast.makeText(MainActivity.this, "test ushp",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, menu.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                    if (documentSnapshot.getString("NotValid")!= null){

                                        Intent intent = new Intent(MainActivity.this, menu.class);
                                        FirebaseUser cUser = mAuth.getCurrentUser();
                                        startActivity(intent);
                                        finish();


                                    }
                                }
                            });
                        }
                    });
        }

    }
}

