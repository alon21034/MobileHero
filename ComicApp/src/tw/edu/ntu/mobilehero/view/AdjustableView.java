package tw.edu.ntu.mobilehero.view;

import tw.edu.ntu.mobilehero.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AdjustableView extends View {
	private Drawable mDrawable;
	
	private float mTranslationX = 0;
	private float mTranslationY = 0;
	private float mScaling = 1.f;
	private float mRotation = 0;
	
	double left;
	double top;
	double right;
	double bottom;
	
	public AdjustableView(Context context, Integer id) {
		super(context);
		
		mDrawable = getResources().getDrawable(R.drawable.ic_launcher);
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
	}
	
	public AdjustableView(Context context, AttributeSet attrs, Integer id) { 
		super(context, attrs);

		mDrawable = getResources().getDrawable(id);
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
	}

	public AdjustableView(Context context, AttributeSet attrs, int defStyle, Integer id) { 
		super(context, attrs, defStyle);
		
		mDrawable = getResources().getDrawable(id);
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
	}
	
	@Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mTranslationX, mTranslationY);
        canvas.scale(mScaling, mScaling, 
        		mDrawable.getIntrinsicWidth() / 2, 
        		mDrawable.getIntrinsicHeight() / 2);
        canvas.rotate(mRotation, 
        		mDrawable.getIntrinsicWidth() / 2, 
        		mDrawable.getIntrinsicHeight() / 2);
        mDrawable.draw(canvas);
        canvas.restore();
    }

    public Boolean containsTouchPoint(float currX, float currY) {
    	double radians = -1.f * Math.toRadians(mRotation);
    	double pivotX = mTranslationX + mDrawable.getIntrinsicWidth() / 2;
    	double pivotY = mTranslationY + mDrawable.getIntrinsicHeight() / 2;
    	double vectorX = currX - pivotX;
    	double vectorY = currY - pivotY;
    	double newCurrX = vectorX * Math.cos(radians) - vectorY * Math.sin(radians) + pivotX;
    	double newCurrY = vectorX * Math.sin(radians) +  vectorY * Math.cos(radians) + pivotY;
    	
    	Log.d("tx", String.valueOf(mTranslationX));
    	Log.d("ty", String.valueOf(mTranslationY));
    	left = mTranslationX - mDrawable.getIntrinsicWidth() * mScaling / 2;
    	top = mTranslationY - mDrawable.getIntrinsicHeight() * mScaling / 2;
    	right = left + mDrawable.getIntrinsicWidth() * mScaling;
    	bottom = top + mDrawable.getIntrinsicWidth() * mScaling;
    	
    	return (newCurrX >= left && newCurrX <= right &&
    			newCurrY >= top && newCurrY <= bottom);
    }
    
    public void translate(float offsetX, float offsetY) {
    	mTranslationX += offsetX;
    	mTranslationY += offsetY;
    }
    
    public void scale(float scaleFactor) {
    	mScaling *= scaleFactor;
    }
    
    public void rotate(float rotationFactor) {
    	mRotation += rotationFactor;
    }
}