package com.example.boxgame;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
* 游戏主界面
* */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    GameView gameView;
    GameMapData gameMapData;
    ImageButton imBtn_Left, imBtn_Right, imBtn_Up, imBtn_Down, imBtn_back;
    Button btn_last, btn_again, btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setViewID();
        getInter();
        gameView.setPassLister(new GameView.PassListener() {
            @Override
            public void pass() {
                Toast.makeText(GameActivity.this, "恭喜通关！！！", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void setViewID(){
        gameView = findViewById(R.id.gameView);
        imBtn_Left = findViewById(R.id.imBtn_left);
        imBtn_Right = findViewById(R.id.imBtn_right);
        imBtn_Up = findViewById(R.id.imBtn_up);
        imBtn_Down = findViewById(R.id.imBtn_down);
        imBtn_back = findViewById(R.id.imBtn_back);
        btn_last = findViewById(R.id.btn_last);
        btn_again = findViewById(R.id.btn_again);
        btn_next = findViewById(R.id.btn_next);
        imBtn_Left.setOnClickListener(this);
        imBtn_Right.setOnClickListener(this);
        imBtn_Up.setOnClickListener(this);
        imBtn_Down.setOnClickListener(this);
        imBtn_back.setOnClickListener(this);
        btn_last.setOnClickListener(this);
        btn_again.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        gameMapData = new GameMapData();
    }

    /**
     * 移动监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imBtn_left:
                gameView.moveLeft();
                break;
            case R.id.imBtn_right:
                gameView.moveRight();
                break;
            case R.id.imBtn_up:
                gameView.moveUp();
                break;
            case R.id.imBtn_down:
                gameView.moveDown();
                break;
            case R.id.btn_last:
//                判断下一关卡是否超出数组下标
                if((mapCode-1)>0){
                    mapCode = mapCode - 1;
                    gameView.setGameMap(gameMapData.arrayList.get(mapCode));
                }else {
                    Toast.makeText(this, "已经是第一关了", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.btn_again:
                if (isMyMap){
                    gameView.setGameMap(gameMapData.arrayList.get(gameMapData.arrayList.size()-1));
                }else {
                    gameView.setGameMap(gameMapData.arrayList.get(mapCode));
                }
                break;
            case R.id.btn_next:
                //                判断上一关卡是否超出数组下标
                if ((mapCode+1) < gameMapData.arrayList.size()){
                    mapCode = mapCode + 1;
                    gameView.setGameMap(gameMapData.arrayList.get(mapCode));
                }else {
                    Toast.makeText(this, "已经是最后一关了", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.imBtn_back:
                gameView.goBack();
                break;
            default:
                Toast.makeText(this, "没有设置此按钮的监听", Toast.LENGTH_SHORT).show();
                break;
        }
    }
/*
*    获取上个界面传输过来的地图数据下标
* */
    int mapCode;
    int[] data1;
    int[] data2;
    boolean isMyMap = false;
    public void getInter(){
         mapCode = getIntent().getIntExtra("0x00",1);
                 data1 = getIntent().getIntArrayExtra("0x10");
                 data2 = getIntent().getIntArrayExtra("0x11");
                 if (data1 !=null && data2 !=null){
                     int[][] data = new int[15][15];
                     for (int i=0; i<15; i++){
                         data[i][0] = data1[i];
                     }
                     for (int i=0; i<15; i++){
                         data[0][i] = data2[i];
                     }
//                     gameMapData.arrayList.add(data);
                     gameView.setGameMap(data);
                     btn_next.setVisibility(View.INVISIBLE);
                     btn_last.setVisibility(View.INVISIBLE);
                     isMyMap = true;
                 } else {
                     isMyMap = false;
                     gameView.setGameMap(gameMapData.arrayList.get(mapCode));
                 }
        }
    }

