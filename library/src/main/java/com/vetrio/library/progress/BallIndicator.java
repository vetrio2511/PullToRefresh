package com.vetrio.library.progress;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.animation.ValueAnimator;

import java.util.ArrayList;

public class BallIndicator extends Indicator {

    public static final float SCALE=1.0f;

    public static final int ALPHA=255;

    float[] scaleFloats=new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE};

    int[] alphas=new int[]{ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA};


    @Override
    public void draw(Canvas canvas, Paint paint) {
        float radius=getWidth()/10;
        float scale;
        int alpha;
        for (int i = 0; i < 8; i++) {
            canvas.save();
            Point point=circleAt(getWidth(),getHeight(),getWidth()/2-radius,i*(Math.PI/4));

            if(!isRefreshing) {
                if(i < percent * 8) {
                    scale = 1f;
                    alpha = 255;
                } else {
                    scale = 0.5f;
                    alpha = 126;
                }
            } else {
                scale = scaleFloats[i];
                alpha = alphas[i];
            }


            canvas.translate(point.x,point.y);
            canvas.scale(scale, scale);
            paint.setAlpha(alpha);
            canvas.drawCircle(0,0,radius,paint);
            canvas.restore();
        }
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators=new ArrayList<>();
        int[] delays= {0, 120, 240, 360, 480, 600, 720, 780, 840};
        for (int i = 0; i < 8; i++) {
            final int index=i;
            ValueAnimator scaleAnim=ValueAnimator.ofFloat(1,0.4f,1);
            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            addUpdateListener(scaleAnim,new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            ValueAnimator alphaAnim=ValueAnimator.ofInt(255, 77, 255);
            alphaAnim.setDuration(1000);
            alphaAnim.setRepeatCount(-1);
            alphaAnim.setStartDelay(delays[i]);
            addUpdateListener(alphaAnim,new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alphas[index] = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(scaleAnim);
            animators.add(alphaAnim);
        }
        return animators;
    }

    /**
     * 圆O的圆心为(a,b),半径为R,点A与到X轴的为角α.
     *则点A的坐标为(a+R*cosα,b+R*sinα)
     * @param width
     * @param height
     * @param radius
     * @param angle
     * @return
     */
    Point circleAt(int width,int height,float radius,double angle){
        float x= (float) (width/2+radius*(Math.cos(angle)));
        float y= (float) (height/2+radius*(Math.sin(angle)));
        return new Point(x,y);
    }

    final class Point{
        public float x;
        public float y;

        public Point(float x, float y){
            this.x=x;
            this.y=y;
        }
    }


}