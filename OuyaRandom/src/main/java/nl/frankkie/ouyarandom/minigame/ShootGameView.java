package nl.frankkie.ouyarandom.minigame;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import nl.frankkie.ouyarandom.R;
import tv.ouya.console.api.OuyaController;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Gebruiker on 29-6-13.
 */
public class ShootGameView extends View {

    Context context;
    Rect screen;
    Paint paint;
    int score = 0;
    long frame = 0;
    OuyaController controller;
    Player player;
    ArrayList<Stuff> stuffs = new ArrayList<Stuff>();
    long lastFpsTime = 0;
    int showFps = 0;
    int fps = 0;
    double showDelta = 0;
    boolean gameRunning = true;
    Handler handler = new Handler();
    long lastShoot = 0;
    long shootDelay = 250;
    Object shootLock = new Object();
    boolean shootAdd = true;

    public ShootGameView(Context context) {
        super(context);
        this.context = context;
        OuyaController.init(context);
        initGame();
//        gameLoop();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                gameLoop();
            }
        });
        t.start();
    }

    protected void initGame() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        screen = new Rect(0, 0, 1920, 1080);
        controller = OuyaController.getControllerByPlayer(0);
        player = new Player();
        stuffs.add(player);
        int makeInitialEnemies = 5;
        for (int i = 0; i < makeInitialEnemies; i++) {
            stuffs.add(new Enemy());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        canvas.drawRect(screen, paint); //clear screen
        paint.setColor(Color.WHITE);
        paint.setTextSize(22);
        canvas.drawText("Score: " + score, 150f, 200f, paint);
        canvas.drawText("Frame: " + (frame++), 150f, 250f, paint);
        canvas.drawText("FPS: " + (showFps), 150f, 300f, paint);
//        player.drawMe(canvas,paint);
        synchronized (shootLock) {
            shootAdd = false;
            for (Stuff s : stuffs) {
                s.drawMe(canvas, paint);
            }
        }
        shootAdd = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyDown(keyCode, event);
        updateGame();
        return handled || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = OuyaController.onKeyUp(keyCode, event);
        updateGame();
        return handled || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        boolean handled = OuyaController.onGenericMotionEvent(event);
        updateGame();
        return handled || super.onGenericMotionEvent(event);
    }

    protected void updateGame() {
        //This get called on input
        if (controller.getButton(OuyaController.BUTTON_MENU)) {
            //KILL GAME
            gameRunning = false;
            ((Activity) context).finish();
        }
        //change speed and direction
        int velocityMultiplier = 10;
        player.velocity = pointDistance(0, 0, controller.getAxisValue(OuyaController.AXIS_LS_X), controller.getAxisValue(OuyaController.AXIS_LS_Y)) * velocityMultiplier;
        player.direction = pointDirection(0, 0, controller.getAxisValue(OuyaController.AXIS_LS_X), controller.getAxisValue(OuyaController.AXIS_LS_Y));
    }


    @Override
    public ArrayList<View> getFocusables(int direction) {
        return super.getFocusables(direction);
    }

    /*
         * Thanks to:
         * http://www.java-gaming.org/index.php?topic=24220.0
         */
    void gameLoop() {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        // keep looping round til the game ends
        while (gameRunning) {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) OPTIMAL_TIME);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000) {
                //System.out.println("(FPS: " + fps + ")");
                lastFpsTime = 0;
                showFps = fps;
                fps = 0;
            }

            // update the game logic
            doGameUpdates(delta);
            showDelta = delta;

            // draw everyting
            //postInvalidate();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });

