package com.xl.game.view;
import android.graphics.*;
import android.util.*;

public  class Screenx 
{
	int x,y;//屏幕区域绝对坐标
	int w,h;//屏幕区域的绝对宽高
  
	int bmpx,bmpy;//画布位置
	int bmpw,bmph;//画布宽高
	Bitmap bitmap;//画布
	Paint paint;//画笔
	Canvas scrbuf;//上一层屏幕
	
	int type;//显示方式：
	private  final int 
	SHOW_SCALE=0,//等比例显示
	SHOW_SCALE_WIDTH=1,//以图片宽度拉伸对齐
	SHOW_SCALE_HEIGHT=2,//以图片高度拉伸对齐
	SHOW_SCALE_ALL=3,//等比例全屏显示
	
	SHOW_EXTRUDE=4,//拉伸全屏
	SHOW_CONSTANT=10;//不变
	
	
	Screenx(int px,int py,int pw,int ph)
	{
		x=px;
		y=py;
		w=pw;
		h=ph;
		bitmap=Bitmap.createBitmap(pw,ph,Bitmap.Config.ARGB_8888);
	}
	Screenx(Bitmap bitmap,int px,int py,int pw,int ph,int type)
	{
		this(px,py,pw,ph);
		this.bitmap=bitmap;
		//等比例显示思路：
		//当图片宽高比大于屏幕宽高比，以图片宽度为准
		//当图片宽高比小于屏幕宽高比，以图片高度为准
		
		if(type==SHOW_SCALE)//等比例显示
		{
			if(bitmap.getWidth()*10/bitmap.getHeight()>w*10/h)
			{
				bmpw=w;
				bmph=w* bitmap.getHeight()/bitmap.getWidth();
			}
			
		}
		//等比例全屏
		//当屏幕宽高比大于图片宽高比，以图片宽度为准
		//当屏幕宽高比小于图片宽高比，以图片高度为准
		else if(type==SHOW_SCALE_ALL)
		{
			if(w*10/h > bitmap.getWidth()*10/bitmap.getHeight())
			{
				
			}
		}
		
		
	}
	
	Screenx(Bitmap bitmap)
	{
		this.bitmap=bitmap;
		w=bitmap.getWidth();
		h=bitmap.getHeight();
		type=SHOW_SCALE;
	}
	
	void draw()//显示这个屏幕到上层屏幕上
	{
		src.left=bmpx;
		src.top=bmpy;
		src.right=bmpx+bmpw;
		src.bottom=bmpy+bmph;
		dst.left=x;
		dst.top=y;
		dst.right=w;
		dst.bottom=h;
		
		
		scrbuf.drawBitmap(bitmap,src,dst, paint);
	}
	
	Rect src=new Rect();
	Rect dst=new Rect();
	void drawBitmap(Bitmap bitmap,int bx,int by,int bw,int bh, int scrx, int scry, int scrw,int scrh)
	{
		src.left=bx;
		src.top=by;
		src.right=bx+bw;
		src.bottom=by+bh;
		dst.left=scrx;
		dst.top=scry;
		dst.right=scrw;
		dst.bottom=scrh;
		scrbuf.drawBitmap(bitmap,src,dst,paint);
	}
	
	
	
	
	//////////
}
