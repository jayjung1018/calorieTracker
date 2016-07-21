package edu.upenn.cis350.calorietracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JayJung on 2/26/2016.
 * Custom view page for displaying calories
 */
public class CalorieView extends View {
    int calories = 2000;
    int food = 0;
    int exercise = 0;
    int goal = 2000;

    /**
     * Contructors for custom views - defaults
     */
    public CalorieView(Context c) {
        super(c);
    }

    public CalorieView(Context c, AttributeSet a) {
        super(c, a);
    }

    public CalorieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * The ondraw method for this view
     */
    public void onDraw(Canvas canvas) {
        //set up bound rectangles
        float width = canvas.getWidth();
        Rect bounds1 = new Rect();
        Rect bounds2 = new Rect();
        Rect bounds3 = new Rect();

        //set up paint
        Paint p = new Paint();
        p.setStrokeWidth(5);
        p.setTextSize(40);

        //change integer fields to strings
        String goalText = Integer.toString(goal);
        String foodText = Integer.toString(food);
        String remainText = Integer.toString(calories);
        String exerciseText = Integer.toString(exercise);

        //goal
        p.getTextBounds(goalText, 0, goalText.length(), bounds1);
        canvas.drawText(goalText, 0, bounds1.height(), p);

        //minus sign
        canvas.drawText("-", bounds1.width() + ((width / 4) - bounds1.width()) / 2, bounds1.height(), p);

        //today's food
        p.getTextBounds(foodText, 0, foodText.length(), bounds2);
        canvas.drawText(foodText, width / 4, bounds2.height(), p);

        //plus sign
        canvas.drawText("+", (width / 4) + (((2 * width / 4) - (width / 4) + bounds2.width()) / 2), bounds1.height(), p);

        //today's exercise
        p.getTextBounds(exerciseText,0,exerciseText.length(),bounds2);
        canvas.drawText(exerciseText, 2 * width / 4, bounds2.height(),p);

        //equals sign
        canvas.drawText("=",(2 * width / 4) + (((3 * width / 4) - (2 * width / 4) + bounds2.width()) / 2), bounds1.height(), p);

        //remaining
        canvas.drawText(remainText, 4 * width / 5, bounds1.height(), p);

        //labels
        float margin = bounds1.height() + 10;
        p.setTextSize(20);
        p.getTextBounds("0", 0, 1, bounds3);
        canvas.drawText("Goal", 0, 4, 0, bounds3.height() + margin, p);
        canvas.drawText("Food", 0, 4, width / 4, margin + bounds3.height(), p);
        canvas.drawText("Exercise", 0, 8, 2 * width / 4, margin + bounds3.height(),p);
        canvas.drawText("Remaining", 0, 9, 4 * width / 5, margin + bounds3.height(), p);


    }

    /**
     * Default method to set dimensions for this view
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(
                parentWidth, parentHeight / 8);
    }
}
