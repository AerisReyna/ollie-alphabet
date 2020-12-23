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

//    "Add a boolean for letter game vs heart game. Make two functions for setting defaults on all these variables based on the game type."

    private boolean isHeartGame = true;
    private char[] iLoveYou = {
            'I', 'L', 'O', 'V', 'E', 'Y', 'O', 'U'
    };
    private int currentPosition = 0;
    private int maxHeartSize;
    private int minHeartSize;

    private int maxLetters;
    private int maxShapes;
    private char currentLetter;
    private boolean isCurrentLetterNeeded;
    private BarrierBox barrierBox;
    private int padding = 30;
    private Paint defaultPaint;
    private Paint currentPaint;
    private float defaultTextSize;
    private float textSizeModifier;
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

        this.click = null;
        if (isHeartGame) {
            generateHeartGameDefaults();
        } else {
            generateLetterGameDefaults();
        }

        generateGameObjects();

    }

    private void generateLetterGameDefaults() {
        this.maxLetters = 10;
        this.maxShapes = 0;
        this.currentLetter = 'A';
        this.isCurrentLetterNeeded = true;
        this.defaultTextSize = 180;
        this.textSizeModifier = 1.5f;
        this.backgroundColor = Color.parseColor("#8B44EE");
        this.defaultColor = Color.argb(200, 70, 87, 117);
        this.currentColor = Color.argb(255, 86, 227, 159);
        this.defaultPaint = defaultPaint();
        this.currentPaint = currentLetterPaint();
    }

    private void generateHeartGameDefaults() {
        this.minHeartSize = 30;
        this.maxHeartSize = 100;
        this.maxLetters = 1;
        this.maxShapes = 50;
        this.currentLetter = this.iLoveYou[this.currentPosition];
        this.isCurrentLetterNeeded = true;
        this.defaultTextSize = 300;
        this.textSizeModifier = 1.5f;

        this.backgroundColor = Color.parseColor("#660049");
        this.defaultColor = Color.BLACK;
        this.currentColor = Color.WHITE;
        this.defaultPaint = null;
        this.currentPaint = heartPaint();
    }

    private Paint heartPaint() {
        Paint paint = new Paint();
        paint.setTextSize(this.defaultTextSize * this.textSizeModifier);
        paint.setColor(this.currentColor);
        return paint;
    }

    private Paint currentLetterPaint() {
        Paint currentPaint = new Paint();
        currentPaint.setTextSize(this.defaultTextSize * this.textSizeModifier);
        currentPaint.setColor(this.currentColor);
        currentPaint.setTextAlign(Paint.Align.LEFT);
        return currentPaint;
    }

    private Paint defaultPaint() {
        Paint paint = new Paint();
        paint.setTextSize(this.defaultTextSize);
        paint.setColor(this.defaultColor);
        paint.setTextAlign(Paint.Align.LEFT);
        return paint;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();
    }

    private void generateGameObjects() {
        gameObjects.clear();

        if (isCurrentLetterNeeded) {
            generateCurrentLetter();
        }

        while (gameObjects.size() < maxLetters) {
            gameObjects.add(generateRandomLetter());
        }

        if (this.isHeartGame) {
            while (gameObjects.size() - maxLetters < maxShapes) {
                gameObjects.add(generateRandomHeart());
            }
        } else {
            while (gameObjects.size() - maxLetters < maxShapes) {
                gameObjects.add(new Shape());
            }
        }
    }

    private GameObject generateRandomHeart() {
        return new Heart(barrierBox.getRandom(), this.maxWidth, this.maxHeight, this.minHeartSize, this.maxHeartSize);
    }

    private Letter generateRandomLetter() {
        return new Letter(defaultPaint, barrierBox.getRandom(), currentLetter, isCurrentLetterNeeded, maxHeight, maxWidth);
    }

    private void generateCurrentLetter() {
        this.gameObjects.add(new Letter(currentPaint, barrierBox.getRandom(), currentLetter, isCurrentLetterNeeded, maxHeight, maxWidth));
        this.isCurrentLetterNeeded = false;
    }

    private void nextLetter() {
        if (this.isHeartGame) {
            this.currentPosition += 1;
            if (this.currentPosition >= this.iLoveYou.length) {
                this.currentPosition = 0;
            }
            this.currentLetter = this.iLoveYou[this.currentPosition];
            this.isCurrentLetterNeeded = true;
        } else {
            this.currentLetter += 1;
            this.isCurrentLetterNeeded = true;
            if (this.currentLetter > 'Z') {
                this.currentLetter = 'A';
            }
        }
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
