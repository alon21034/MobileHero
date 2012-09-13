package tw.edu.ntu.mobilehero;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DrawActivity extends Activity {
	private final int OPEN_EXISTING_PANEL_REQUEST = 0;
	
	private CanvasView 	mCanvasView;
	private Paint		mPaint;
	
	public class CanvasView extends View {
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Paint   mBitmapPaint;
        private Path    mPath;
        private Path    mTextPath;

        @TargetApi(11)
        public CanvasView(Context c) {
            super(c);

            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            mPath = new Path();
            mTextPath = new Path();
            
            // size of the canvas
            setLayoutParams(new LinearLayout.LayoutParams(820, 600));
            // position  of the canvas
            //(TODO) api level 11
            setX(150);
    		setY(10);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);//0xFFAAAAAA
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }
        
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
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

        public void loadPanel(String filePath) {
        	Bitmap panelBitmap = BitmapFactory.decodeFile(filePath);
        	mCanvas.drawBitmap(
        			panelBitmap, null, new Rect(0, 0, panelBitmap.getWidth(), panelBitmap.getHeight()), null);
        }
        
        public void drawText(String string) {
        	mBitmapPaint.getTextPath(string, 0, string.length(), 450, 300, mTextPath);
        	mCanvas.drawPath(mTextPath, mBitmapPaint);
        	invalidate();
        	mTextPath.reset();
        }
        
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_down(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        
        private void touch_up() {
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
        }
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_draw);
		RelativeLayout drawLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		mCanvasView = new CanvasView(DrawActivity.this);
		drawLayout.addView(mCanvasView);
		        
        Button redButton = (Button) findViewById(R.id.redButton);
        Button yellowButton = (Button) findViewById(R.id.yellowButton);
        Button blueButton = (Button) findViewById(R.id.blueButton);
        Button smallButton = (Button) findViewById(R.id.smallButton);
        Button bigButton = (Button) findViewById(R.id.bigButton);
        Button penButton = (Button) findViewById(R.id.penButton);
        Button eraserButton = (Button) findViewById(R.id.eraserButton);
        Button openButton = (Button) findViewById(R.id.openButton);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button enterButton = (Button) findViewById(R.id.enterButton);
        ButtonClickedListener listener = new ButtonClickedListener();
        redButton.setOnClickListener(listener);
        yellowButton.setOnClickListener(listener);
        blueButton.setOnClickListener(listener);
        smallButton.setOnClickListener(listener);
        bigButton.setOnClickListener(listener);
        penButton.setOnClickListener(listener);
        eraserButton.setOnClickListener(listener);
        openButton.setOnClickListener(listener);
        saveButton.setOnClickListener(listener);
        enterButton.setOnClickListener(listener);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case OPEN_EXISTING_PANEL_REQUEST:
				Log.d("File Path", data.getExtras().getString("filePath"));
				mCanvasView.loadPanel(data.getExtras().getString("filePath"));
				break;
				
			default:
				break;
			}
		}
	}
	
	private class ButtonClickedListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.redButton:
				mPaint.setColor(Color.RED);
				break;
			case R.id.yellowButton:
				mPaint.setColor(Color.YELLOW);
				break;
			case R.id.blueButton:
				mPaint.setColor(Color.BLUE);
				break;
			case R.id.smallButton:
				mPaint.setStrokeWidth(9);
				break;
			case R.id.bigButton:
				mPaint.setStrokeWidth(15);
				break;
			case R.id.penButton:
				mPaint.setXfermode(null);
				break;
			case R.id.eraserButton:
			    //(TODO) background color
				mPaint.setColor(Color.WHITE);
//				mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
				break;
			case R.id.openButton:
				openFile();
				break;
			case R.id.saveButton:
				saveFile();
				break;
			case R.id.enterButton:
				EditText editText = (EditText) findViewById(R.id.editText);
				mCanvasView.drawText(editText.getText().toString());
				break;
		    			
    		default:
    			break;
    		}
    	}
    }
	
	public void openFile() {
		Intent intent = new Intent(DrawActivity.this, OpenFileActivity.class);
		startActivityForResult(intent, OPEN_EXISTING_PANEL_REQUEST);
	}
	
	public void saveFile()
    {        
        File imageStorageDir = new File(
    			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ComicRelay");

    	if (!imageStorageDir.exists()) {
    		if (!imageStorageDir.mkdirs()){
    			Log.d("Creating Dir \"ComicRelays\"", "failed");
    		}
    	}

    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    	File imageFile = new File(imageStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        try{
        	imageFile.createNewFile();
        	
        	Bitmap bitmap = Bitmap.createBitmap(mCanvasView.getWidth(), mCanvasView.getHeight(), Bitmap.Config.ARGB_8888);
        	Canvas canvas = new Canvas(bitmap);
        	mCanvasView.draw(canvas); 
        	FileOutputStream fos = new FileOutputStream(imageFile.getAbsolutePath());
        	if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
        		fos.flush();
        		fos.close();
            }
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace(); 
        }
        
        sendBroadcast(new Intent(
        		Intent.ACTION_MEDIA_MOUNTED,  
        		Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }
	
	public Uri getOutputImageFileUri(){
    	File imageStorageDir = new File(
    			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ComicRelays");

    	if (!imageStorageDir.exists()) {
    		if (!imageStorageDir.mkdirs()){
    			Log.d("ComicRelay", "failed to create directory");
    			return null;
    		}
    	}

    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    	File imageFile = new File(imageStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        return Uri.fromFile(imageFile);
    }
}
