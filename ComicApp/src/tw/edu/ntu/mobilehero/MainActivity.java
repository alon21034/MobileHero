package tw.edu.ntu.mobilehero;

import greendroid.sql.FileManager;
import greendroid.sql.FileManager.FileInfo;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {
/** Called when the activity is first created. */

	Drawable icon_tab_1, icon_tab_2, icon_tab_3, icon_tab_4, icon_tab_5, icon_tab_6;

	Facebook facebook = new Facebook("311185528978877");
	
	private SharedPreferences mPrefs;
	@SuppressWarnings("deprecation")
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
 
 
		setContentView(R.layout.main_tab_horizontal);
		parseHorizontalTab();
		
		Utils.login(getCurrentActivity());
	}


	@SuppressWarnings("deprecation")
    private void parseHorizontalTab() {

		final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup(getLocalActivityManager());
		icon_tab_1 = this.getResources().getDrawable(R.drawable.home4);
		icon_tab_2 = this.getResources().getDrawable(R.drawable.plus4);
		icon_tab_3 = this.getResources().getDrawable(R.drawable.paint4);
		icon_tab_4 = this.getResources().getDrawable(R.drawable.hot4);
		icon_tab_5 = this.getResources().getDrawable(R.drawable.personal4);
		icon_tab_6 = this.getResources().getDrawable(R.drawable.setting4);
		createHorizontalTab(tabHost);
	}

	private void createHorizontalTab(TabHost tabHost) {

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator(
						createIndicatorView(this, tabHost, icon_tab_1, ""))
						.setContent(new Intent(this, Main.class)));		
		
		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(
						createIndicatorView(this, tabHost, icon_tab_2, ""))
						.setContent(new Intent(this, ActivityGroupPaint.class)));
		
		tabHost.addTab(tabHost
				.newTabSpec("tab3")
				.setIndicator(
						createIndicatorView(this, tabHost, icon_tab_3, ""))
						.setContent(new Intent(this, ActivityGroupEdit.class)));
		
		tabHost.addTab(tabHost
				.newTabSpec("tab4")
				.setIndicator(
						createIndicatorView(this, tabHost, icon_tab_4, ""))
						.setContent(new Intent(this, ActivityGroupView.class)));
		
		tabHost.addTab(tabHost
				.newTabSpec("tab5")
				.setIndicator(
						createIndicatorView(this, tabHost, icon_tab_5, ""))
						.setContent(new Intent(this, ActivityGroupView1.class)));
		
		tabHost.addTab(tabHost
				.newTabSpec("tab6")
				.setIndicator(
						createIndicatorView(this, tabHost, icon_tab_6, ""))
						.setContent(new Intent(this, ActivityGroupSetting.class)));

		TabWidget tw = tabHost.getTabWidget();
		tw.setOrientation(LinearLayout.VERTICAL);
}


private View createIndicatorView(Context context, TabHost tabHost, Drawable icon, String title) {
	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View tabIndicator = inflater.inflate(R.layout.tab_indicator_horizontal, tabHost.getTabWidget(), false);
	final ImageView iconView = (ImageView) tabIndicator.findViewById(R.id.icon);
	final TextView titleView = (TextView) tabIndicator.findViewById(R.id.title);
	titleView.setText(title);
	iconView.setImageDrawable(icon);
	return tabIndicator;
}

}
