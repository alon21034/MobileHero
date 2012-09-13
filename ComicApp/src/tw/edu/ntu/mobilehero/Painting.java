package tw.edu.ntu.mobilehero;

import tw.edu.ntu.mobilehero.EasingType.Type;
import tw.edu.ntu.mobilehero.Panel.OnPanelListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Painting extends Activity  implements OnPanelListener , OnTouchListener{

	private Panel panel1;
	private Panel panel2;
	private Panel panel3;
	private Panel panel4;
	private Panel panel5;
	private ImageView tool;
	
	//FrameLayout f;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint);

        //Panel panel;

        //f = (FrameLayout) findViewById(R.id.paint_frameLayout);
        panel1 = (Panel) findViewById(R.id.rightPanel);
        panel1.setOnPanelListener(this);
        panel1.setInterpolator(new BackInterpolator(Type.OUT, 2));

        panel2 = (Panel) findViewById(R.id.rightPanel2);
        panel2.setOnPanelListener(this);
        panel2.setInterpolator(new BackInterpolator(Type.OUT, 2)); 
        
        panel3 = (Panel) findViewById(R.id.rightPanel3);
        panel3.setOnPanelListener(this);
        panel3.setInterpolator(new BackInterpolator(Type.OUT, 2));

        panel4 = (Panel) findViewById(R.id.rightPanel4);
        panel4.setOnPanelListener(this);
        panel4.setInterpolator(new BackInterpolator(Type.OUT, 2));
        
        panel5 = (Panel) findViewById(R.id.rightPanel5);
        panel5.setOnPanelListener(this);
        panel5.setInterpolator(new BackInterpolator(Type.OUT, 2));        
        panel5.bringToFront();
        
        
        tool = (ImageView)findViewById(R.id.paint_toolbar);
        tool.setOnTouchListener(this);
        //tool = new ImageView(this);
        //tool.setImageResource(R.drawable.toolbar);
        /*
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(100,0,0,0);
        
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER_VERTICAL;        
        tool.setLayoutParams(params);
        addContentView(tool, params);
        tool.bringToFront();
*/
        
        RelativeLayout l = new RelativeLayout(this);
        l.addView(tool);
        //l.setGravity(RelativeLayout.CENTER_VERTICAL);
        l.setPadding(100, 0, 0, 0);
        //RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(150,700);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        l.setLayoutParams(p);
        this.addContentView(l, p);
        
        
        //tool = (ImageView) findViewById(R.id.paint_toolbar);
        //addContentView(tool, new LayoutParams(135,630));

        //tool.setOnTouchListener(this);
        //tool.setSelected(true);
        //tool.bringToFront();
    }
	@Override
	public void onPanelClosed(Panel panel) {
		// TODO Auto-generated method stub
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("TestPanels", "Panel [" + panelName + "] closed");
		//tool.setSelected(true);
		//tool.bringToFront();
	}
	@Override
	public void onPanelOpened(Panel panel) {
		// TODO Auto-generated method stub
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("TestPanels", "Panel [" + panelName + "] opened");
		//tool.setSelected(true);
		//tool.bringToFront();
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i("touch","touch");
		//tool.bringToFront();
		//f.invalidate();
		return true;
	}
    
    
}
