package tw.edu.ntu.mobilehero;

import greendroid.widget.AsyncImageView;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import tw.edu.ntu.mobilehero.view.Comic;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


public class PicView extends Fragment implements OnClickListener, OnTouchListener {
	
	AsyncImageView comic1;
	AsyncImageView comic2;
	AsyncImageView comic3;
	AsyncImageView comic4;
	AsyncImageView preview;
	ImageView number1;
	ImageView number2;
	ImageView number3;
	ImageView number4;
	
	private ImageView edit;
	private ImageView share;
	private ImageView black;
	private boolean isEditable;
	
	Comic comic;
	
	public PicView() {
	    
	}
	
	public PicView(Comic v) {
	    Log.d("!!","pic view constructor");
	    comic = v;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.picview);
        View v = inflater.inflate(R.layout.picview, container, false);
        Log.d("!!","PicviewActivity on create view");
        edit = (ImageView) v.findViewById(R.id.picview_edit);
        edit.setOnClickListener(this);
        edit.bringToFront();
        share = (ImageView) v.findViewById(R.id.picview_share);
        share.setOnClickListener(this);
        share.bringToFront();
        
        isEditable = false;
        
        comic1 = (AsyncImageView) v.findViewById(R.id.picview_pic1);
        comic1.setOnTouchListener(this);
        comic2 = (AsyncImageView) v.findViewById(R.id.picview_pic2);
        comic2.setOnTouchListener(this);
        comic3 = (AsyncImageView) v.findViewById(R.id.picview_pic3);
        comic3.setOnTouchListener(this);
        comic4 = (AsyncImageView) v.findViewById(R.id.picview_pic4);
        comic4.setOnTouchListener(this);
        preview = (AsyncImageView) v.findViewById(R.id.picview_preview);
        preview.setVisibility(4);
        number1 = (ImageView) v.findViewById(R.id.picview_number1);
        number2 = (ImageView) v.findViewById(R.id.picview_number2);
        number3 = (ImageView) v.findViewById(R.id.picview_number3);
        number4 = (ImageView) v.findViewById(R.id.picview_number4);
        
