package dev.arielrayna.olliesalphabet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public enum State {
        GENERATE_NEW, ADVANCE, SUCCESS
    }

    private GameThread gameThread;
    private ArrayList<GameObject> gameObjects;

    "Add a boolean for letter game vs heart game. Make two functions for setting defaults on all these variables based on the game type."

    private int maxLetters = 10;
    private int maxShapes = 0;
    private char currentLetter = 'A';
    private boolean isCurrentLetterNeeded = true;
    private BarrierBox barrierBox;
    private int padding = 30;
    private Paint defaultPaint;
    private Paint currentPaint;
    private float defaultTextSize = 80;
    private float textSizeModifier = 1.5f;
    private int defaultColor;
    private int currentColor;
    private int backgroundColor;
    private int maxWidth;
    private int maxHeight;
    private PointF click;

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        gameThread = new GameThread(getHolder(), this);
        setFocusable(true);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        this.maxWidth = metrics.widthPixels;
        this.maxHeight = metrics.heightPixels;

        this.gameObjects = new ArrayList<>();

        this.barrierBox = new BarrierBox(context, padding);

        this.backgroundColor = Color.argb(255, 239, 111, 108);
        this.defaultColor = Color.argb(180, 70, 87, 117);
        this.currentColor = Color.argb(255, 86, 227, 159);
        this.defaultPaint = defaultPaint();
        this.currentPaint = currentLetterPaint();
        this.click = null;
    }

    private Paint currentLetterPaint() {
        currentPaint = new Paint();
        currentPaint.setTextSize(defaultTextSize * textSizeModifier);
        currentPaint.setColor(this.currentColor);
        return currentPaint;
    }

    private Paint defaultPaint() {
        defaultPaint = new Paint();
        defaultPaint.setTextSize(defaultTextSize);
        defaultPaint.setColor(this.defaultColor);
        return defaultPaint;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();
        generateGameObjects();
    }

    private void generateGameObjects() {
        gameObjects.clear();

        if (isCurrentLetterNeeded) {
            generateCurrentLetter();
        }

        while (gameObjects.size() < maxLetters) {
            gameObjects.add(generateRandomLetter());
        }

        while (gameObjects.size() - maxLetters <= maxShapes) {
            gameObjects.add(new Shape());
        }
    }

    private Letter generateRandomLetter() {
        return new Letter(defaultPaint, barrierBox.getRandom(), currentLetter, isCurrentLetterNeeded, maxHeight, maxWidth);
    }

    private void generateCurrentLetter() {
        this.gameObjects.add(new Letter(currentPaint, barrierBox.getRandom(), currentLetter, isCurrentLetterNeeded, maxHeight, maxWidth));
        this.isCurrentLetterNeeded = false;
    }

    private void nextLetter() {
        if (this.currentLetter < 'Z') {
            this.currentLetter += 1;
            this.isCurrentLetterNeeded = true;
        } else {
            gameOver();
        }
    }

    private void gameOver() {
        this.currentLetter = 'A';
        this.isCurrentLetterNeeded = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.click = new PointF(event.getX(), event.getY());
            return true;
        }
        return false;
    }




    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.setRunning(false);
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {

        for (GameObject gameObject : gameObjects) {
            State state = gameObject.update(click);
            if (state == State.ADVANCE) {
                // I'm going to call a break here, but this might cause it to break, because I intend to clear the object that's calling this function.
                advanceGame();
                break;
            } else if (state == State.GENERATE_NEW) {
                gameObject.reset(barrierBox.getRandom());
            }
        }
    }

    private void advanceGame() {
        nextLetter();
        generateGameObjects();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            canvas.drawColor(this.backgroundColor);
            for (GameObject gameObject : gameObjects) {
                gameObject.draw(canvas);
            }
        }
    }
}
