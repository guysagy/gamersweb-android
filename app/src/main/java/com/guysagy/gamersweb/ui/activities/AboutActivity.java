package com.guysagy.gamersweb.ui.activities;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.guysagy.gamersweb.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setActivityView(R.layout.about, R.string.str_About, Color.WHITE);

        String strAboutText = "";
        
        try 
        {
            InputStream aboutFile = getResources().openRawResource(R.raw.about);
            strAboutText = inputStreamToString(aboutFile);
		} 
        catch (IOException e) 
		{
        	strAboutText = getResources().getString(R.string.version_info);
		}
        finally
        {
            ((WebView)findViewById(R.id.WebView_About)).loadData(strAboutText, "text/html", "utf-8");
        }
    }

	@Override
	protected void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);
	}
	
    public String inputStreamToString(InputStream inputStream) throws IOException 
    {
        StringBuffer stringBuffer = new StringBuffer();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String strLine = null;
        
        while ((strLine = dataInputStream.readLine()) != null) 
        {
        	stringBuffer.append(strLine);
        }
        
        dataInputStream.close();
        inputStream.close();
        return stringBuffer.toString();
    }
}
