package com.example.android.myandroidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import static android.R.attr.start;
import static android.R.attr.x;

/**
 * Created by Dennis on 8/17/2017.
 */

public class Hero extends GameObject {
    //hero variables
    private Bitmap spritesheet; // this is going to be two images, so we need a var here

    private int score;
    private double dya; // y speed when we touch the screen
    private boolean up; // going up or down?
    private boolean playing; // if we are playing the game

    private Animation animation = new Animation();
    private long startTime;


    public Hero(Bitmap res, int w, int h, int numFrames) {
        //need to coordinate with Animation class
        x = 100;
        y = GamePanel.HEIGHT / 2;

        //need to determine direction of hero on screen press
        dy = 0;

        score = 0;

        //dimensions for creating bitmap
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++) { // 3 frames in the image
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(10);

        startTime = System.nanoTime();

    }// end hero constructor

    // Create boolean function if the screen is being pressed.
    public void setUp(boolean b) {
        up = b;
    }


    public void update() { // hero behavior
        //timer
        long elapsed = (System.nanoTime() - startTime) / 10000000;

        // score will auto-increment every 100 ms
        if (elapsed >= 100) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if (up) {
            dy = (int) (dya -= 1.1);
        } else {
            dy = (int) (dya += 1.1);
        }

        // set speed limit
        if (dy > 14) dy = 14;
        if (dy < -14) dy = -14;

        y += dy * 2;
        dy = 0;
    } // end update

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    } // end draw method

    // some simple extra methods that we will need
    public int getScore() {
        return score;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDYA() {
        dya = 0;
    }

    public void resetScore() {
        score = 0;
    }

} // end class
