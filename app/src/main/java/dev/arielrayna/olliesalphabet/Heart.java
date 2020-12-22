package dev.arielrayna.olliesalphabet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

public class Heart implements GameObject {

    private Paint paint;
    private RectF bounds;
    private PointF direction;
    private Path heartShape;

    private int minSize = 5;
    private int maxSize = 30;
    private int color;

    private int maxWidth;
    private int maxHeight;


    public Heart(Paint paint, RotatedPointF locationAndDirection, int maxWidth, int maxHeight) {
        this.paint = paint;

        this.heartShape = generateHeartShape();
        this.heartShape.offset(locationAndDirection.getPoint().x, locationAndDirection.getPoint().y);
        heartShape.computeBounds(this.bounds, false);
        this.direction = locationAndDirection.getSlope();
        this.color = randomColor();

        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;

    }

    private int randomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private Path generateHeartShape() {
        Random random = new Random();
        int size = random.nextInt((((this.maxSize - this.minSize) + 1) - this.minSize));
        Path heart = new Path();
        heart.moveTo(0, size / 4.0f);
        heart.lineTo(0, size / 2.0f);
        heart.lineTo(size / 2.0f, size);
        heart.lineTo(size, size / 2.0f);
        heart.lineTo(size, size / 4.0f);
        heart.lineTo(size * .75f, 0);
        heart.lineTo(size / 2.0f, size / 4.0f);
        heart.lineTo(size / 4.0f, 0);
        heart.close();
        return heart;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(this.heartShape, this.paint);
    }

    @Override
    public GameView.State update(PointF click) {
        "check if the heart has been clicked, reset with new color if it has. Do the same if out of bounds. Recalculate color too. Recalculate bounds on update"
    }

    @Override
    public void reset(RotatedPointF point) {
        "Reset size, color, bounds, direction"
    }
}
