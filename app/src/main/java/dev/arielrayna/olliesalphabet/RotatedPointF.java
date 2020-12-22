package dev.arielrayna.olliesalphabet;

import android.graphics.PointF;

public class RotatedPointF {

    private PointF point;
    private PointF slope;

    public PointF getPoint() {
        return point;
    }

    public void setPoint(PointF point) {
        this.point = point;
    }

    public PointF getSlope() {
        return slope;
    }

    public void setSlope(PointF slope) {
        this.slope = slope;
    }

    public RotatedPointF(PointF point, PointF slope) {
        this.point = point;
        this.slope = slope;
    }
}
