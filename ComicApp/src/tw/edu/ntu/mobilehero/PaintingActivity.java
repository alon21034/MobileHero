package tw.edu.ntu.mobilehero;

import greendroid.widget.AsyncImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import tw.edu.ntu.mobilehero.EasingType.Type;
import tw.edu.ntu.mobilehero.MultiTouchController.OnFlickListener;
import tw.edu.ntu.mobilehero.Panel.OnPanelListener;
import tw.edu.ntu.mobilehero.view.DrawView;
import tw.edu.ntu.mobilehero.view.ImageScrap;
import tw.edu.ntu.mobilehero.view.DrawView.State;
import tw.edu.ntu.mobilehero.view.Scrap;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

@TargetApi(11)
public class PaintingActivity extends Fragment implements OnPanelListener , OnTouchListener, OnFlickListener<Scrap>{

	private Panel panel1;
	private Panel panel2;
	private Panel panel3;
	private Panel panel4;
	private Panel panel5;
	private ImageView tool;
	private ImageView pic1;
	private ImageView pic2;
	private ImageView pic3;
	private ImageView preview;
	private ImageView imageView;
	private ImageView upload;
	private ImageView save;
	private GridView gridview;
	private GridView penbox;
    private Bitmap myBitmap;  
    private byte[] mContent;
    private int resource;
    
    private Context context;
    
    private DrawView canvasView;
//    private RelativeLayout canvasLayout;
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.dialog1,R.drawable.dialog2,
            R.drawable.dialog3,R.drawable.dialog4,
            R.drawable.umbrella,R.drawable.umbrella1,
            R.drawable.glasses1,R.drawable.glasses2,
            R.drawable.car
    };
    
    private Integer[] mPenIds = {
            R.drawable.blackpen,R.drawable.gray,
            R.drawable.red,R.drawable.yellow,
            R.drawable.pink,R.drawable.yellowgreen,
            R.drawable.green,R.drawable.blue,
            R.drawable.bluegreen,R.drawable.white,
            R.drawable.radius1,R.drawable.radius2,
            R.drawable.radius3,R.drawable.radius4
    };

    public PaintingActivity() {

    }
    
    Bitmap bm1;
    Bitmap bm2;
    Bitmap bm3;
	public PaintingActivity(Bundle bundle, Comic c) {
	    
	    comic = c;
	    byte[] b1 = bundle.getByteArray("image1");
	    byte[] b2 = bundle.getByteArray("image2");
	    byte[] b3 = bundle.getByteArray("image3");
	    if(b1 != null)
	        bm1 = BitmapFactory.decodeByteArray(b1, 0, b1.length);
	    if(b2 != null)
	        bm2 = BitmapFactory.decodeByteArray(b2, 0, b2.length);
	    if(b3 != null)
	        bm3 = BitmapFactory.decodeByteArray(b3, 0, b3.length);
	    
	}
    
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.paint);
        RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.paint, container, false);
        //context = this;
        //Panel panel;
        Log.d("!!","paintingActivity on create view");
        //f = (FrameLayout) findViewById(R.id.paint_frameLayout);
        panel1 = (Panel) v.findViewById(R.id.rightPanel);
        panel1.setOnPanelListener(this);
        panel1.setInterpolator(new BackInterpolator(Type.OUT, 2));

        panel2 = (Panel) v.findViewById(R.id.rightPanel2);
        panel2.setOnPanelListener(this);
        panel2.setInterpolator(new BackInterpolator(Type.OUT, 2)); 
        
        panel3 = (Panel) v.findViewById(R.id.rightPanel3);
        panel3.setOnPanelListener(this);
        panel3.setInterpolator(new BackInterpolator(Type.OUT, 2));

        panel4 = (Panel) v.findViewById(R.id.rightPanel4);
        panel4.setOnPanelListener(this);
        panel4.setInterpolator(new BackInterpolator(Type.OUT, 2));
        
        panel5 = (Panel) v.findViewById(R.id.rightPanel5);
        panel5.setOnPanelListener(this);
        panel5.setInterpolator(new BackInterpolator(Type.OUT, 2));
 
        imageView = (ImageView) v.findViewById(R.id.paint);

        
        
        tool = new ImageView(getActivity());
        tool.setImageResource(R.drawable.toolbar);
        
        pic1 = (ImageView) v.findViewById(R.id.paint_pic1);
        pic1.setOnTouchListener(this);
        if(bm1 != null)
            pic1.setImageBitmap(bm1);
        
        pic2 = (ImageView) v.findViewById(R.id.paint_pic2);
        pic2.setOnTouchListener(this);
        if(bm2 != null)
            pic2.setImageBitmap(bm2);
        
        pic3 = (ImageView) v.findViewById(R.id.paint_pic3);
        pic3.setOnTouchListener(this);
        if(bm3 != null)
            pic3.setImageBitmap(bm3);
        
        preview = (AsyncImageView) v.findViewById(R.id.paint_preview);
        
        LayoutInflater prospect = inflater;
        getActivity().addContentView( prospect.inflate(R.layout.tool, null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
        
        upload = (ImageView) v.findViewById(R.id.paint_upload);
        upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View vv) {
//				new UploadComicAsyncTask().execute("¿…¶W", UUID.randomUUID().toString(), "1");
			}
		});
        save = (ImageView) v.findViewById(R.id.paint_save);
        save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View vv) {
				
			}
		});
        
