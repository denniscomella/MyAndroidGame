package com.example.android.myandroidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Dennis on 8/17/2017.
 */

public class Background {

    // use Bitmap & Canvas to create images on screen
    // create using BitmapFactory class
    private Bitmap image;
    private int x, y, dx;

    //constructor...
    public Background(Bitmap res){
        image = res;
        dx = GamePanel.MOVESPEED;
    }// end constructor

    public void update(){
        //we want the image to move left every time the screen is updated
        x += dx;

        //check if bg is moving off of the screen
        if (x<-GamePanel.WIDTH){
            //WIDTH is the width of the device screen
            x = 0;
        }
    } // end update

    public void draw(Canvas canvas){
        //draw first bg
        canvas.drawBitmap(image, x, y, null);

        if (x<0){
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y, null); // shouldn't it be x+ image.width? These just happen to be the same.
            // I'm not sure I like how the image Bitmap object is handled here... but I guess it's better than creating and destroying objects.
        }

    }// end draw

}// end class