        black = (ImageView) v.findViewById(R.id.picview_black);
        if(comic.getUrl(0).equals("null"))
            comic1.setImageResource(R.drawable.ic_launcher);
        else
            comic1.setUrl(comic.getUrl(0));
        if(comic.getUrl(1).equals("null"))
            comic2.setImageResource(R.drawable.ic_launcher);
        else
            comic2.setUrl(comic.getUrl(1));
        if(comic.getUrl(2).equals("null"))
            comic3.setImageResource(R.drawable.ic_launcher);
        else
            comic3.setUrl(comic.getUrl(2));
        if(comic.getUrl(3).equals("null"))
            comic4.setImageResource(R.drawable.ic_launcher);
        else
            comic4.setUrl(comic.getUrl(3));
        return v;
    }
    
    public void setAnimation(View v){
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
        translateAnimation.setDuration(10);
        v.startAnimation(translateAnimation);
    }    
    
    public void setAnimation1(View v , int mEnd_x, int mEnd_y){
        Random random = new Random();
        final int end_x = (random.nextInt())%10;
        final int end_y = (random.nextInt())%10;
        TranslateAnimation translateAnimation = new TranslateAnimation(mEnd_x,end_x,mEnd_y, end_y);
        int flag = Math.max(Math.abs(end_x-mEnd_x), Math.abs(end_y-mEnd_y));
        mEnd_x = end_x;
        mEnd_y = end_y;
        
        translateAnimation.setAnimationListener(new AnimationListener() {
     
                    @Override
                    public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                    	setAnimation1(number1 ,end_x, end_y);
                    }
            });
        	translateAnimation.setDuration(getDuration(flag));
        	v.startAnimation(translateAnimation);
    }
    
    public void setAnimation2(View v , int mEnd_x, int mEnd_y){
        Random random = new Random();
        final int end_x = (random.nextInt())%10;
        final int end_y = (random.nextInt())%10;
        TranslateAnimation translateAnimation = new TranslateAnimation(mEnd_x,end_x,mEnd_y, end_y);
        int flag = Math.max(Math.abs(end_x-mEnd_x), Math.abs(end_y-mEnd_y));
        mEnd_x = end_x;
        mEnd_y = end_y;
        
        translateAnimation.setAnimationListener(new AnimationListener() {
     
                    @Override
                    public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                    	setAnimation2(number2 ,end_x, end_y);
                    }
            });
        	translateAnimation.setDuration(getDuration(flag));
        	v.startAnimation(translateAnimation);
    }    
    
    public void setAnimation3(View v , int mEnd_x, int mEnd_y){
        Random random = new Random();
        final int end_x = (random.nextInt())%10;
        final int end_y = (random.nextInt())%10;
        TranslateAnimation translateAnimation = new TranslateAnimation(mEnd_x,end_x,mEnd_y, end_y);
        int flag = Math.max(Math.abs(end_x-mEnd_x), Math.abs(end_y-mEnd_y));
        mEnd_x = end_x;
        mEnd_y = end_y;
        
        translateAnimation.setAnimationListener(new AnimationListener() {
     
                    @Override
                    public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                    	setAnimation3(number3 ,end_x, end_y);
                    }
            });
        	translateAnimation.setDuration(getDuration(flag));
        	v.startAnimation(translateAnimation);
    }    
    
    public void setAnimation4(View v , int mEnd_x, int mEnd_y){
        Random random = new Random();
        final int end_x = (random.nextInt())%10;
        final int end_y = (random.nextInt())%10;
        TranslateAnimation translateAnimation = new TranslateAnimation(mEnd_x,end_x,mEnd_y, end_y);
        int flag = Math.max(Math.abs(end_x-mEnd_x), Math.abs(end_y-mEnd_y));
        mEnd_x = end_x;
        mEnd_y = end_y;
        
        translateAnimation.setAnimationListener(new AnimationListener() {
     
                    @Override
                    public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                    	setAnimation4(number4 ,end_x, end_y);
                    }
            });
        	translateAnimation.setDuration(getDuration(flag));
        	v.startAnimation(translateAnimation);
    }
    
    
    public int getDuration(int flag){
        return flag*100;
    }    

    
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if ( arg0 == edit){
			if(isEditable == false){
				isEditable = true;
				//Animation shake1 = AnimationUtils.loadAnimation(this, R.anim.shake);
				setAnimation4(number4,0,0);
				setAnimation1(number1,0,0);
				setAnimation2(number2,0,0);
				setAnimation3(number3,0,0);
				black.setVisibility(0);

				if(getActivity() instanceof ActivityGroupView){
				    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				    Bundle bundle = new Bundle();
				    if(!comic.getUrl(0).equals("null")){
				        comic1.buildDrawingCache();
				        bundle.putByteArray("image1", getByteArray(comic1.getDrawingCache()));
				    }

                    if(!comic.getUrl(1).equals("null")){
                        comic1.buildDrawingCache();
                        bundle.putByteArray("image2", getByteArray(comic2.getDrawingCache()));
                    }
                    
                    if(!comic.getUrl(2).equals("null")){
                        comic1.buildDrawingCache();
                        bundle.putByteArray("image3", getByteArray(comic3.getDrawingCache()));
                    }
                    
			        ft.replace(R.id.simple_fragment, new PaintingActivity(bundle, comic));
			        ft.addToBackStack(null);  
			        ft.commitAllowingStateLoss();  
				}
				
			}else {
				isEditable = false;
				setAnimation(number4);
				setAnimation(number1);
				setAnimation(number2);
				setAnimation(number3);
				black.setVisibility(4);
			}
		}
	}
	
	public byte[] getByteArray(Bitmap bm) {
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        bm.recycle();
        return stream.toByteArray();
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
    	float x = event.getX();
    	float y = event.getY();
    	
    	Log.i("touch","touch " + x +" " +y);
    	
		return false;
    	
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction() == MotionEvent.ACTION_UP){
			preview.setVisibility(4);
		}

		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(isEditable == false){
				if (v == comic1){
					preview.setUrl(comic.getUrl(0));
					preview.setAlpha(200);
					preview.setVisibility(0);
				
				}else if (v == comic2){
				    preview.setUrl(comic.getUrl(1));
					preview.setAlpha(200);
					preview.setVisibility(0);			
				}else if (v == comic3){
				    preview.setUrl(comic.getUrl(2));
					preview.setAlpha(200);
					preview.setVisibility(0);			
				}else if (v == comic4){
				    preview.setUrl(comic.getUrl(3));
					preview.setAlpha(200);
					preview.setVisibility(0);			
				}
			}else{
				
				setAnimation(number1);
				setAnimation(number2);
				setAnimation(number3);
				setAnimation(number4);
				if(v == comic3 || v == comic1 || v == comic2 || v == comic4){
					if(getActivity() instanceof ActivityGroupView)
						((ActivityGroupView) getActivity()).changeFragment(new PaintingActivity());
					else {
						((ActivityGroupView1) getActivity()).changeFragment(new PaintingActivity());
					}
				}else if (v == edit){
					
				}
			}
		}
		
		return true;
	}

}

