package com.example.boxgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_startGame, btn_aboutGame, btn_exitGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startGame = findViewById(R.id.btn_startGame);
        btn_aboutGame = findViewById(R.id.btn_aboutGame);
        btn_exitGame = findViewById(R.id.btn_exitGame);
        btn_startGame.setOnClickListener(this);
        btn_exitGame.setOnClickListener(this);
        btn_aboutGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_startGame:
                Intent intent = new Intent(this,ChoiceActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_aboutGame:
                break;
            case R.id.btn_exitGame:
                System.exit(1);
                break;
        }
    }
}
