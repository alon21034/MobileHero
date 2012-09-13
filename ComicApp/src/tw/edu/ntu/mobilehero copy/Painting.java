package tw.edu.ntu.mobilehero;

import tw.edu.ntu.mobilehero.EasingType.Type;
import tw.edu.ntu.mobilehero.Panel.OnPanelListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Painting extends Activity  implements OnPanelListener {

	private Panel panel1;
	private Panel panel2;
	private Panel panel3;
	private Panel panel4;
	private Panel panel5;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint);

        //Panel panel;

        
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
    }
	@Override
	public void onPanelClosed(Panel panel) {
		// TODO Auto-generated method stub
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("TestPanels", "Panel [" + panelName + "] closed");
	}
	@Override
	public void onPanelOpened(Panel panel) {
		// TODO Auto-generated method stub
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("TestPanels", "Panel [" + panelName + "] opened");
	}
    
    
}
