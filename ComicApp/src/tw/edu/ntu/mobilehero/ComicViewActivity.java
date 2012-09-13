package tw.edu.ntu.mobilehero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ComicViewActivity extends Activity {

	
	private GridView gridView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i("test","test "+arg2);

                View view = ActivityGroupView.groupView.getLocalActivityManager().startActivity("Activity2", new Intent(ComicViewActivity.this, PicView.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                ActivityGroupView.groupView.replaceView(view);
			}
		});
		listview.setAdapter(myAdapter);
		
		Log.i("test","testView");
    }

	private static String[] dataObjects = new String[]{ "Text #1",
		"Text #2",
		"Text #3" ,"Text #3" ,"Text #3" ,"Text #3" ,"Text #3" ,"Text #3","Text #3","Text #3","Text #3","Text #3","Text #3"}; 
	
	private BaseAdapter myAdapter = new BaseAdapter() {
		
		@Override
		public int getCount() {
			return dataObjects.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);
			TextView title = (TextView) retval.findViewById(R.id.title);
			title.setText(dataObjects[position]);
			
			return retval;
		}
		
	};
    }
