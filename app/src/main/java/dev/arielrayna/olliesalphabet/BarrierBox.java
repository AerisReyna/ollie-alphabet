package dev.arielrayna.olliesalphabet;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import java.util.Random;

public class BarrierBox {
    private int padding;
    private int width;
    private int height;
    private Random numberGen;

    public BarrierBox(Context context, int padding) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        this.width = metrics.widthPixels;
        this.height = metrics.heightPixels;
        this.padding = padding;

        numberGen = new Random();
    }

    public RotatedPointF getRandom() {
        int random = numberGen.nextInt(width * 2 + height * 2 + padding * 8);
        int x = 0;
        int y = 0;
        int angle;

        // Going clockwise.
        if (random < width + height + padding * 4) {
            if (random > width + padding * 2) { //Side 2
                System.out.println("Side 2");
                y = random - (width + padding * 2);
                x = width - padding * 2;
                angle = randomAngle(105, 255);
            } else { // Side 1
                System.out.println("Side 1");
                x = random;
                angle = randomAngle(15, 165);
            }
        } else {
            random = random - (width + height + padding * 4);
            if (random > width + padding * 2) { // Side 4
                System.out.println("Side 4");
                y = random - (width + padding * 2);
                angle = randomAngle(285, 435);
            } else { // Side 3
                System.out.println("Side 3");
                x = random;
                y = height - padding * 2;
                angle = randomAngle(195, 345);
            }
        }
        float slopeX = (float) Math.cos(angle);
        float slopeY = (float) Math.sin(angle);
        System.out.println(x + " " + y + " " + slopeX + " " + slopeY);
        return new RotatedPointF(new PointF(x, y), new PointF((float) Math.cos(Math.toRadians(angle)), ((float) Math.sin(Math.toRadians(angle)))));
    }

    private int randomAngle(int min, int max) {
        return numberGen.nextInt((max - min) + 1) + min;
    }
}
