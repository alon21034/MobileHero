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
    
    private LinearLayout mTopScrollView;
    private LinearLayout mBotScrollView;

    OnClickListener listener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            Log.d("!!","!!!!!!!");
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
		
        if (!Utils.hasInternetConnection(this)) {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        
        mTopScrollView = (LinearLayout) findViewById(R.id.browser_horizontalscroll_top);
        mBotScrollView = (LinearLayout) findViewById(R.id.browser_horizontalscroll_down);
        
        DownloadComicAsyncTask mAsyncTask = new DownloadComicAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList<Comic> result) {
                super.onPostExecute(result);
                
                boolean flag = false;
                for(Comic c : result){
                    ComicView child = new ComicView(getApplicationContext(), listener);
                    child.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT));
                    child.addComic(c);
                    if(!flag) {
                        mTopScrollView.addView(child);
                        flag = true;
                    } else {
                        mBotScrollView.addView(child);
                        flag = false;
                    }
                }
            }
        };
        mAsyncTask.execute(0);
	}
}
