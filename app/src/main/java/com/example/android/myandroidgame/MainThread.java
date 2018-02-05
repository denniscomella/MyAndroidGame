package com.example.android.myandroidgame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

// import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Dennis on 8/3/2017.
 */

public class MainThread extends Thread {

    public static Canvas canvas;
    // Variables
    private int FPS = 100;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;


    // create class constructor
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    } // end of constructor


    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;

        int frameCount = 0;

        long targetTime = 1000 / FPS;
        System.out.println("hello");
        while (running) {
            // set the time
            startTime = System.nanoTime(); // 1 millisecond = 10^6 nanoseconds, i.e. 1s = 10^12 nsec

            canvas = null; // canvas will paint object to screen every frame

            try {
                //lock canvas to content view
                canvas = this.surfaceHolder.lockCanvas();

                //sync every time something is updated or drawn
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
            } // catch any exception

            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // do time calculation to change nsec to msec
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            if (waitTime > 0) {
                try {
                    this.sleep(waitTime);
                } catch (Exception e) {
                }
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == FPS) { // this code calculates averageFPS but is inefficient
                // if (totalTime >=1) { averageFPS = frameCount; } // ???
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("Average FPS: " + averageFPS);
            }

        }  //end while


    } // end of run method

    public void setRunning(boolean b) {
        running = b;
    }

} // end of thread class
