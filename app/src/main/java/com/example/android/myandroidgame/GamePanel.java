package com.example.android.myandroidgame;

import android.content.Context;
//import android.support.annotation.MainThread; // huh? why? This caused an error
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import static android.R.attr.width;
import static com.example.android.myandroidgame.MainThread.canvas;

/**
 * Created by Dennis on 7/29/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private Random rand = new Random();

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    public static final int MOVESPEED = -5;

    private Background bg;

    private Hero hero;

    private ArrayList<Bullet> bullet; // ArrayList allows list to grow
    private long bulletStartTime;

    // enemy info
    private ArrayList<Enemy> alien;
    private long alienStartTime;


    private ArrayList<BorderBottom> bottomBorders;
    private ArrayList<BorderTop> topBorders;

    private ArrayList<Obstacle> obstacle;
    private long obstacleStartTime;

    private ArrayList<Explosion> explosions;

    // vars to reset the game
    private boolean newGameCreated;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;


    //reference
    private MainThread thread;


    //class constructor
    public GamePanel(Context context) {

        super(context);

        //callback
        getHolder().addCallback(this);


        //make GamePanel focusable so it can handle events
        setFocusable(true);


    }//end constructor


    @Override // called after surface is created
    public void surfaceCreated(SurfaceHolder holder) {

        //object; moved here (from constructor) to avoid lag issues(?)
        thread = new MainThread(getHolder(), this);

        //draw the image on screen
        //I'm not really sure what this BitmapFactory does
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        bottomBorders = new ArrayList<BorderBottom>();
        topBorders = new ArrayList<BorderTop>();
        int i = 0;
        while (i < WIDTH+20) {
            bottomBorders.add(new BorderBottom(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom), i, HEIGHT - 10 - rand.nextInt(50)));
            topBorders.add(new BorderTop(BitmapFactory.decodeResource(getResources(), R.drawable.borderbottom), i, 10 - 120 + rand.nextInt(50), 120));
            i += bottomBorders.get(0).width;
        }


        //create hero object
        hero = new Hero(BitmapFactory.decodeResource(getResources(), R.drawable.hero), 30, 45, 3);

        bullet = new ArrayList<Bullet>();
        bulletStartTime = System.nanoTime();

        alien = new ArrayList<Enemy>();
        alienStartTime = System.nanoTime();

        obstacle = new ArrayList<Obstacle>();
        obstacleStartTime = System.nanoTime();

        thread.setRunning(true);
        thread.start();
    }

    @Override // called after structural changes (i.e. format or size) have been made to surface
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override // called immediately before surface is destroyed; disables surface in all threads
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter <1000) {
            try {
                thread.setRunning(false);
                thread.join(); // holds dead thread until it is finished executing

                // kill the thread to start anew next time
                retry = false;
                thread = null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override // know if we touched the screen
    public boolean onTouchEvent(MotionEvent event) {
        //do action when we touch the screen

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!hero.isPlaying() && newGameCreated && reset) {
                hero.setPlaying(true);
                hero.setUp(true);
            } else if (hero.isPlaying()) {
                if (!started) started = true;
                reset = false;
                hero.setUp(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            hero.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    //update GamePanel with thread to run a gameloop every second
    public void update() {
        if (hero.isPlaying()) {
            bg.update();

            for (int i = 0; i < bottomBorders.size(); i++) {
                bottomBorders.get(i).update();
            }
            for (int i = 0; i < topBorders.size(); i++) {
                topBorders.get(i).update();
            }

            hero.update();

            long obstacleElapsed = (System.nanoTime()-obstacleStartTime)/1000000;

            if (obstacleElapsed > (15000-hero.getScore()/4)) {
                // bottom obstacle appears
                obstacle.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle),
                        WIDTH+10, HEIGHT-290+rand.nextInt(150), 90, 300, hero.getScore(), 1));
                // obstacle height will be 190px < h < 340px (HEIGHT == 480)

                //reset timer
                obstacleStartTime = System.nanoTime();
            }

            // loop through obstacles and check collision
            for (int i = 0; i < obstacle.size(); i++) {
                obstacle.get(i).update();

                if (collision(obstacle.get(i), hero)) {
                    // hero has collided with enemy
                    obstacle.remove(i);
                    hero.setPlaying(false);
                    break;
                }

                if (obstacle.get(i).getX() < -100) {
                    obstacle.remove(i);
                    break;
                }

            }

            // loop through bullets

            long bulletTimer = (System.nanoTime() - bulletStartTime) / 1000000;
            if (bulletTimer > (2500 - hero.getScore() / 4)) {
                // add bullet to ArrayList at the middle-right of the hero; 7 frame of 15x7 images
                bullet.add(new Bullet((BitmapFactory.decodeResource(getResources(), R.drawable.bullet)), hero.getX() + 60, hero.getY() + 24, 15, 7, 7));
                bulletStartTime = System.nanoTime();
            }

            for (int i = 0; i < bullet.size(); i++) {
                bullet.get(i).update();
                if (WIDTH - bullet.get(i).getX() < -10) {
                    bullet.remove(i);
                }else { // collide with obstacle
                    for (int j = 0; j < obstacle.size(); j++) {
                        if (collision(bullet.get(i), obstacle.get(j))) {
                            bullet.remove(j);
                            break;
                        }
                    }
                }
            }
            // movement behavior of enemy
            long alienElapsed = (System.nanoTime() - alienStartTime) / 1000000;
            if (alienElapsed > (10000 - hero.getScore() / 4)) {
                alien.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
                        WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - 50)), 40, 60, hero.getScore(), 3));
                alienStartTime = System.nanoTime(); // reset timer
            }

            // loop through enemies and check collision
            for (int i = 0; i < alien.size(); i++) {
                alien.get(i).update();

                if (collision(alien.get(i), hero)) {
                    // hero has collided with enemy
                    alien.remove(i);
                    hero.setPlaying(false);
                    break;
                }

                if (alien.get(i).getX() < -100) {
                    alien.remove(i);
                    break;
                }

                // collision between enemy & bullet
                for (int j = 0; j < bullet.size(); j++) {
                    if (collision(alien.get(i), bullet.get(j))) {
//                        explosions.add(new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion),
//                                alien.get(i).x, alien.get(i).y, 500/4, 300/3, 4*3));
                        alien.remove(i);
                        bullet.remove(j);
                        break;
                    } else {
                        bullet.get(j).update();
                    }
                }
            } // end enemy loop

//            for (int i = 0; i<explosions.size(); i++){
//                explosions.get(i).update();
//            }

        } // end if playing

        else { // not playing
            hero.resetDYA();
            if(!reset){
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                disappear = true;
            }

            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed>2500 && !newGameCreated){
                newGame();
            }

        } // end else

    } // end update


    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    } // end collision


    //draw method
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //need to scale the images for all screen sizes
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);  // SurfaceView.getWidth()
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        // this is the most important to scale everything to the screen!!!
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if (!disappear){
                hero.draw(canvas);
            }


            for (BorderBottom bb : bottomBorders) {
                bb.draw(canvas);
            }
            for (BorderTop bt : topBorders) {
                bt.draw(canvas);
            }

            for (Bullet fp : bullet) {
                fp.draw(canvas);
            }

            for (Enemy aln : alien) {
                aln.draw(canvas);
            }

            for (Obstacle obs : obstacle) {
                obs.draw(canvas);
            }

            // reset scale
            canvas.restoreToCount(savedState);
        }
    }// end draw

    public void newGame(){
        // resets the game
        disappear = false;

        alien.clear();
        obstacle.clear();
        bottomBorders.clear();
        topBorders.clear();
        bullet.clear();

        hero.resetDYA();
        hero.resetScore();
        hero.setY(HEIGHT/2);

        newGameCreated = true;
    }
}
