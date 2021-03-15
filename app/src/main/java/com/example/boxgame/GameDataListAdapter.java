package com.example.boxgame;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class GameDataListAdapter extends ArrayAdapter<ChoiceGameMap> {
    private int resourceId;
    public GameDataListAdapter( Context context, int resource, List<ChoiceGameMap> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChoiceGameMap choiceGameMap = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.mapName = view.findViewById(R.id.tv_mapName);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mapName.setText(choiceGameMap.getMapName());
        return view;
    }
    class ViewHolder{
        TextView mapName;
    }
}
