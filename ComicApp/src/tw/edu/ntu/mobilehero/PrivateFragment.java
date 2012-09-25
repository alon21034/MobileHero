package tw.edu.ntu.mobilehero;

import java.util.ArrayList;

import tw.edu.ntu.mobilehero.asynctask.DownloadComicAsyncTask;
import tw.edu.ntu.mobilehero.view.Comic;
import tw.edu.ntu.mobilehero.view.ComicView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

public class PrivateFragment extends Fragment {

    private LinearLayout mTopScrollView;
    private LinearLayout mBotScrollView;
    
    OnClickListener listener;
    
    public PrivateFragment(OnClickListener l) {
        listener = l;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_browser_others, container, false);
        mTopScrollView = (LinearLayout) v.findViewById(R.id.browser_others_top);
        mBotScrollView = (LinearLayout) v.findViewById(R.id.browser_others_bot);
        
        DownloadComicAsyncTask mAsyncTask = new DownloadComicAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList<Comic> result) {
                super.onPostExecute(result);
                
                for(Comic c : result){
                    ComicView child = new ComicView(getActivity().getApplicationContext(), listener);
                    child.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    child.setPadding(10, 10, 10, 10);
                    child.addComic(c);
                    mTopScrollView.addView(child);
                }
            }
        };
        mAsyncTask.execute(0);

        DownloadComicAsyncTask mAsyncTask2 = new DownloadComicAsyncTask() {
            @Override
            protected void onPostExecute(ArrayList<Comic> result) {
                super.onPostExecute(result);
                
                for(Comic c : result){
                    ComicView child = new ComicView(getActivity().getApplicationContext(), listener);
                    child.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    child.setPadding(10, 10, 10, 10);
                    child.addComic(c);
                    mBotScrollView.addView(child);
                }
            }
        };
        mAsyncTask2.execute(0);      
        
        return v;
    }
}
