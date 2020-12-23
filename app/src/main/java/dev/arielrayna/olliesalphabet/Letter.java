package dev.arielrayna.olliesalphabet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;
import dev.arielrayna.olliesalphabet.GameView.State;

public class Letter implements GameObject {

    private PointF direction;
    private char letter;
    private Paint paint;
    private RectF bounds;
    private boolean isCurrentLetter;
    private char currentLetter;
    private int maxHeight;
    private int maxWidth;


    private float speedMultiplier;

    //    "THIS SHOULD HAVE THE PAINT PASSED IN INSTEAD OF THE BOUNDS. WHAT IT NEEDS IS THE ROTATEDPOINTF SO THAT IT KNOWS WHERE TO DRAW ITSELF AND HOW TO CHANGE IT'S DIRECTION ON EACH UPDATE"
//    "IF IT'S THE CURRENT LETTER, IT NEEDS TO GO TO A DIFFERENT FUNCTION THAT MAKES IT BIGGER, COLORED BRIGHTLY"
    public Letter(Paint paint, RotatedPointF locationAndDirection, char currentLetter, boolean isCurrentLetterNeeded, int height, int width) {
        this.paint = paint;
        this.speedMultiplier = 3.0f;
        this.isCurrentLetter = isCurrentLetterNeeded;
        this.currentLetter = currentLetter;
        if (isCurrentLetterNeeded) {
            this.letter = currentLetter;
        }
        this.maxHeight = height;
        this.maxWidth = width;

        this.reset(locationAndDirection);
    }

    private Paint backgroundPaintTester() {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        return paint;
    }

    private RectF getLetterBounds(char letter) {
        Rect bounds = new Rect();
        paint.getTextBounds(String.valueOf(letter), 0, 1, bounds);
        return new RectF(bounds);
    }

    private char getRandomLetter(char currentLetter) {
        Random randomGen = new Random();
        char letter = currentLetter;
        while (letter == currentLetter) {
            letter = (char) (randomGen.nextInt(('Z' - 'A') + 1) + 'A');
        }
        return letter;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.letter == 'I') {
            canvas.drawText(String.valueOf(this.letter), this.bounds.left - this.bounds.width(), this.bounds.bottom, paint);
        } else {
            canvas.drawText(String.valueOf(this.letter), this.bounds.left, this.bounds.bottom, paint);
        }

    }

    @Override
    public State update(PointF click) {
        if (click != null && this.bounds.contains(click.x, click.y)) {
            if (this.isCurrentLetter) {
                return State.ADVANCE;
            } else {
                return State.GENERATE_NEW;
            }
        }

        this.bounds.offset(this.direction.x * this.speedMultiplier, this.direction.y * this.speedMultiplier);
        if (this.bounds.left > this.maxWidth || this.bounds.right < 0 || this.bounds.top > this.maxHeight || this.bounds.bottom < 0) {
            return State.GENERATE_NEW;
        }
        return State.SUCCESS;
    }

    @Override
    public void reset(RotatedPointF point) {
        this.direction = point.getSlope();

        if (!isCurrentLetter) {
            this.letter = getRandomLetter(this.currentLetter);
        }

        this.bounds = getLetterBounds(this.letter);
        PointF location = point.getPoint();
        this.bounds.offsetTo(location.x, location.y);
    }
}
