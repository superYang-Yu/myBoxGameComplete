package com.example.boxgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class UserMapActivity extends AppCompatActivity {
    Button btn_setData;
    EditText et_mapLineData;
    GameView  gameView;
    GameMapData gameMapData;
    int[][] lineDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);
        et_mapLineData = findViewById(R.id.et_mapLineData);
        btn_setData = findViewById(R.id.btn_setData);
        gameView = findViewById(R.id.gv_myMap);
        gameMapData = new GameMapData();
        lineDate = gameMapData.arrayList.get(0);  // 初始二维地图数据
        gameView.setGameMap(lineDate);
    }
/*
*          将当前编辑框的数据放到地图上
* */
    int line = 0; // 第几行
    String data;  // 初始的文本数据（输入框数据）
    String[] data_str; // 分割后的字符串数组
    int[] myLineList = new int[15]; // 分割字符串数组转化为int数组
    public void addLineDate(View view) {
//        获取输入框中的数据并且去空处理
        data = et_mapLineData.getText().toString().replaceAll(" ","");
        data_str = data.split(",");
        if (data_str.length!=15){
            Toast.makeText(this, "数据超过或不足15个，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
//      数据类型转换
        for (int k=0; k<15; k++){
            myLineList[k] = Integer.parseInt(data_str[k]);
        }
//        将数据一行一行插入数组
        if (line<15){
            for (int i=0; i<15; i++){
                lineDate[line][i] = myLineList[i];
            }
        }
        btn_setData.setText("设置"+(line+2)+"行数据");
        gameView.setGameMap(lineDate);
       if (line<15){
           line++;
       }else {
           Toast.makeText(this, "已完成地图制作", Toast.LENGTH_SHORT).show();
           return;
       }
        et_mapLineData.setText("");
    }

    public void useMap(View view) {
        int[] data1 = new int[15];  // 列数据
        int[] data2 = new int[15];  // 行数据
        for (int i=0; i<line; i++){
            for (int j=0; j<15; j++){
                lineDate[i][j] = gameView.gameMap[i][j];
            }
        }
        for (int i=0; i<15; i++){
            data1[i] = lineDate[i][0]; }

        for (int i=0; i<15; i++){
            data2[i] = lineDate[0][i]; }

        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("0x10",data1);
        intent.putExtra("0x11",data2);
        startActivity(intent);
    }

    public void againMap(View view) {
        GameMapData gameMapData1 = new GameMapData();
        gameView.setGameMap(gameMapData1.arrayList.get(0));
    }
}
