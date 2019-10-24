package com.xl.game.view;
import android.graphics.*;

//简单精灵类。。
public class Sprite
	{
		public int x,y;//坐标
		public int w,h;//每一帧的宽高
		Prect pbmp[]=new Prect[20];//图片缓冲
		public int ftp;//帧数
		public int ftps;//每秒显示多少帧
		int wid,hid;//计算多少行多少列

		int ftpn=0;//当前显示帧的序号
		public Sprite(Prect pbmp[], int bmpsize)
			{
				ftp = bmpsize;
				this.pbmp = pbmp;
			}
		public Sprite(Prect pbmp)
			{
				ftp = 1;
				this.pbmp[0] = pbmp;
				x = 0;
				y = 0;
				w = pbmp.pw;
				h = pbmp.ph;
				wid = 1;
				hid = 1;
			}

		public Sprite(Bitmap bmp)
			{
				ftp = 1;
				pbmp[0] = new Prect (bmp, 0, 0, bmp.getWidth (), bmp.getHeight ());
				this.pbmp[0].bitmap = bmp;
				x = 0;
				y = 0;

				w = bmp.getWidth ()   ;
				h = bmp.getHeight ()   ;

				wid = 1;
				hid = 1;

			}

		//矩阵模式创建精灵
		public  Sprite(Bitmap bitmap, int wsize, int hsize)
			{
				int size=0;
				int x=0,y=0;
				w = bitmap.getWidth () / wsize;
				h = bitmap.getHeight () / hsize;
				for (size = 0;size < w * h;size++)
					{
						this.pbmp[size] = new Prect (bitmap, x, y, w, h);
						x += w;
						if (x >= bitmap.getWidth ())
							{
								y += h;
							}
					}

			}


		public Paint paint=new Paint ();

		/*
		 将缓存中名为bitmap的图片，
		 从(x, y)开始 + 宽高为w, h的区域 ，
		 加载到序号为i的bmp缓冲中。
		 */
		public void Load(int i, Bitmap bitmap, int x,int y,int w,int h)
			{
				pbmp[i]=new Prect(bitmap,x,y,w,h);
			}

		/*
		 新建一个序号为i的bmp缓冲，
		 缓冲图片的宽高为w, h，
		 供加载图片使用。
		 */
		public void New(int i, int w,int h)
			{
				pbmp[i]=new Prect(Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888));

			}


		/*
		 绘制精灵
		 i：精灵id
		 x,y：图片坐标
		 mod：选择如下
		 BM_REVERSE //反向绘图，相当 于BM_TRANSPARENT＋反向绘图 （V1939）

		 */
		public void Draw(Canvas canvas,int i, int x, int y, int mod)
			{
switch(mod)
       {
				 case 0:
				canvas.drawBitmap (pbmp[i].bitmap, pbmp[i].rect, new Rect(x,y,x+pbmp[i].pw,y+pbmp[i].ph), paint);
				case 1:
				
				}
			}

		public void draw(Canvas canvas)
			{
				//canvas.drawBitmap(pbmp[ftpn].bmp,x,y,null);
				canvas.drawBitmap (pbmp[ftpn].bitmap, x, y, paint);
				ftpn++;
				if (ftpn >= ftp)ftpn = 0;
			}



/*

		public void bmpshowflip(Bitmap bmp, int x, int y, int tw, int w, int h, int type, int tx, int ty, int color)
			{

				Rect rect =new Rect (tx * bmp.getWidth () / tw, 
				ty * bmp.getWidth () / tw,
				(tx + w) * bmp.getWidth () / tw,
				(ty + h) * bmp.getWidth () / tw);
				Rect scrrect =new Rect (x * gameView_480.Screen_W / gameView_480.SCRW, y * gameView_480.Screen_W / gameView_480.SCRW,
				(x + w) * gameView_480.Screen_W / gameView_480.SCRW, (y + h) * gameView_480.Screen_W / gameView_480.SCRW);
				gameView_480.canvas.drawBitmap (bmp, rect, scrrect, gameView_480.paint);
			}
*/



		public boolean impact(int x, int y, int w, int h)
			{

				if ((this.x + this.w) > x && (this.x + this.w) < (x + w) && (this.y) > y && this.y < (y + h))
					return true;
				if ((this.x + this.w) > x && (this.x + this.w) < (x + w) && (this.y + this.h) > y && (this.y) < y + h)
					return true;
				return false;

			}
		public boolean impact(int x, int y)
			{
				if (x > this.x && y > this.y && x < this.x + this.w && y < this.y + this.h)
					return true;
				return false;
			}

	}
