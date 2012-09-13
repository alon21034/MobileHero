package tw.edu.ntu.mobilehero;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class OpenFileActivity extends Activity {
	private List<String> mPanelPaths = new ArrayList<String>();
	private List<Bitmap> mPanelBitmaps = new ArrayList<Bitmap>();
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.open_existing_panel);
        
        getPanels();
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new PanelAdapter());
        gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("filePath", mPanelPaths.get(position));
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
        	
        });
    }

    

    private void getPanels() {        
    	File sdcardDir = Environment.getExternalStorageDirectory();
    	File ComicRelaysDir = new File(sdcardDir, "ComicRelays");
    	
    	if (ComicRelaysDir.exists()) {
    		File[] files = ComicRelaysDir.listFiles();
	        for (File file : files) {
	        	if (file.isFile()) {
	        		Log.d("file", file.getAbsolutePath());
	        		Bitmap panel = BitmapFactory.decodeFile(file.getAbsolutePath());
	        		mPanelPaths.add(file.getAbsolutePath());
	        		mPanelBitmaps.add(panel);
	        	}
	        }
    	}
    }

    public class PanelAdapter extends BaseAdapter {
        public PanelAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;

            if (convertView == null) {
                i = new ImageView(OpenFileActivity.this);
                i.setScaleType(ImageView.ScaleType.CENTER);
                i.setLayoutParams(new GridView.LayoutParams(280, 210));
            } else {
                i = (ImageView) convertView;
            }

            Bitmap panel = mPanelBitmaps.get(position);
            i.setImageBitmap(panel);

            return i;
        }

        public final int getCount() {
        	return mPanelBitmaps.size();
        }

        public final Object getItem(int position) {
            return mPanelBitmaps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }
}