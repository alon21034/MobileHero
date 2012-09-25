package tw.edu.ntu.mobilehero;

import java.util.ArrayList;

import tw.edu.ntu.mobilehero.asynctask.DownloadComicAsyncTask;
import tw.edu.ntu.mobilehero.view.ComicView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

public class BrowserFragment extends Fragment {

    private LinearLayout mTopScrollView;
    private LinearLayout mBotScrollView;
    
    OnClickListener listener;
    
    public BrowserFragment(OnClickListener l) {
        listener = l;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_browser, container, false);
        mTopScrollView = (LinearLayout) v.findViewById(R.id.browser_horizontalscroll_top);
        mBotScrollView = (LinearLayout) v.findViewById(R.id.browser_horizontalscroll_down);
        
        DownloadComicAsyncTask mAsyncTask = new DownloadComicAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList<Comic> result) {
                super.onPostExecute(result);
                
                boolean flag = false;
                for(Comic c : result){
                    ComicView child = new ComicView(getActivity().getApplicationContext(), listener);
                    child.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    child.setPadding(10, 10, 10, 10);
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

        return v;
    }
}
