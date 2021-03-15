package com.example.boxgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class GameView extends View {
//    地图的行数和列数
    final int MAP_H = 15;
    final int MAP_L = 15;
    Paint mPaint = null;   // 画笔声明
    ArrayList<int[][]> arrayList = new ArrayList<>();   // 声明二维数组，用于记忆以往步骤
    Canvas canvas; // 画布声明
    private int width;
    private int height;
    Boolean isMoveOver = false;  // 此次移动结束标志

//    图片资源：0=墙，1=空，2=玩家，3=箱子1，4=箱子2，
//    5=目的地，6=人和花重叠，7=吃了花之后的玩家
    int wall = 0;
    int empty = 1;
    int gameMan = 2;
    int box1 = 3;
    int box2 = 4;
    int flower = 5;
    int flowerAddGameMan = 6;
    int gameMan2 = 7;
    Bitmap bitmap0 = BitmapFactory.decodeResource(getResources(),R.drawable.qiang);
    Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.kong);
    Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.ren_1);
    Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.xiang_1);
    Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.xiang_2);
    Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(),R.drawable.hua);
    Bitmap bitmap6 = BitmapFactory.decodeResource(getResources(),R.drawable.renandhua);
    Bitmap bitmap7 = BitmapFactory.decodeResource(getResources(),R.drawable.ren_2);

    //二维地图数据
    int[][] gameMap =  {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,5,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,3,1,3,5,0,0,0,0,0},
            {0,0,0,0,5,3,2,1,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,3,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,5,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };

//    通关监听接口
    public interface PassListener{
        void pass();
}
//实现接口
private PassListener myPassLister;
    public void setPassLister(PassListener passLister){
        this.myPassLister = passLister;
    }

    public GameView(Context context) {
        super(context);;
    }
    public GameView(Context context, AttributeSet attr) {
        super(context,attr);
    }
    public GameView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        width = specWidthSize;
        height = specHeightSize;
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        this.canvas = canvas;
        for (int i=0; i<15; i++){
            for (int j=0; j<15; j++){
                int bitmap_point = gameMap[i][j];
//                每一张图片的大小，长宽均分1/15
                Rect rect = new Rect((width/MAP_H)*j,(width/MAP_L)*i,
                        (width/MAP_H)*(j+1),(width/MAP_L)*(i+1));
//                画图
                switch (bitmap_point){
                    case 0:
                        canvas.drawBitmap(bitmap0,null,rect,mPaint);
                        break;
                    case 1:
                        canvas.drawBitmap(bitmap1,null,rect,mPaint);
                        break;
                    case 2:
                        canvas.drawBitmap(bitmap2,null,rect,mPaint);
                        break;
                    case 3:
                        canvas.drawBitmap(bitmap3,null,rect,mPaint);
                        break;
                    case 4:
                        canvas.drawBitmap(bitmap4,null,rect,mPaint);
                        break;
                    case 5:
                        canvas.drawBitmap(bitmap5,null,rect,mPaint);
                        break;
                    case 6:
                        canvas.drawBitmap(bitmap6,null,rect,mPaint);
                        break;
                    case 7:
                        canvas.drawBitmap(bitmap7,null,rect,mPaint);
                        break;
                }
            }
        }
    }

