package dev.arielrayna.olliesalphabet;

import android.graphics.Canvas;
import android.graphics.PointF;

public class Shape implements GameObject {

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


    enum Shapes {
        SQUARE, CIRCLE, RECTANGLE, STAR, OVAL
    }
    public Shape() {
    }
}
