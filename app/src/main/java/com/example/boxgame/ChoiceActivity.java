package com.example.boxgame;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择关卡界面
 */
public class ChoiceActivity extends AppCompatActivity{

    ListView listView;
    List<ChoiceGameMap> gameChoiceMapList = new ArrayList<>();
    GameDataListAdapter gameDataListAdapter;
    int mapCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        initMapData();
    }

    public void initMapData(){
        for (int i=1;i<=4;i++){
            ChoiceGameMap choiceGameMap = new ChoiceGameMap("第"+i+"关");
            gameChoiceMapList.add(choiceGameMap);
    }
        listView = findViewById(R.id.listView);
        gameDataListAdapter = new GameDataListAdapter(this,R.layout.choice_list,gameChoiceMapList);
        listView.setAdapter(gameDataListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapCode = position;
                ChoiceGameMap choiceGameMap = gameChoiceMapList.get(position);
                initGame();
            }
        });
    }

    public void addMap(View view) {
        Intent intent = new Intent(this,UserMapActivity.class);
        startActivity(intent);
    }
    public void initGame(){
        Intent intent_mapCode = new Intent(this,GameActivity.class);
        switch (mapCode){
            case 0:
                intent_mapCode.putExtra("0x00",1);
                startActivity(intent_mapCode);
                break;
            case 1:
                intent_mapCode.putExtra("0x00",2);
                startActivity(intent_mapCode);
                break;
            case 2:
                intent_mapCode.putExtra("0x00",3);
                startActivity(intent_mapCode);
                break;
            case 3:
                intent_mapCode.putExtra("0x00",4);
                startActivity(intent_mapCode);
                break;
            default:
                break;
        }
    }
}
