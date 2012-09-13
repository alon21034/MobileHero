package tw.edu.ntu.mobilehero.view;

import greendroid.widget.AsyncImageView;

import java.util.ArrayList;

import tw.edu.ntu.mobilehero.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class ComicView extends LinearLayout{

    ArrayList<AsyncImageView> image = new ArrayList<AsyncImageView>();
    
    public ComicView(Context context) {
        super(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        addView(inflater.inflate(R.layout.comic_view, null));
        
        image.add( (AsyncImageView) findViewById(R.id.comic_1));
        image.add( (AsyncImageView) findViewById(R.id.comic_2));
        image.add( (AsyncImageView) findViewById(R.id.comic_3));
        image.add( (AsyncImageView) findViewById(R.id.comic_4));
        
        image.get(3).setBackgroundColor(Color.BLACK);
    }
    
    public void setUrl(int n, String str) {
        image.get(n).setUrl(str);
    }
    
    public void setImageResourse(int n, int id) {
        image.get(n).setImageResource(id);
    }
}
