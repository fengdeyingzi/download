package com.xl.game.view;
import android.graphics.*;
import android.util.*;
import java.io.*;
import android.graphics.Bitmap.*;
public class bitmap
{
	Bitmap bmp;
	int bx;//图片x坐标
	int by;
	int bw;
	int bh;//图片高度
	int px;//屏幕x坐标
	int py;
	
	Bitmap zz(String string)
	{
		//将字符串转换成Bitmap类型
		Bitmap bitmapd = null;
		try
		{
			byte[]bitmapArray;
			bitmapArray=Base64.decode(string, Base64.DEFAULT);
			bitmapd=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			}
		catch(Exception e)
		{e.printStackTrace();}
		return bitmapd;
		}
		
		public String bitmaptoString(Bitmap bitmap)
		{
			//将Bitmap转换成字符串String 
			String string=null;
			ByteArrayOutputStream bStream=new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG,100,bStream);
			byte[]bytes=bStream.toByteArray();
			string=Base64.encodeToString(bytes,Base64.DEFAULT);
			return string;
			}
			
	
	
	
	
	
}
