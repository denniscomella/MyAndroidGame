package com.example.android.myandroidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Dennis on 8/17/2017.
 */

public class Enemy extends GameObject {

    private int score; // score value
    private int speed;
    private Random rand = new Random(); // to randomize speed
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public Enemy(Bitmap res, int x, int y, int w, int h, int s, int numFrames){
        super.x = x;
        super.y = y;

        //dimensions for creating bitmap
        height = h;
        width = w;

        score = s;

        //randomized speed every gameloop
        speed = 4 + (int) (rand.nextDouble()*score/30);
        if (speed > 40) {speed = 40;} // max speed

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++) { // 3 frames in the image
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100-speed);

    }
    public void update(){
        x-= speed;
        animation.update();
    }

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch(Exception e){}
    }

    @Override
    public int getWidth() {
        // provides "more realistic collision detection"
        return width-10;
    }
}