//    设置地图
    public void setGameMap(int[][] pointXY){
        for (int i=0;i<MAP_H; i++){
            for (int j=0; j<MAP_L; j++){
                gameMap[i][j] = pointXY[i][j];
            }
        }
        arrayList.clear();
//        刷新画布
        invalidate();
    }

    /*
    * 向左移动逻辑
    * */
    public void moveLeft(){
        addLastMapData();
        isMoveOver = false;
        for (int i=0;i<MAP_L;i++){
            if (isMoveOver){ break; }
            for (int j=0;j<MAP_H;j++){
                if (isMoveOver){ break; }
                int nowWhat = gameMap[i][j];  // 当前取到的元素
                if (nowWhat==gameMan || nowWhat==flowerAddGameMan || nowWhat==gameMan2) {   // 如果当前取到的元素为人物，则进行移动逻辑代码
                    switch (gameMap[i][j-1]) {
                        case 0:  // 左边是墙
                            Toast.makeText(getContext(), "左边是墙，无法前进", Toast.LENGTH_SHORT).show();
                            isMoveOver = true;
                            break;
                        case 1:  // 左边为空
                            if (gameMap[i][j]==flowerAddGameMan){    // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                            }else {
                                gameMap[i][j] = empty;
                            }
                            gameMap[i][j-1] = gameMan;
                            break;
                        case 3:  // 左边是绿箱子（没有放置好的箱子）
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                if (gameMap[i][j-2]==wall){   // 箱子左边是墙
                                    Toast.makeText(getContext(), "箱子左边是墙，推不动", Toast.LENGTH_SHORT).show();
                                }else {
                                    gameMap[i][j] = flower;
                                }
                            }
                            if (gameMap[i][j-2]==empty){   // 空箱子左边为空
                                gameMap[i][j-2] = box1;
                                gameMap[i][j-1]  = gameMan;
                                gameMap[i][j]=empty;
                            }else if (gameMap[i][j-2]==wall){   // 箱子左边为墙
//                                Toast.makeText(getContext(), "箱子左边是墙，推不动", Toast.LENGTH_SHORT).show();
                            }else if (gameMap[i][j-2]==box1 || gameMap[i][j-2]==box2){    // 箱子左边是箱子
//                                Toast.makeText(getContext(), "两个箱子推不动", Toast.LENGTH_SHORT).show();
                            }else if (gameMap[i][j-2]==flower){  // 箱子左边是花（目的地）
                                gameMap[i][j-2] = box2;  // 箱子变成红箱子
                                gameMap[i][j-1]  = gameMan;
                                gameMap[i][j]=empty;
                            }
                            isMoveOver = true;
                            break;
                        case 4:   // 左边是红箱子（已经放好了的箱子）
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                if (gameMap[i][j-2]==wall){    // 如果红箱子左边是墙
//                                    Toast.makeText(getContext(), "箱子左边是墙，推不动", Toast.LENGTH_SHORT).show();
                                    break;
                                }else {
                                    gameMap[i][j] = flower;
                                }
                            }
                            if (gameMap[i][j-2]==wall){   // 红箱子左边是墙
//                                Toast.makeText(getContext(), "箱子左边是墙，推不动", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (gameMap[i][j-2]==empty){    // 红箱子左边为空
                                gameMap[i][j-2] = box1;
                                gameMap[i][j-1] = gameMan;
                                gameMap[i][j] = empty;
                            }else if (gameMap[i][j-2]==box1 || gameMap[i][j-2]==box2){    // 箱子左边是箱子
                                Toast.makeText(getContext(), "两个箱子推不动", Toast.LENGTH_SHORT).show();
                            }else if (gameMap[i][j-2]==flower){  // 红箱子左边是花（目的地）
                                gameMap[i][j-2] = box2;
                                gameMap[i][j-1]  = flowerAddGameMan;
                            }
                            isMoveOver = true;
                            break;
                        case 5:  // 左边是花
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                            }else {
                                gameMap[i][j] = empty;
                            }
                            gameMap[i][j-1] = flowerAddGameMan;

                            isMoveOver = true;
                            break;
                        default:
                            break;
                    }
                    isMoveOver = true;
                }
            }
        }
        invalidate();
        ifPass();
        }

    /*
    * 向右移动逻辑
    * */
    public void moveRight(){
        addLastMapData();
        isMoveOver = false;
        for (int i=0;i<MAP_L;i++){
            if (isMoveOver){ break; }
            for (int j=0;j<MAP_H;j++){
                if (isMoveOver){ break; }
                int nowWhat = gameMap[i][j];  // 当前取到的元素
                if (nowWhat==gameMan || nowWhat==flowerAddGameMan || nowWhat==gameMan2) {   // 如果当前取到的元素为人物，则进行移动逻辑代码
                    switch (gameMap[i][j+1]) {
                        case 0:  // 右边是墙
//                            Toast.makeText(getContext(), "右边是墙，无法前进", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:  // 右边为空
                            if (gameMap[i][j]==flowerAddGameMan){    // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                            }else {
                                gameMap[i][j] = empty;
                            }
                            gameMap[i][j+1] = gameMan;
                            break;
                        case 3:  // 右边是绿箱子（没有放置好的箱子）
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                                gameMap[i][j+1] = gameMan;
                            }
                            if (gameMap[i][j+2]==empty){   // 空箱子右边为空
                                gameMap[i][j+2] = box1;
                                gameMap[i][j+1]  = gameMan;
                                gameMap[i][j]=empty;
                            }else if (gameMap[i][j+2]==wall){   // 箱子右边为墙
//                                Toast.makeText(getContext(), "箱子右边是墙，推不动", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (gameMap[i][j+2]==box1 || gameMap[i][j+2]==box2){    // 箱子左边是箱子
//                                Toast.makeText(getContext(), "两个箱子推不动", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (gameMap[i][j+2]==flower){  // 箱子右边是花（目的地）
                                gameMap[i][j+2] = box2;  // 箱子变成红箱子
                                gameMap[i][j+1]  = gameMan;
                                gameMap[i][j]=empty;
                            }
                            break;
                        case 4:   // 右边是红箱子（已经放好了的箱子）
                            if (gameMap[i][j]==flowerAddGameMan) {  // 如果当前状态为人+花
                                if (gameMap[i][j + 2] == wall) {    // 如果红箱子右边是墙
                                    Toast.makeText(getContext(), "箱子右边是墙，推不动", Toast.LENGTH_SHORT).show();
                                    break;
                                } else {
                                    gameMap[i][j] = flower;
                                }
                            }
                            if (gameMap[i][j+2]==wall){   // 红箱子右边是墙
                                Toast.makeText(getContext(), "箱子右边是墙，推不动", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (gameMap[i][j+2]==empty){    // 红箱子左边为空
                                gameMap[i][j+2] = box1;    // 变成绿箱子
                                gameMap[i][j+1] = flowerAddGameMan;
                            }else if (gameMap[i][j+2]==box1 || gameMap[i][j+2]==box2){    // 箱子左边是箱子
                                Toast.makeText(getContext(), "两个箱子推不动", Toast.LENGTH_SHORT).show();
                            }else if (gameMap[i][j+2]==flower){  // 红箱子右边是花（目的地）
                                gameMap[i][j+2] = box2;
                                gameMap[i][j+1]  = flowerAddGameMan;
                            }
                            break;
                        case 5:  // 右边是花
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                            }else{
                                gameMap[i][j] = empty;
                            }
                            gameMap[i][j+1] = flowerAddGameMan;
                            isMoveOver = true;
                            break;
                        default:
                            break;
                    }
                    isMoveOver = true;  // 移动结束标志
                }
            }
        }
        invalidate();
        ifPass();
    }


    /*
    * 向下移动逻辑
    * */
    public void moveDown(){
        addLastMapData();
        isMoveOver = false;
        for (int i=0;i<MAP_L;i++){
            if (isMoveOver){ break; }
            for (int j=0;j<MAP_H;j++){
                if (isMoveOver){ break; }
                int nowWhat = gameMap[i][j];  // 当前取到的元素
                if (nowWhat==gameMan || nowWhat==flowerAddGameMan || nowWhat==gameMan2) {   // 如果当前取到的元素为人物，则进行移动逻辑代码
                    switch (gameMap[i+1][j]) {
                        case 0:  // 下面是墙
                            Toast.makeText(getContext(), "下面是墙，无法前进", Toast.LENGTH_SHORT).show();
                            isMoveOver = true;
                            break;
                        case 1:  // 下面为空
                            if (gameMap[i][j]==flowerAddGameMan){    // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                            }else {
                                gameMap[i][j] = empty;
                            }
                            gameMap[i+1][j] = gameMan;
                            isMoveOver = true;
                            break;
                        case 3:  // 下面是绿箱子（没有放置好的箱子）
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                                gameMap[i+1][j] = gameMan;
                            }
                            if (gameMap[i+2][j]==empty){   // 空箱子下面为空
                                gameMap[i+2][j] = box1;
                                gameMap[i+1][j]  = gameMan;
                                gameMap[i][j]=empty;
                            }else if (gameMap[i+2][j]==wall){   // 箱子下面为墙
                                Toast.makeText(getContext(), "箱子下面是墙，推不动", Toast.LENGTH_SHORT).show();
                            }else if (gameMap[i+2][j]==box1 || gameMap[i+2][j]==box2){    // 箱子下面是箱子
                                Toast.makeText(getContext(), "两个箱子推不动", Toast.LENGTH_SHORT).show();
                            }else if (gameMap[i+2][j]==flower){  // 箱子下面是花（目的地）
                                gameMap[i+2][j] = box2;  // 箱子变成红箱子
                                gameMap[i+1][j]  = gameMan;
                                gameMap[i][j]=empty;
                            }
                            isMoveOver = true;
                            break;
                        case 4:   // 下面是红箱子（已经放好了的箱子）
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                if (gameMap[i+2][j]==wall){    // 如果红箱子下面是墙
                                    Toast.makeText(getContext(), "箱子下面是墙，推不动", Toast.LENGTH_SHORT).show();
                                    break;
                                }else {
                                    gameMap[i][j] = flower;
                                }
                            }
                            if (gameMap[i+2][j]==wall){   // 红箱子下面是墙
                                Toast.makeText(getContext(), "箱子下面是墙，推不动", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (gameMap[i+2][j]==empty){    // 红箱子下面为空
                                gameMap[i+2][j] = box1;
                                gameMap[i+1][j] = flowerAddGameMan;
                            }else if (gameMap[i+2][j]==box1 || gameMap[i+2][j]==box2){    // 箱子下面是箱子
                                Toast.makeText(getContext(), "两个箱子推不动", Toast.LENGTH_SHORT).show();
                            }else if (gameMap[i+2][j]==flower){  // 红箱子下面是花（目的地）
                                gameMap[i+2][j] = box2;
                                gameMap[i+1][j]  = flowerAddGameMan;
                            }
                            break;
                        case 5:  // 下面是花
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                            }else {
                                gameMap[i][j] = empty;
                            }
                            gameMap[i+1][j] = flowerAddGameMan;
                            isMoveOver = true;
                            break;
                        default:
                            Toast.makeText(getContext(), "未知操作", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    isMoveOver = true;
                }
            }
        }
        invalidate();
        ifPass();
    }

    /*
    *向上移动逻辑
    * */
    public void moveUp(){
        addLastMapData();
        isMoveOver = false;
        for (int i=0;i<MAP_L;i++){
            if (isMoveOver){ break; }
            for (int j=0;j<MAP_H;j++){
                if (isMoveOver){ break; }
                int nowWhat = gameMap[i][j];  // 当前取到的元素
                if (nowWhat==gameMan || nowWhat==flowerAddGameMan || nowWhat==gameMan2) {   // 如果当前取到的元素为人物，则进行移动逻辑代码
                    switch (gameMap[i-1][j]) {
                        case 0:  // 上面是墙
                            Toast.makeText(getContext(), "上面是墙，无法前进", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:  // 上面为空
                            if (gameMap[i][j]==flowerAddGameMan){    // 如果当前状态为人+花
                                gameMap[i][j] = flower;
                            }else {
                                gameMap[i][j] = empty;
                            }
                            gameMap[i-1][j] = gameMan;
                            break;
                        case 3:  // 上面是绿箱子（没有放置好的箱子）
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                if (gameMap[i-2][j]==wall){
                                    Toast.makeText(getContext(), "箱子上面是墙，推不动", Toast.LENGTH_SHORT).show();
                                    break;
                                }else {
                                    gameMap[i][j] = flower;    // 原坐标变成花
                                }
                            }
                            if (gameMap[i-2][j]==empty){   // 空箱子上面为空
                                gameMap[i-2][j] = box1;
                                gameMap[i-1][j]  = gameMan;
                                gameMap[i][j] = empty;   // 原坐标变为空
                            }else if (gameMap[i-2][j]==wall){   // 箱子上面为墙
                                Toast.makeText(getContext(), "箱子上面是墙，推不动", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (gameMap[i-2][j]==box1 || gameMap[i-2][j]==box2){    // 箱子上面是箱子
                                Toast.makeText(getContext(), "两个箱子推不动", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (gameMap[i-2][j]==flower){  // 箱子上面是花（目的地）
                                gameMap[i-2][j] = box2;  // 箱子变成红箱子
                                gameMap[i-1][j] = gameMan;
                                gameMap[i][j] = empty;
                            }
                            break;
                        case 4:   // 上面是红箱子（已经放好了的箱子）
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                if (gameMap[i-2][j]==wall){    // 如果红箱子上面是墙
                                    Toast.makeText(getContext(), "箱子上面是墙，推不动", Toast.LENGTH_SHORT).show();
                                    break;
                                }else {
                                    gameMap[i][j] = flower;
                                }
                            }
                            if (gameMap[i-2][j]==wall){   // 红箱子上面是墙
                                Toast.makeText(getContext(), "箱子上面是墙，推不动", Toast.LENGTH_SHORT).show();
                                break;
                            }else if (gameMap[i-2][j]==empty){    // 红箱子上面为空
                                gameMap[i-2][j] = box1;  // 红箱子变成绿箱子
                                gameMap[i-1][j] = flowerAddGameMan;    // 原来红箱子在的地方变成人+花
                            }else if (gameMap[i-2][j]==box1 || gameMap[i-2][j]==box2){    // 箱子上面是箱子
                                Toast.makeText(getContext(), "两个箱子推不动", Toast.LENGTH_SHORT).show();
                            }else if (gameMap[i-2][j]==flower){  // 红箱子上面是花（目的地）
                                gameMap[i-2][j] = box2;
                                gameMap[i-1][j]  = flowerAddGameMan;   // 原来红箱子在的地方变成人+花
                            }
                            break;
                        case 5:    // 如果上面是花
                            if (gameMap[i][j]==flowerAddGameMan){  // 如果当前状态为人+花
                                gameMap[i][j] = flower;    // 原坐标变成花
                            }else{
                                gameMap[i][j] = empty;  // 否则原坐标变成空
                            }
                            gameMap[i-1][j] = flowerAddGameMan;   // 上移后变成人+花
                            break;
                        default:
                            Toast.makeText(getContext(), "未知操作", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    isMoveOver = true;    // 移动结束标志
                }
            }
        }
        invalidate();
        ifPass();
    }

    /*
    * 判断当前是否通关
    * */
    boolean isPass = false;
    public void ifPass(){
        isPass = false;
        for (int i=0;i<MAP_L;i++) {
            if (isPass) {
                break; }
            for (int j = 0; j < MAP_H; j++) {
                if (isPass) { break;}
                int now = gameMap[i][j];
                if (now==box1){
                    isPass=true;
                }
            }
        }
        if (!isPass){
            myPassLister.pass();
        }
    }

    /*
    * 记录上一步的地图数据，用于返回上一步
    * */
    int[][][] lastMap = new int[25][15][15];   // 最多撤回当前的前25步
   ArrayList<int [][]> lastMapList = new ArrayList<>();
    int count = 1;
    public void addLastMapData(){
        if (count>19){
            lastMapList.clear();
            count=0;
        }
        if (lastMapList.size()>20){   // 满20步后
            lastMapList.remove(0);   // 删除第前19步
        }
        for (int i=0; i<MAP_L; i++){
            for (int j=0; j<MAP_H; j++){
                lastMap[count][i][j] = gameMap[i][j];
            }
        }
        lastMapList.add(lastMap[count]);
        count++;
    }

    /*
    * 返回上一步
    * */
    public void goBack(){
        if (lastMapList.size()>0){
            this.gameMap = lastMapList.get(lastMapList.size()-1);
            invalidate();
            lastMapList.remove(lastMapList.size()-1);
        }
    }
}
