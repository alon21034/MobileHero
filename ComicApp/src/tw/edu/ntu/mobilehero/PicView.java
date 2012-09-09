package tw.edu.ntu.mobilehero;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class PicView extends Activity implements OnClickListener {

	ImageView edit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picview);

        edit = (ImageView)findViewById(R.id.picview_edit);
        edit.setOnClickListener(this);
        
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        findViewById(R.id.picview_number1).startAnimation(shake);	
        findViewById(R.id.picview_number2).startAnimation(shake);	
        findViewById(R.id.picview_number3).startAnimation(shake);	
        findViewById(R.id.picview_number4).startAnimation(shake);	
	}

}
