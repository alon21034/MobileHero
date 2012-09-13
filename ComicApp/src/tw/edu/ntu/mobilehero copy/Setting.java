package tw.edu.ntu.mobilehero;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Setting extends Activity  {

    private TextView myText;
    
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        myText = (TextView) findViewById(R.id.myTextView01);
        
        
    }
}
