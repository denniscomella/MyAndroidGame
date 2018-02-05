package com.example.android.myandroidgame;

import android.graphics.Rect;

/**
 * Created by Dennis on 8/17/2017.
 */

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;


    // set & get methods
    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    //same for y coordinate
    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    // no need to set width & height; only get!
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // rect method to check for collisions; create rectangle around image
    public Rect getRectangle() {
        return new Rect(x, y, x + width, y + height);
    }
}
