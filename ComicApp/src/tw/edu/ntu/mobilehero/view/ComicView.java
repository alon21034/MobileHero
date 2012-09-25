package tw.edu.ntu.mobilehero.view;

import greendroid.widget.AsyncImageView;

import java.util.ArrayList;

import tw.edu.ntu.mobilehero.Comic;
import tw.edu.ntu.mobilehero.R;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ComicView extends LinearLayout implements OnClickListener{

    ArrayList<AsyncImageView> image = new ArrayList<AsyncImageView>();
    Comic comic;
    Context context;
    
    public ComicView(Context context, OnClickListener listener) {
        super(context);
        this.context = context;
        final LayoutInflater inflater = LayoutInflater.from(context);
        addView(inflater.inflate(R.layout.comic_view, null));
        
        image.add( (AsyncImageView) findViewById(R.id.comic_1));
        image.add( (AsyncImageView) findViewById(R.id.comic_2));
        image.add( (AsyncImageView) findViewById(R.id.comic_3));
        image.add( (AsyncImageView) findViewById(R.id.comic_4));
        
        //image.get(3).setBackgroundColor(Color.BLACK);
        this.setOnClickListener(listener);
    }
    
    public void setUrl(int n, String str) {
        image.get(n).setUrl(str);
    }
    
    public void setImageResourse(int n, int id) {
        image.get(n).setImageResource(id);
    }
    
    public void addComic(Comic c) {
        comic = c;
        for(int i = 0 ; i < 4 ; i++) {
            String url = c.getUrl(i);
            Log.d("!!",url);
            if(!url.equals("null")) {
                setUrl(i, url);
            } else {
                setImageResourse(i, R.drawable.ic_launcher);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d("!!","on click");
        
    }
    
    public Comic getComic() {
        return comic;
    }
    
}