package com.example.listit;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends Activity {

    SharedPreferences sharedPreferences;
    EditText userNameED;
    TextView userNameTV, phraseTV;
    Button rememberMeButton;
    Intent intent;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        intent = new Intent(this, SecondActivity.class);

        userNameED = findViewById(R.id.userNameED);

        userNameTV = findViewById(R.id.userNameTV);
        phraseTV = findViewById(R.id.phrases);
        rememberMeButton = findViewById(R.id.rememberMeButton);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        rememberMeButton.setOnClickListener(v -> {

            String userName = userNameED.getText().toString();

            editor.putString("NAME", userName);
            editor.apply();

            if (!userName.isEmpty()) {

                YoYo.with(Techniques.FadeIn)
                        .duration(1000)
                        .playOn(userNameTV);

                YoYo.with(Techniques.FadeIn)
                        .duration(1000)
                        .playOn(phraseTV);

                userNameTV.setText(sharedPreferences.getString("NAME", "notFound"));
                userNameTV.setTextColor(getColor(R.color.dark_orange));
                phraseTV.setText(getString(R.string.phrase));

                handler.postDelayed(() -> {
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }, 1500);

            }

        });

    }

}