package dev.arielrayna.olliesalphabet;

import android.graphics.Canvas;
import android.graphics.PointF;

public interface GameObject {
    void draw(Canvas canvas);
    GameView.State update(PointF click);
    void reset(RotatedPointF point);
}
