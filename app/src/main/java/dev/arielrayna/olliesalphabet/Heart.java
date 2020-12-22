package dev.arielrayna.olliesalphabet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

public class Heart implements GameObject {

    private int[] colors;
    private Paint paint;
    private RectF bounds;
    private PointF direction;
    private Path heartShape;

    private int minSize;
    private int maxSize;

    private int maxWidth;
    private int maxHeight;
    private float speedMultiplier;


    public Heart(RotatedPointF locationAndDirection, int maxWidth, int maxHeight, int minSize, int maxSize) {
        this.colors = generateColors();
        this.maxSize = maxSize;
        this.minSize = minSize;

        resetHeart(locationAndDirection);

        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;


    }

    private void resetHeart(RotatedPointF locationAndDirection) {
        this.speedMultiplier = 2;
        this.paint = randomHeartColor();
        this.heartShape = generateHeartShape();
        this.heartShape.offset(locationAndDirection.getPoint().x, locationAndDirection.getPoint().y);
        this.bounds = new RectF();
        heartShape.computeBounds(this.bounds, false);
        this.direction = locationAndDirection.getSlope();
    }

    private int[] generateColors() {
        return new int[] {
                Color.parseColor("#7209B7"),
                Color.parseColor("#8900F2"),
                Color.parseColor("#A100F2"),
                Color.parseColor("#B100E8"),
                Color.parseColor("#BC00DD"),
                Color.parseColor("#D100D1"),
                Color.parseColor("#DB00B6"),
                Color.parseColor("#B5179E"),
                Color.parseColor("#C577F9"),
                Color.parseColor("#460E95"),
                Color.parseColor("#DCADFF"),
                Color.parseColor("#440066"),
                Color.parseColor("#1F0029"),
                Color.parseColor("#DA0AFF"),
                Color.parseColor("#520052"),
                Color.parseColor("#FF0ABA"),

        };
    }

    private Paint randomHeartColor() {
        Random random = new Random();
        Paint paint = new Paint();
        paint.setColor(this.colors[random.nextInt(this.colors.length)]);
        return paint;
    }

    private Path generateHeartShape() {
        Random random = new Random();
        int size = random.nextInt((this.maxSize - this.minSize) + 1) + this.minSize;
        System.out.println(size);
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
        if (click != null && this.bounds.contains(click.x, click.y)) {
            return GameView.State.GENERATE_NEW;
        }

        this.bounds.offset(this.direction.x * this.speedMultiplier, this.direction.y * this.speedMultiplier);
        this.heartShape.offset(this.direction.x * 2, this.direction.y * 2);
        if (this.bounds.left > this.maxWidth || this.bounds.right < 0 || this.bounds.top > this.maxHeight || this.bounds.bottom < 0) {
            return GameView.State.GENERATE_NEW;
        }

        return GameView.State.SUCCESS;
    }

    @Override
    public void reset(RotatedPointF point) {
        resetHeart(point);
    }
}
