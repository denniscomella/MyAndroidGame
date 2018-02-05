package com.example.android.myandroidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Dennis on 9/22/2017.
 */

public class Bullet extends GameObject {
    private int speed;

    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public Bullet(Bitmap res, int x, int y, int w, int h, int numFrames){
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        // set initial speed
        speed = 13;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        // save all the frames to the image[i] array/table
        for(int i = 0; i<image.length;i++){
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        animation.setFrames(image);

        animation.setDelay(120-speed);

    } // end constructor

    public void update(){
        x += speed - 4;
        animation.update();

    } // end update

    public void draw(Canvas canvas){

        try{
            canvas.drawBitmap(animation.getImage(), x-30, y, null);
        }catch (Exception e){}

    } // end draw
}
