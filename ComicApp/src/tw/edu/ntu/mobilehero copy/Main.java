package tw.edu.ntu.mobilehero;

import greendroid.widget.AsyncImageView;

import java.util.ArrayList;
import java.util.List;


import tw.edu.ntu.mobilehero.asynctask.DownloadComicAsyncTask;
import tw.edu.ntu.mobilehero.view.ComicView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class Main extends Activity{

    private GridView mGridView;
    private HorizontalGridViewAdapter mAdapter;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
		
        if (!Utils.hasInternetConnection(this)) {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mGridView = (GridView) findViewById(R.id.browse_gridview);
        mAdapter = new HorizontalGridViewAdapter(this, new ArrayList<Comic>());
        mGridView.setAdapter(mAdapter);
        
        DownloadComicAsyncTask mAsyncTask = new DownloadComicAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList<Comic> result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Log.d("!!","" + result.size());
                
                for(Comic c : result){
                    mAdapter.add(c);
                }
                mAdapter.notifyDataSetChanged();
                mGridView.setNumColumns(mAdapter.getCount());
            }
        };
        mAsyncTask.execute(0);
	}
    
    public class HorizontalGridViewAdapter extends ArrayAdapter<Comic> {

        Context context;
        public HorizontalGridViewAdapter(Context context, List<Comic> objects) {
            super(context, 0, objects);
            this.context = context;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ComicView image = new ComicView(parent.getContext());
            image.setLayoutParams(new GridView.LayoutParams(300,GridView.LayoutParams.MATCH_PARENT));
            for(int i = 0 ; i < 4 ; i++) {
                String url = getItem(position).getUrl(i);
                if(!url.equals("null")) {
                    image.setUrl(i, url);
                } else {
                    image.setImageResourse(i, R.drawable.ic_launcher);
                }
            }
            return image;
//            AsyncImageView image = new AsyncImageView(context);
//            image.setLayoutParams(new GridView.LayoutParams(100,100));
//            
//            String url = getItem(position).getUrl(0);
//            if(!url.equals("null")) {
//                Log.d("","url = " + url);
//                image.setUrl(url);
//            } else {
//                image.setImageResource(R.drawable.ic_launcher);
//            }
//            
//            return image;
        }
    }
}
