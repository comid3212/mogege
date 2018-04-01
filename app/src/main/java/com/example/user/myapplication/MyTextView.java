package com.example.user.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by USER on 2018/1/17.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
    }
    public boolean top = true, left = true, right = true, down = true;
    private int drawColor = Color.BLACK;
    private float sroke_width = 1;

    public void setStrokeWidth(float width) {
        sroke_width = width;
    }

    public void setDrawColor(int color) {
        drawColor = color;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        //  将边框设为黑色
        paint.setColor(drawColor);
        paint.setStrokeWidth(sroke_width);
        //  画TextView的4个边
        if(top)
            canvas.drawLine(0, 0, this.getWidth() - sroke_width, 0, paint); //TOP
        if(left)
            canvas.drawLine(0, 0, 0, this.getHeight() - sroke_width, paint);//LEFT
        if(right)
            canvas.drawLine(this.getWidth() - sroke_width, 0, this.getWidth() - sroke_width, this.getHeight() - sroke_width, paint); // RIGHT
        if(down)
            canvas.drawLine(0, this.getHeight() - sroke_width, this.getWidth() - sroke_width, this.getHeight() - sroke_width, paint); //DOWN
        super.onDraw(canvas);
    }
}
