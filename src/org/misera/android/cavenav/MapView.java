package org.misera.android.cavenav;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class MapView extends View {

	private Bitmap pic;
    private Rect screen = new Rect();
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
	private float mLastTouchY;
	private float mLastTouchX;
	private float mPosX;
	private float mPosY;
	private float angle = 0.f;
    
	public MapView(Context context, Bitmap pic, SensorManager mSensorManager) {
		super(context);
		this.pic = pic;
			
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screen.bottom = dm.heightPixels;
		screen.right = dm.widthPixels;

	    mPosX = screen.right/2;
	    mPosY = screen.bottom/2;
	    
	    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}  
	
	@Override
    public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	    canvas.save();
	    /*
	    float centerX = screen.right/2;
	    float centerY = screen.bottom/2;
	    
        canvas.translate(centerX, centerY);
        canvas.rotate(-degrees + 180);
	    canvas.scale(mScaleFactor, mScaleFactor);
        canvas.translate(-centerX, -centerY);
        canvas.translate(mPosX, mPosY);

		canvas.drawBitmap(pic, null, screen, null);
		
	    Paint paint = new Paint();
	    paint.setColor(Color.RED);
	    canvas.drawCircle(centerX, centerY, 3, paint);
	    paint.setColor(Color.GREEN);
	    canvas.drawCircle(mPosX, mPosY, 3, paint);
        */
		
	    
	    Matrix m = new Matrix();
	    float centerX = screen.right/2;
	    float centerY = screen.bottom/2;
	    m.preScale(mScaleFactor, mScaleFactor, centerX, centerY);
	    m.preRotate(angle, centerX, centerY);
	    
	    float x = mPosX / mScaleFactor;
	    float y = mPosY / mScaleFactor;
	    m.preTranslate(x, y);
        canvas.drawBitmap(pic, m, null);
		
	    /*
	    float centerX = screen.right/2;
	    float centerY = screen.bottom/2;
	    double rad = Math.toRadians(angle);
	    Matrix mt = new Matrix();
	    mt.setTranslate(-centerX, -centerY);
	    Matrix m = new Matrix();
	    float[] values = {
	    		(float) (mScaleFactor*Math.cos(rad)), (float) -Math.sin(rad), centerX,
	    		(float) Math.sin(rad), (float) (mScaleFactor*Math.cos(rad)), centerY,
	    		0, 0, 1
	    };
	    m.setValues(values);
	    Matrix mtb = new Matrix();
	    mtb.setTranslate(centerX, centerY);
	    
	    mt.preConcat(m);
	    mt.preConcat(mtb);
	    
        canvas.drawBitmap(pic, mt, null);
        */
        
	    Paint paint = new Paint();
	    paint.setColor(Color.RED);
	    canvas.drawCircle(centerX, centerY, 1, paint);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawCircle(centerX, centerY, 3, paint);
	    String debug = "angle: " + angle + "�, zoom: " + mScaleFactor;
	    canvas.drawText(debug, 10, 10, paint);
        
	    canvas.restore();
    }
	
	public boolean onTouchEvent(MotionEvent  ev) {
		mScaleDetector.onTouchEvent(ev);
		final int action = ev.getAction();
		switch (action) {
		    case MotionEvent.ACTION_DOWN: {
		        final float x = ev.getX();
		        final float y = ev.getY();
		        
		        // Remember where we started
		        mLastTouchX = x;
		        mLastTouchY = y;
		        break;
		    }
		        
		    case MotionEvent.ACTION_MOVE: {
		        final float x = ev.getX();
		        final float y = ev.getY();
		        
		        // Calculate the distance moved
		        final float dx = x - mLastTouchX;
		        final float dy = y - mLastTouchY;
		        
		        // Move the object
		        mPosX += dx;
		        mPosY += dy;
		        
		        // Remember this touch position for the next move event
		        mLastTouchX = x;
		        mLastTouchY = y;
		        
		        // Invalidate to request a redraw
		        invalidate();
		        break;
		    }
	    }
		return true;
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        mScaleFactor *= detector.getScaleFactor();

	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

	        invalidate();
	        return true;
	    }
	}
	private final SensorEventListener mListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
        	float heading = event.values[0];
        	angle = (float) Math.floor(-heading+180);
            invalidate();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
