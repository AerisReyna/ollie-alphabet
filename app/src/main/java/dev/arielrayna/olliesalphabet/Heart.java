package dev.arielrayna.olliesalphabet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Heart implements GameObject{

    private Paint paint;
    private RectF bounds;
    private PointF direction;

    private int minSize = 5;
    private int maxSize = 30;
    private int color;

    public Heart(Paint paint, RotatedPointF locationAndDirection) {
        this.paint = paint;


    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public GameView.State update(PointF click) {
        return null;
    }

    @Override
    public void reset(RotatedPointF point) {

    }
}
