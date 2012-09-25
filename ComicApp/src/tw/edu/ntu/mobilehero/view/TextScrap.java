package tw.edu.ntu.mobilehero.view;

import android.content.Context;
import android.graphics.Canvas;

public class TextScrap extends Scrap {

    TextDrawer mTextDrawer;
    /**
     * Init a new TextScrap based on ContentValues
     *
     * @param context: is required in this class to init TextDrawer.
     * @param values: ContentValues from DB.
     *
     * @throws PictureFiles.Exception
     *         when drawable cannot be loaded using specified path
     * @throws NullPointerException
     *         when an expected value in ContentValues is missing or cannot be converted.
     */

    public TextScrap(Context context, String text_string, String text_font,
            int text_color, int text_outline_color) {

        mTextDrawer = new TextDrawer(context, text_string,
                text_font, text_color, text_outline_color, true);

        calculateMetrics();
        this.setInitialPos();
    }


    public String getTextString() {
        return mTextDrawer.getString();
    }

    public String getTextFont() {
        return mTextDrawer.getFont();
    }

    public int getTextColor() {
        return mTextDrawer.getColor();
    }

    public int getTextOutlineColor() {
        return mTextDrawer.getOutlineColor();
    }

    public void setText(DrawView photoProtoView, String text,
                        String text_font, int text_color, int text_outline_color) {
        mTextDrawer.setText(photoProtoView.getContext(), text, text_font,
                text_color, text_outline_color);

        calculateMetrics();
        updateTransformations();

        photoProtoView.setChanged(true);
    }

    @Override
    public void setAlpha(int alpha) {
        mTextDrawer.setAlpha(alpha);
    }

    @Override
    protected void calculateMetrics() {
        mWidth = mTextDrawer.getWidth();
        mHeight = mTextDrawer.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mTextDrawer.draw(canvas);
    }

    @Override
    public int getMinimumWidth() {
        mWidth = mTextDrawer.getWidth();
        return mWidth;
    }

    @Override
    public int getMinimumHeight() {
        mHeight = mTextDrawer.getHeight();
        return mHeight;
    }
}