//            Runtime.getRuntime().gc();

            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
                Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
            } catch (Exception e) {
                //ignore
            }
        }
    }

    private void doGameUpdates(double delta) {
        for (int i = 0; i < stuffs.size(); i++) {
            // all time-related values must be multiplied by delta!
            Stuff s = stuffs.get(i);
            s.move(delta);
            //other updates
            float rx = controller.getAxisValue(OuyaController.AXIS_RS_X);
            float ry = controller.getAxisValue(OuyaController.AXIS_RS_Y);
            float deadzone = 0.25f;
            if (Math.abs(pointDistance(0, 0, rx, ry)) > deadzone) {
                if ((System.currentTimeMillis() - shootDelay) > lastShoot) {
                    lastShoot = System.currentTimeMillis();
                    synchronized (shootLock) {
                        if (shootAdd) {
                            stuffs.add(new Shoot(player.positionX + (player.width / 2) - (56 / 2), player.positionY + (player.height / 2) - (56 / 2), pointDirection(0, 0, rx, ry)));
                        } else {
                            //Delay a frame :C
                            lastShoot = 0;
                        }
                    }
                }
            }
        }
    }

    public static float pointDirection(float x1, float y1, float x2, float y2) {
        //http://wiki.yoyogames.com/index.php/Point_direction
        //radtodeg(arctan2(y1-y2,x2-x1));
        return (float) Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
    }

    public static float pointDistance(float x1, float y1, float x2, float y2) {
        //http://wiki.yoyogames.com/index.php/Point_distance
        //dist=sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1))
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public class Stuff {

        @Override
        public boolean equals(Object o) {
            if (o instanceof Stuff) {
                Stuff other = (Stuff) o;
                if (this.direction == other.direction
                        && this.positionX == other.positionX
                        && this.positionY == other.positionY) {
                    return true;
                }
            }
            return false;
        }

        float friction = 0;
        float velocity = 0;
        float positionX = 0;
        float positionY = 0;
        float direction = 0;
        int width = 0;
        int height = 0;
        Bitmap image;
        boolean outsideWrap = false;
        boolean limitAtEdge = false;

        public void drawMe(Canvas c, Paint p) {
            c.drawBitmap(image, positionX, positionY, p);
        }

        public void move(double delta) {
            //velocity -= Math.abs((float) (friction * delta));
            if (velocity < 0) { //limit
                velocity = 0;
            }
//            s.position += s.velocity * delta;
            positionY += (float) ((Math.sin((-direction) * (Math.PI / 180))) * velocity) * delta;
            positionX += (float) ((Math.cos((-direction) * (Math.PI / 180))) * velocity) * delta;

            if (outsideWrap && !limitAtEdge) {
                if (positionY > screen.height()) {
                    positionY = 1;
                }
                if (positionY < 0) {
                    positionY = screen.height() - 1;
                }
                if (positionX > screen.width()) {
                    positionX = 1;
                }
                if (positionX < 0) {
                    positionX = screen.width() - 1;
                }
            }

            if (limitAtEdge && !outsideWrap) {
                if (positionY > screen.height() - height) {
                    positionY = (screen.height() - height) - 1;
                }
                if (positionY < 0) {
                    positionY = 1;
                }
                if (positionX > screen.width() - width) {
                    positionX = (screen.width() - width) - 1;
                }
                if (positionX < 0) {
                    positionX = 1;
                }
            }
        }
    }


    public class Enemy extends Stuff {
        public Enemy() {
            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
            width = 64;
            height = 64;
            outsideWrap = true;
            Random r = new Random();
            boolean hor = r.nextBoolean();
            boolean top = r.nextBoolean();
            //appear at the edge of the screen
            if (hor) {
                positionX = r.nextInt(screen.width());
                if (top) {
                    positionY = -10;
                } else {
                    positionY = screen.height() + 10;
                }
            } else {
                positionY = r.nextInt(screen.height());
                if (top) {
                    positionX = -10;
                } else {
                    positionX = screen.width() + 10;
                }
            }
            velocity = 5;
            direction = pointDirection(positionX, positionY, player.positionX, player.positionY);
        }
    }

    public class Player extends Stuff {
        public Player() {
            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.android_small);
            width = 96;
            height = 96;
            positionX = screen.exactCenterX() - (width / 2);
            positionY = screen.exactCenterY() - (height / 2);
            limitAtEdge = true;
        }
    }

    public class Shoot extends Stuff {

        public Shoot(float x, float y, float direction) {
            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot);
            width = 56;
            height = 56;
            positionX = x;
            positionY = y;
            this.direction = direction;
            velocity = 15;
        }

        @Override
        public void move(double delta) {
            super.move(delta);
            if (positionX > screen.width() || positionX < 0
                    || positionY > screen.height() || positionY < 0) {
                stuffs.remove(this);
            }
        }
    }
}



