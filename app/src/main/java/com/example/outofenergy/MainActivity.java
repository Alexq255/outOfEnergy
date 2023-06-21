package com.example.outofenergy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

private TextView main_circle_1,main_circle_2,main_circle_3,main_AuthText,main_email_x_pswd,main_choosed_provider_text;
private EditText main_ed_mail,main_ed_password,main_ed_confirm_password;
private Button main_btn_next;
private ImageView main_google_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }
    public void init(){   // init all Funcs of this application
        init_point_one();
        init_point_two();
        init_point_three();
        main_google_img = findViewById(R.id.main_google_img);
        main_circle_1=findViewById(R.id.main_circle_1);
        main_circle_2=findViewById(R.id.main_circle_2);
        main_circle_3=findViewById(R.id.main_circle_3);
        main_ed_mail=findViewById(R.id.main_ed_mail);
        main_ed_password=findViewById(R.id.main_ed_password);
        main_ed_confirm_password=findViewById(R.id.main_ed_confirm_password);
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

    }
    public void init_point_one() {

     //--main_Actions

    }
    public void init_point_two() {

    }
    public void init_point_three() {

    }


    public void one_circle_next(View view) {
        TranslateAnimation animation = new TranslateAnimation(-600, -15, 50, 50);
        animation.setDuration(1000);
        animation.setFillAfter(true);

    }
}