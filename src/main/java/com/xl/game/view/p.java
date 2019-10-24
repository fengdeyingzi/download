package com.xl.game.view;
import android.graphics.*;

//简单精灵类。。
public class p
	{
		int x,y;//坐标
		int w,h;//每一帧的宽高
		Prect pbmp[]=new Prect[20];//图片
		int ftp;//帧数
		int ftps;//每秒显示多少帧
		int wid,hid;//计算多少行多少列
		
		int ftpn=0;//当前显示帧的序号
		p(Prect pbmp[],int bmpsize)
			{
				ftp=bmpsize;
				this.pbmp=pbmp;
			}
		 p(Prect pbmp)
		{
			ftp=1;
			this.pbmp[0]=pbmp;
			x=0;
			y=0;
			w=pbmp.pw;
			h=pbmp.ph;
			wid=1;
			hid=1;
		}
		
	 p(Bitmap bmp)
		{
			ftp=1;
			pbmp[0]=new Prect(bmp,0,0,bmp.getWidth(),bmp.getHeight());
			this.pbmp[0].bitmap=bmp;
			x=0;
			y=0;
			
			w=bmp.getWidth()   ;
			h=bmp.getHeight()   ;
			
			wid=1;
			hid=1;
			
		}

			//矩阵模式创建精灵
   p(Bitmap bitmap,int wsize,int hsize)
   {
		 int size=0;
		 int x=0,y=0;
	   w=bitmap.getWidth()/wsize;
		 h=bitmap.getHeight()/hsize;
		 for(size=0;size<w*h;size++)
		 {
			 this.pbmp[size]=new Prect(bitmap,x,y,w,h);
			 x+=w;
			 if(x>=bitmap.getWidth())
			 {
				 y+=h;
			 }
		 }
		 
   }
	 
	 
	 Paint paint=new Paint();
	 void draw(Canvas canvas)
	 {
		 //canvas.drawBitmap(pbmp[ftpn].bmp,x,y,null);
		 canvas.drawBitmap(pbmp[ftpn].bitmap,x,y, paint);
		 ftpn++;
		 if(ftpn>=ftp)ftpn=0;
	 }
	 /*
		void bmpshowflip(Bitmap bmp,int x,int y,int tw,int w,int h,int type,int tx,int ty,int color)
			{

				Rect rect =new Rect(tx*bmp.getWidth()/tw, 
														ty * bmp.getWidth()/tw,
														(tx+w)* bmp.getWidth()/tw,
														(ty+h)* bmp.getWidth()/tw  );
				Rect scrrect =new Rect(x*gameView_480.Screen_W/gameView_480.SCRW,y*gameView_480.Screen_W/gameView_480.SCRW,
				(x+w)*gameView_480.Screen_W/gameView_480.SCRW,(y+h)*gameView_480.Screen_W/gameView_480.SCRW );
				gameView_480.canvas.drawBitmap(bmp,rect,scrrect,gameView_480.paint);
			}
			*/
			
			
			
		boolean impact(int x,int y,int w,int h)
		{
		/*	if(this.x+this.w>x && this.x+this.w<x+w && this.y>y && this.y<y+h)
				return true;
				*/
		if( (this.x+this.w)>x && (this.x+this.w)<(x+w) && (this.y)>y && this.y<(y+h))
			return true;
		if((this.x+this.w)>x && (this.x+this.w)<(x+w) && (this.y+this.h)>y && (this.y)<y+h)
			return true;
			return false;
			
		}

	}
