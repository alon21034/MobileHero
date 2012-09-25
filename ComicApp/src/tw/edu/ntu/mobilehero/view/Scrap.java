package tw.edu.ntu.mobilehero.view;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import tw.edu.ntu.mobilehero.MultiTouchController.PositionAndScale;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.FloatMath;
import android.util.Log;

public abstract class Scrap {

    private static final float SCREEN_MARGIN = 100;

    private float mOrigX, mOrigY;
    protected int mWidth, mHeight;
    protected float mCenterX, mCenterY, mScale = 1, mAngle;

    // These matrices store transformations from screen space to image
    // space (for input handling) and its inverse (for rendering). Scrap
    // space has its origin at the center of the image so its extents
    // are half-width/half-height on both axes.
    private Matrix mScreenToScrap;
    private Matrix mScrapToScreen = new Matrix();

    // These matrices store transformations to be applied immediately
    // before drawing. It is used to animate the image without changing
    // the underlying real transformation.
    private Matrix mTransientPreTransformation;
    private Matrix mTransientPostTransformation;

    // if in trash can, it's not selectable
    protected boolean isSelectable = true;

    private long mId;

    /*
     * Abstract methods which must be implemented by subclasses
     *
     */
    protected abstract void calculateMetrics();
    public abstract void setAlpha(int alpha);
    public abstract int getMinimumWidth();
    public abstract int getMinimumHeight();

    private static float randomBetween(float low, float high) {
        return low + (float) Math.random() * (high - low);
    }


    public static Scrap newInstance(Class<?> scrapClass, Context context,
                                    ContentValues values)
            throws IllegalArgumentException{
        try {
            Constructor<?> c = scrapClass.getConstructor(Context.class, ContentValues.class);
            return (Scrap) c.newInstance(context, values);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Not enough data in ContentValues");
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Required constructor for scraps that are loaded from db
     *
     * @param values
     *
     * @throws NullPointerException
     *         when an expected value in ContentValues is missing or cannot be converted.
     */

    protected void setInitialPos() {
        // Scale so the image takes up 50% of either screen dimension
        // in either orientation.
        //(TODO) need to change init point
        float scale = Math.min(480,640)
                / (2.f * Math.max(mWidth, mHeight));

        // Randomly place the image so that it is on-screen (barring
        // slight clipping from the random rotation).
        float xEdge = SCREEN_MARGIN + scale * mWidth / 2;
        float yEdge = SCREEN_MARGIN + scale * mHeight / 2;
        float cx = randomBetween(xEdge, 480 - xEdge);
        float cy = randomBetween(yEdge, 480 - yEdge);

        // Randomly rotate the image from -30 to 30 degrees.
        final float range = (float) Math.PI / 6;
        float an = randomBetween(-range, range);

        setPos(500, 400, scale, an);
    }

    /**
     * Make Scrap to center in screen if it's outside.
     * Should be called after activities in which the Scrap size or visible area might change;
     * for example, after TextScrap changes and Clipping.
     */
    public void makeOnScreen() {
        Log.d("!!","!! make on screen") ;
        if (mCenterX<0 || mCenterX>640 || mCenterY<0 || mCenterY>480) {
            mCenterX = 300;
            mCenterY = 300;
            updateTransformations();
        }
    }
    
    /**
     * Set the position and scale of an image in screen coordinates
     */
    public void setPos(PositionAndScale newImgPosAndScale) {
        setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(),
                newImgPosAndScale.getScale(), newImgPosAndScale.getAngle());
    }

    /**
     * Set the position and scale of an image in screen coordinates.
     */

    public void setPos(float centerX, float centerY, float scale,
            float angle) {
        mCenterX = centerX;
        mCenterY = centerY;
        mScale = scale;
        mAngle = angle;

        updateTransformations();
    }

    public void setTransientTransformations(Matrix pre, Matrix post) {
        mTransientPreTransformation = pre;
        mTransientPostTransformation = post;
    }

    protected void updateTransformations() {
        mScreenToScrap = new Matrix();
        float degrees = mAngle * 180.0f / (float) Math.PI;
        mScreenToScrap.postTranslate(-mCenterX, -mCenterY);
        mScreenToScrap.postRotate(-degrees);
        mScreenToScrap.postScale(1.f / mScale, 1.f / mScale);

        mScreenToScrap.invert(mScrapToScreen);
    }

    /**
     * Return whether or not the given screen coords are inside this image
     */
    public boolean containsPoint(float screenX, float screenY) {
        float[] screenPoint = new float[] { screenX, screenY };
        float[] modelPoint = new float[] { screenX, screenY };
        mScreenToScrap.mapPoints(modelPoint, screenPoint);

        float modelX = modelPoint[0];
        float modelY = modelPoint[1];
        return modelX >= -mWidth / 2.f && modelX < mWidth / 2.f
                && modelY >= -mHeight / 2.f && modelY < mHeight / 2.f;
    }

    public float distance(Scrap that) {
        // Give the approximate distance between the two images.
        // It just does a radius check using the larger of the two
        // dimensions. It's not 100% accurate but it's only used
        // to drive some animations so it doesn't matter.
        float thisRadius = Math.max(mWidth, mHeight) * mScale / 2;
        float thatRadius = Math.max(that.mWidth, that.mHeight) * that.mScale / 2;
        float radius = thisRadius + thatRadius;
        float dx = this.mCenterX - that.mCenterX;
        float dy = this.mCenterY - that.mCenterY;
        return (float)FloatMath.sqrt(dx * dx + dy * dy) - radius;
    }

    public void draw(Canvas canvas) {
        // TODO(jfw): If slow, this can be cached.
        Log.d("!!","scrap draw");   
        Matrix m = new Matrix(mScrapToScreen);

        if (mTransientPostTransformation != null)
            m.postConcat(mTransientPostTransformation);
        if (mTransientPreTransformation != null)
            m.preConcat(mTransientPreTransformation);

        canvas.translate(100, 20);
        canvas.setMatrix(m);
    }

    public float getCenterX() {
        return mCenterX;
    }

    public float getCenterY() {
        return mCenterY;
    }

    public float getOrigX() {
        return mOrigX;
    }

    public float getOrigY() {
        return mOrigY;
    }

    public void setOrigX(float n) {
        mOrigX = n;
    }

    public void setOrigY(float n) {
        mOrigY = n;
    }

    public float getScale() {
        return mScale;
    }

    public float getAngle() {
        return mAngle;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getId() {
        return mId;
    }
}
