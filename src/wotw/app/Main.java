package wotw.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        ((Button) findViewById(R.id.shareBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		Intent i = new Intent();
        		i.setClass(Main.this, UploadImage.class);
        		startActivity(i);
        	}
        });
		
	}
	
}
