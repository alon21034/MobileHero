package tw.edu.ntu.mobilehero.view;

import java.io.File;
import java.util.ArrayList;

import tw.edu.ntu.mobilehero.MultiTouchController;
import tw.edu.ntu.mobilehero.MultiTouchController.MultiTouchObjectCanvas;
import tw.edu.ntu.mobilehero.MultiTouchController.PointInfo;
import tw.edu.ntu.mobilehero.MultiTouchController.PositionAndScale;
import tw.edu.ntu.mobilehero.R;
import tw.edu.ntu.mobilehero.asynctask.MakeTextAsyncTask;
import tw.edu.ntu.mobilehero.view.PictureFiles.BitmapFile;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

public class DrawView extends View implements MultiTouchObjectCanvas<Scrap>{
    public static enum State{
        Picture,
        Drawing
    };
    
    Context mContext;
    ArrayList<Scrap> mScraps;
    private PointInfo currTouchPoint = new PointInfo();
    private MultiTouchController<Scrap> multiTouchController;
    private Path mPath;
    private Bitmap  mBitmap;
    public Canvas  mCanvas;
    
    private boolean isChanged = false;
    public static Paint paint = new Paint();
    private Paint   mBitmapPaint;
    
    private State state = State.Picture;
    private ImageScrap background;
    
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DrawView(Context context) {
        super(context);
        mContext = context;
        init();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	Log.d("CONSTRUCT", "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    
    private void init() {
        this.setBackgroundColor(Color.GRAY);
        mScraps = new ArrayList<Scrap>();
        multiTouchController = new MultiTouchController<Scrap>(this, ViewConfiguration.get(mContext));
        if(mPath == null) mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        paint = new Paint();
        paint.setStyle(Style.STROKE);
        invalidate();
    }

    public void setOnFlickListener(MultiTouchController.OnFlickListener<Scrap> l) {
        multiTouchController.setOnFlickListener(l);
    }
    
    public void addScrap() {
        BitmapFile bmpFile;
        try {
            bmpFile = PictureFiles.loadResourcePicture(mContext, R.drawable.ic_launcher);
            ImageScrap imageScrap = new ImageScrap(bmpFile.bmp, bmpFile.file);
            mScraps.add(imageScrap);
            
            Log.d("!!","add scrap");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void addScrap(int id) {
        BitmapFile bmpFile;
        try {
            bmpFile = PictureFiles.loadResourcePicture(mContext, id);
            ImageScrap imageScrap = new ImageScrap(bmpFile.bmp, bmpFile.file);
            mScraps.add(imageScrap);
            Log.d("!!","add scrap");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addScrap(Scrap scrap) {
        mScraps.add(scrap);
        setChanged(true);
    }
    
    public void addScrap(String filepath) {
        Log.d("!!","add scrap from path: " + filepath);
        try {
            ImageScrap imageScrap = new ImageScrap(PictureFiles.loadPrivatePicture(new File(filepath)), new File(filepath));
            mScraps.add(imageScrap);
            Log.d("!!","add scrap");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addScrap(String filepath, boolean b) {
        try {
            ImageScrap imageScrap = new ImageScrap(PictureFiles.loadPrivatePicture(new File(filepath)), new File(filepath));
//            mScraps.add(imageScrap);
            setBackground(imageScrap);
            invalidate();
            
            Log.d("!!","add scrap");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addText(String str) {
        MakeTextAsyncTask mAsyncTask = new MakeTextAsyncTask(this);
        mAsyncTask.execute(str);
    }
    
    public synchronized void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
        if (isChanged)
            postInvalidate();
    }
    
    public boolean onTouchEvent(MotionEvent event) {
        if(state == State.Picture) {
            return multiTouchController.onTouchEvent(event);
        } else {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    touch_down(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            
            return true;
        }
    }
    
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_down(float x, float y) {
        Log.d("!!","touch down");
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    
    private void touch_move(float x, float y) {
        Log.d("!!","touch move " + x + "  " + y + "  " + mX +"  " +  mY + " ");
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    
    private void touch_up() {
        Log.d("!!","touch up");
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, paint);
        mPath.reset();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        for (Scrap scrap : mScraps) {
            scrap.draw(canvas);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, paint);
    }
    
    @Override
    public Scrap getDraggableObjectAtPoint(PointInfo pt) {
        return getDraggableObjectAtPoint(pt.getX()+100, pt.getY()+70);
    }

    public Scrap getDraggableObjectAtPoint(float x, float y) {
        for (int i = mScraps.size() - 1; i >= 0; i--) {
            Scrap scrap = mScraps.get(i);
            if (scrap.containsPoint(x+80, y+80) && scrap.isSelectable())
                return scrap;
        }
        return null;
    }

    
    public void getPositionAndScale(Scrap scrap, PositionAndScale objPosAndScaleOut) {
        float sc = scrap.getScale();
        objPosAndScaleOut.set(scrap.getCenterX(), scrap.getCenterY(), true, sc,
                false, sc, sc, true, scrap.getAngle());
    }

    /**
     * Set the position and scale of the dragged/stretched image.
     */
    public void setPositionAndScale(Scrap scrap,
                                    PositionAndScale newImgPosAndScale, PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        scrap.setPos(newImgPosAndScale);
        setChanged(true);
    }

    @Override
    public void selectObject(Scrap obj, PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    public void setColor(int color) {
        paint.setColor(color);
    }
    
    public void setStrokeWidth(int n) {
        paint.setStrokeWidth(n);
    }
    
    public Paint getPaint() {
        return paint;
    }
    
    public synchronized void setBackground(ImageScrap img) {
        // User changing background
        // Resize the bundled background to fit the screen
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        int feedback = 0;
        try {
            Bitmap origBitmap = img.getDrawable().getBitmap();
            Bitmap resizedBitmap = ThumbnailUtils.extractThumbnail(origBitmap, screenWidth, screenHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            PictureFiles.BitmapFile bmpf = PictureFiles.loadMemoryPicture(resizedBitmap);
            img.delete();
            img = new ImageScrap(bmpf.bmp, bmpf.file);
            doSetBackground(img);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (PictureFiles.Exception e) {
            e.printStackTrace();
        }

        doSetBackground(img);
        setChanged(true);
    }
    
    public synchronized void doSetBackground(ImageScrap img) {
        if (img == background && img != null)
            return;
        if (background != null)
            background.delete();
        background = img;
        if (background != null) {
            setBackgroundDrawable(background.getDrawable());
        } else if (mScraps.size() == 0) {
        } else {
        }
    }
}