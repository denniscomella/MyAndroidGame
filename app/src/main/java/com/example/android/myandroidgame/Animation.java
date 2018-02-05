package com.example.android.myandroidgame;

import android.graphics.Bitmap;

/**
 * Created by Dennis on 8/22/2017.
 */

public class Animation {
    private Bitmap[] frames; // bitmap table to keep the set of image frames.

    private int currentFrame;

    private long startTime; // animation timer
    private long delay; // delay between frames

    private boolean playedOnce; // for animations we only want to play one time and stop


    // need to create animation here: decide on frames and timer, etc.
    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long d) {
        delay = d;
    }

    public void setFrame(int i) {
        currentFrame = i;
    }

    public void update() {
        //create timer in milliseconds
        // timer will determine a lot of gameplay actions, image animations, etc.
        // all objects will have a timer.
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        if (elapsed >= delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if (currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }//end update

    //what will the hero class be drawing?
    public Bitmap getImage() {
        return frames[currentFrame];
    }

    public int getFrame() {
        return currentFrame;
    }

    public boolean isPlayedOnce() {
        return playedOnce;
    }
}
