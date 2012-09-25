package tw.edu.ntu.mobilehero.view;

import java.io.File;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

public class ImageScrap extends Scrap {

    private File mPath;
    private BitmapDrawable mDrawable;

    public ImageScrap(BitmapDrawable bd, File path) {
        assert (bd != null);
        // Antialiasing causes artifacts on some phones, particularly
        // after opening images in Aviary. No one knows why. Instead
        // our paint is antialiased and drawn on top of the image,
        // covering its edges slightly.
        bd.setAntiAlias(false);
        this.mDrawable = bd;
        this.mPath = path;

        calculateMetrics();
        this.setInitialPos();
    }

    @Override
    protected void calculateMetrics() {
        mWidth = mDrawable.getMinimumWidth();
        mHeight = mDrawable.getMinimumHeight();
        mDrawable.setBounds(-mWidth / 2, -mHeight / 2, mWidth / 2, mHeight / 2);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mDrawable.draw(canvas);
    }

    public File getPath() {
        return mPath;
    }

    public Uri getUri() {
        return Uri.fromFile(mPath);
    }

    public BitmapDrawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(BitmapDrawable drawable) {
        mDrawable = drawable;
        calculateMetrics();
        updateTransformations();
    }

    @Override
    public void setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
    }

    public void delete() {
        if (mPath != null)
            mPath.delete();
    }

    @Override
    public int getMinimumWidth() {
        return mDrawable.getMinimumWidth();
    }

    @Override
    public int getMinimumHeight() {
        return mDrawable.getMinimumHeight();
    }
}

