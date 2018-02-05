package com.example.android.myandroidgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Dennis on 8/17/2017.
 */

public class BorderBottom extends GameObject {
    private Bitmap image;
    private Random rand = new Random();

    public BorderBottom(Bitmap res, int x, int y) {
        height = 150;
        width = 20;

        this.x = x;
        this.y = y;

        dx = GamePanel.MOVESPEED;

        // no for loop, only one frame
        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }// end constructor

    public void update() {
        x += dx;
        if (x < -width) {
            x = GamePanel.WIDTH;
            y += 30 - rand.nextInt(60);
            if (y > GamePanel.HEIGHT) {
                y = GamePanel.HEIGHT;
            } else if (y < GamePanel.HEIGHT - 120) {
                y = GamePanel.HEIGHT - 120;
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }
}
