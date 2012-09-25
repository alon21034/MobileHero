package tw.edu.ntu.mobilehero;

import java.util.ArrayList;

import tw.edu.ntu.mobilehero.asynctask.DownloadComicAsyncTask;
import tw.edu.ntu.mobilehero.view.Comic;
import tw.edu.ntu.mobilehero.view.ComicView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Main extends Activity{
    
    private LinearLayout mScrollView;

    OnClickListener listener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            Log.d("!!","!!!!!!!");
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_main);
		
        if (!Utils.hasInternetConnection(this)) {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        
        mScrollView = (LinearLayout) findViewById(R.id.mainbrowser_horizontalscroll);
        
        DownloadComicAsyncTask mAsyncTask = new DownloadComicAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList<Comic> result) {
                super.onPostExecute(result);
                
                for(Comic c : result){
                    ComicView child = new ComicView(getApplicationContext(), listener);
                    child.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT));
                    child.addComic(c);
                    mScrollView.addView(child);
                }
            }
        };
        mAsyncTask.execute(0);
	}
}