//        canvasLayout = (RelativeLayout) v.findViewById(R.id.canvasLayout);
        
        gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	canvasView.setState(State.Picture);
            	
            	canvasView.addScrap(mThumbIds[position]);
            }
        });
        
        penbox = (GridView) v.findViewById(R.id.paint_pen);
        penbox.setAdapter(new PenAdapter(getActivity()));

        penbox.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	canvasView.setState(State.Drawing);
            	
    			switch (position) {
    			case 0:
    				canvasView.setColor(Color.BLACK);
    				break;
    			case 1:
    				canvasView.setColor(Color.argb(255, 108, 108, 108));			
    				break;
    			case 2:
    				canvasView.setColor(Color.argb(255, 254, 102, 102));
    				break;
    			case 3:
    				canvasView.setColor(Color.argb(255, 254, 221, 0));
    				break;
    			case 4:
    				canvasView.setColor(Color.argb(255, 254, 0, 137));
    				break;
    			case 5:
    				canvasView.setColor(Color.argb(255, 203, 222, 40));
    				break;
    			case 6:
    				canvasView.setColor(Color.argb(255, 84, 179, 86));
    				break;
    			case 7:
    				canvasView.setColor(Color.argb(255, 84, 95, 179));
    				break;
    			case 8:
    				canvasView.setColor(Color.argb(255, 27, 156, 158));
    				break;
    			case 9:
    				canvasView.setColor(Color.WHITE);
    				break;
    			case 10:
    				canvasView.setStrokeWidth(9);
    				break;
    			case 11:
    				canvasView.setStrokeWidth(12);
    				break;
    			case 12:
    				canvasView.setStrokeWidth(15);
    				break;
    			case 13:
    				canvasView.setStrokeWidth(18);
    				break;

    			default:
    				break;
    			}
            }
        });

        canvasView = (DrawView) v.findViewById(R.id.canvasView);
        canvasView.setOnFlickListener(this);
        canvasView.getPaint().setAntiAlias(true);
        canvasView.getPaint().setDither(true);
        canvasView.getPaint().setColor(Color.BLACK);
        canvasView.getPaint().setStyle(Paint.Style.STROKE);
        canvasView.getPaint().setStrokeJoin(Paint.Join.ROUND);
        canvasView.getPaint().setStrokeCap(Paint.Cap.ROUND);
        canvasView.getPaint().setStrokeWidth(12);
        
        return v;

    }
	@Override
	public void onPanelClosed(Panel panel) {
		// TODO Auto-generated method stub
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("TestPanels", "Panel [" + panelName + "] closed");
		if(panelName.equals("rightPanel5")){
			
            final CharSequence[] items = { "From Gallery", "From camera" };  
            
            AlertDialog dlg = new AlertDialog.Builder(getActivity()).setTitle("Select Resource").setItems(items,   
                    new DialogInterface.OnClickListener() {  
                          
                        @Override  
                        public void onClick(DialogInterface dialog, int which) {  
                            // TODO Auto-generated method stub  
                            if(which==1){  
                                Intent getImageByCamera  = new Intent("android.media.action.IMAGE_CAPTURE");  
                                getActivity().startActivityForResult(getImageByCamera, 1);  
                            }else{  
                                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);  
                                getImage.addCategory(Intent.CATEGORY_OPENABLE);  
                                getImage.setType("image/jpeg");  
                                getActivity().startActivityForResult(getImage, 0);  
                            }  
                              
                        }  
                    }).create();  
            dlg.show();  
			
			
		}else if (panelName.equals("rightPanel3")){
			////////////
			// eraser.//
			////////////
		}		
		
	}
	@Override
	public void onPanelOpened(Panel panel) {
		// TODO Auto-generated method stub
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("TestPanels", "Panel [" + panelName + "] opened" + panel.getId());
		
		if(panelName.equals("rightPanel5")){
			
            final CharSequence[] items = { "From Gallery", "From camera" };  
            
            AlertDialog dlg = new AlertDialog.Builder(getActivity()).setTitle("Select Resource").setItems(items,   
                    new DialogInterface.OnClickListener() {  
                          
                        @Override  
                        public void onClick(DialogInterface dialog, int which) {  
                            // TODO Auto-generated method stub  
                            if(which==1){  
                                Intent getImageByCamera  = new Intent("android.media.action.IMAGE_CAPTURE");  
                                getActivity().startActivityForResult(getImageByCamera, 1);  
                            }else{  
                                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);  
                                getImage.addCategory(Intent.CATEGORY_OPENABLE);  
                                getImage.setType("image/jpeg");  
                                getActivity().startActivityForResult(getImage, 0);  
                            }  
                              
                        }  
                    }).create();  
            dlg.show();  
			
			
		}else if (panelName.equals("rightPanel3")){
			///////////
			//eraser.//
			///////////
		}
		
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i("touch","touch");
		if( event.getAction() == MotionEvent.ACTION_DOWN ){
			preview.setVisibility(0);
			preview.setAlpha(200);
			switch (v.getId()) {
            case R.id.paint_pic1:
                preview.setUrl(comic.getUrl(0));
                break;
            case R.id.paint_pic2:
                preview.setUrl(comic.getUrl(1));
                break;
            case R.id.paint_pic3:
                preview.setUrl(comic.getUrl(2));
                break;
            default:
                break;
            }
		}else if( event.getAction() == MotionEvent.ACTION_UP ){
			preview.setVisibility(4);
		}
		
		
		return true;
	}
    
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    
	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(0, 0, 0, 0);
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(mThumbIds[position]);
	        return imageView;
	    }

	}
	
	public class PenAdapter extends BaseAdapter {
	    private Context mContext;

	    public PenAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mPenIds.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    
	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(0, 0, 0, 0);
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(mPenIds[position]);
	        return imageView;
	    }

	}
	
	
	public void handleActivityResult(int requestCode, int resultCode, Intent data){
		 super.onActivityResult(requestCode, resultCode, data);  
	        
	        ContentResolver contentResolver= getActivity().getContentResolver();  

	        if(requestCode==0){  
	             
	            try { 
	                Uri orginalUri = data.getData();
	                mContent = readStream(contentResolver.openInputStream(Uri.parse(orginalUri.toString()))); 
	                myBitmap  =getPicFromBytes(mContent,null); 
	                imageView.setImageBitmap(myBitmap); 
	            } catch (Exception e) { 
	                e.printStackTrace(); 
	                // TODO: handle exception 
	            }

	        }else if(requestCode==1){
	            try {  
	                Bundle extras = data.getExtras();  
	                myBitmap = (Bitmap) extras.get("data");  
	                ByteArrayOutputStream baos = new ByteArrayOutputStream();       
	                myBitmap.compress(Bitmap.CompressFormat.JPEG , 100, baos);       
	                mContent=baos.toByteArray();  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	                // TODO: handle exception  
	            }  
	            imageView.setImageBitmap(myBitmap);  
	        }  
    }
	
   public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {   
        if (bytes != null)   
            if (opts != null)   
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);   
            else   
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);   
        return null;   
    }   
	      
    public static byte[] readStream(InputStream in) throws Exception{  
        byte[] buffer  =new byte[1024];  
        int len  =-1;  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        
        while((len=in.read(buffer))!=-1){  
            outStream.write(buffer, 0, len);  
        }  
        byte[] data  =outStream.toByteArray();  
        outStream.close();  
        in.close();  
        return data;  
    }
    
    @Override
    public void onFlick(Scrap obj) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void onFingerUp(Scrap obj, MotionEvent event) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void onDrag(Scrap obj, MotionEvent event) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void onFingerDown(Scrap obj, MotionEvent event) {
        // TODO Auto-generated method stub
        
    }  
}
