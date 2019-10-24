package com.xl.game.tool;
import android.app.Activity;
import android.util.Log;
import android.content.Context;

public class Logcat
{
	Context context;
	public static void e(Object activity, String out)
	{
		Log.e("XLLOG","信息："+out);
		
	}
	public static void e( String out)
	{
		Log.e("XLLOG","信息："+out);

	}
	public Logcat()
	{
		
	}
	
	
	
}
