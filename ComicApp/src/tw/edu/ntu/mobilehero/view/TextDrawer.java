package tw.edu.ntu.mobilehero.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.TypedValue;

public class TextDrawer {

    public static final int TEXT_SIZE = 40;

    private final boolean mDrawFromCenter;
    private final int textStrokeWidth;
    private final int textPadding;
    private final int textSize;

    private int mWidth, mHeight;

    private Path mPath;

    private String mString;
    private String mFont;

    private Paint mTextPaint;
    private Paint mStrokePaint;

    public static final String FONTS_PREFIX = "fonts/";

    public TextDrawer(Context context, String text, String font, int color,
            int outline_color, boolean drawFromCenter) {

        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                TEXT_SIZE, context.getResources().getDisplayMetrics());
        textPadding = textStrokeWidth = (int) (textSize/10.f);

        /*
         * initialization
         */
        mStrokePaint = new Paint();
        mStrokePaint.setTextSize(textSize);
        mStrokePaint.setTextAlign(Paint.Align.CENTER);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(textStrokeWidth);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);

        mDrawFromCenter = drawFromCenter;

        updatePaints(context, font, color, outline_color);
        setString(text);
    }

    public void draw(Canvas canvas) {

        // (judith) to avoid "ERROR/OpenGLRenderer: Font size to large to fit in cache",
        // use a path to draw a text
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache

        canvas.drawPath(mPath, mStrokePaint);
        canvas.drawPath(mPath, mTextPaint);
    }

    private void updatePaths(String[] strings) {
        float maxWidth = 0;
        for (String str : strings) {
            float w = mStrokePaint.measureText(str);
            maxWidth = (w > maxWidth) ? w : maxWidth;
        }
        mWidth = (int) (maxWidth + (2 * textPadding));

        final float lineHeight = mStrokePaint.getFontSpacing();
        mHeight = (int) ((lineHeight * strings.length) + (2 * textPadding));

        float y = (mDrawFromCenter ? -mHeight / 2 : 0) + lineHeight * (2 / 3.f) + textPadding;
        final float x = mDrawFromCenter? 0 : mWidth/2;

        mPath = new Path();

        Path path;
        for (int i=0; i<strings.length; i++) {
            String str = strings[i];
            path = new Path();
            mTextPaint.getTextPath(str, 0, str.length(), x, y, path);
            path.close();
            mPath.addPath(path);
            y += lineHeight;
        }
    }

    private void updatePaints(Context context, String font, int color, int strokeColor) {
        mFont = font;
//        Typeface typeface = getTypeface(context, mFont);

        mStrokePaint.setColor(strokeColor);
//        mStrokePaint.setTypeface(typeface);

        mTextPaint.setColor(color);
//        mTextPaint.setTypeface(typeface);
    }

    /*
     * Call updateWidth() or updateHeight() after this methods if your need the text drawing size afterwards.
     */
    public void setText(Context context, String text, String text_font,
                        int text_color, int text_outline_color) {

        updatePaints(context, text_font, text_color, text_outline_color);
        setString(text);
    }

    /*
     * Call updateWidth() or updateHeight() after this methods if your need the text drawing size afterwards.
     */
    public void setString(String string) {
        mString = string;
        String[] strings = string.split("\n");

        updatePaths(strings);
    }

    public String getString() {
        return mString;
    }

    /*
     * Text size may differ from font to font.
     * Call updateWidth() or updateHeight() after this methods if your need the text drawing size afterwards.
     */
    public void setFont(Context context, String font) {
        mFont = font;
//        Typeface typeface = getTypeface(context, mFont);
//        mStrokePaint.setTypeface(typeface);
//        mTextPaint.setTypeface(typeface);
        setString(mString);
    }

    public String getFont() {
        return mFont;
    }

    public void setColor(int color) {
        mTextPaint.setColor(color);
    }

    public int getColor() {
        return mTextPaint.getColor();
    }

    public void setStrokeColor(int color) {
        mStrokePaint.setColor(color);
    }

    public int getOutlineColor() {
        return mStrokePaint.getColor();
    }

    public void setAlpha(int alpha) {
        mTextPaint.setAlpha(alpha);
        if (mStrokePaint.getAlpha()!=0)
            mStrokePaint.setAlpha(alpha);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

//    public static Typeface getTypeface(Context ctx, String fontPath) {
//        return Typeface.createFromAsset(mgr, path)
//    }
}