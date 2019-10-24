package com.xl.game.view;
import android.graphics.*;
import android.view.View;
import android.view.SurfaceView;
import android.view.KeyEvent;
import android.app.Activity;
import android.util.Log;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import java.util.Formatter;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import android.os.Environment;
//import com.xl.game.tool.Logcat;

/*
 屏幕类，控制整个屏幕或部分屏幕，不过目前用来控制整个屏幕。。。
 风的影子
 */


abstract public  class Screen 
	{
		public opgame gameview;
		public Context context;
		private int x,y,w,h;//屏幕显示区域
		public int SCRX,SCRY,SCRW,SCRH,SCRP;//图片区域(真正的屏幕大小)
		public Canvas canvas;//当前屏幕
		public Bitmap bitmap;//当前屏幕缓存
		Bitmap dealbitmap;
		public Canvas realCanvas;//上层屏幕

		public Paint paint;//屏幕画笔
		public Paint paint_canvas;
		public Paint paint_dealcanvas;
		public Paint paint_text;
		public Paint paint_graph;
		public Paint paint_bitmap;

		MediaPlayer player ;

    //public Activity activity; //当前activity
		//public View view;

		private int show_type;//显示方式：
		public static  final int 
		SHOW_SCALE=0,//等比例显示
		SHOW_SCALE_WIDTH=1,//以图片宽度拉伸对齐
		SHOW_SCALE_HEIGHT=2,//以图片高度拉伸对齐
		SHOW_SCALE_ALL=3,//等比例全屏显示

		SHOW_EXTRUDE=4,//拉伸全屏
		SHOW_CONSTANT=10;//不变



		public	Rect src,dst;


		public static final int 

		KY_DOWN=0,
		KY_UP=1,
		MS_DOWN=2,
		MS_UP=3,

		MS_MOVE=12,
		_UP=KeyEvent.KEYCODE_DPAD_UP,//上键
		_DOWN=KeyEvent.KEYCODE_DPAD_DOWN,//下键
		_LEFT=KeyEvent.KEYCODE_DPAD_LEFT,//左键
		_RIGHT=KeyEvent.KEYCODE_DPAD_RIGHT,//右键
		_SLEFT=KeyEvent.KEYCODE_MENU,//菜单键
		_SRIGHT=KeyEvent.KEYCODE_BACK,//返回键
		_SELECT=KeyEvent.KEYCODE_MENU,//菜单键
		_MENU=KeyEvent.KEYCODE_MENU,//菜单键
		_HOME=KeyEvent.KEYCODE_HOME,//home键
		_BACK=KeyEvent.KEYCODE_BACK;//返回键

		public int id;//屏幕id



		public Screen()
			{
				src=new Rect();
				dst=new Rect();
				paint=new Paint();
				paint.setAntiAlias(false);
				paint.setFilterBitmap(true);
				//paint.setDither(false);

				paint_canvas=new Paint();
				//设置不抗锯齿
				paint_canvas.setAntiAlias(false);
				paint_canvas.setFilterBitmap(true);
				//设置无抖动
				//paint_canvas.setDither(false);

				paint_dealcanvas=new Paint();
				paint_dealcanvas.setFilterBitmap(true);
				
				paint_text=new Paint();
				paint_text.setTextSize(20);
				paint_text.setAntiAlias(true);
				

				paint_graph=new Paint();
				paint_bitmap=new Paint();
				player=new MediaPlayer();
			}

		//最简单以及常用的方法，参数：屏幕缓存，真正的屏幕
		public Screen(Bitmap scrbitmap,Bitmap dstbitmap,int type)
			{
				this();
				Log.e("XL", "Screen创建");
				bitmap=scrbitmap;
				//设置不透明
			//	bitmap.setHasAlpha(false);

				dealbitmap=dstbitmap;
			//	dealbitmap.setHasAlpha(false);
				canvas=new Canvas(bitmap);


				realCanvas=new Canvas(dealbitmap);

				//先计算出宽高比的256倍
				int scrbufscale = (scrbitmap.getWidth()<<8)/scrbitmap.getHeight();
				int dstbufscale = (dstbitmap.getWidth()<<8)/dstbitmap.getHeight();
				if(type==SHOW_SCALE)//等比例
				//当屏幕宽高比大于图片宽高比，以图片高度为准
					{

						SCRX=0;
						SCRY=0;

						SCRW=scrbitmap.getWidth();
						SCRH=scrbitmap.getHeight();
						SCRP=SCRW/80;
						paint.setTextSize(SCRP*5);

						if(scrbufscale<dstbufscale)
							{
								x=0;
								y=(dstbitmap.getWidth()-scrbitmap.getWidth()*dstbitmap.getHeight()/scrbitmap.getHeight())/2;
								w=scrbitmap.getWidth()*dstbitmap.getHeight()/scrbitmap.getHeight();
								h=dstbitmap.getHeight();

								src.set(0, 0, scrbitmap.getWidth(), scrbitmap.getHeight());
								//居中
								dst.set(0,
								(dstbitmap.getWidth()-scrbitmap.getWidth()*dstbitmap.getHeight()/scrbitmap.getHeight())/2,
								scrbitmap.getWidth()*dstbitmap.getHeight()/scrbitmap.getHeight(),
								y+dstbitmap.getHeight());

							}
						else//图片宽度为准
							{
								x=0;
								y=(dstbitmap.getHeight()-scrbitmap.getHeight()*dstbitmap.getWidth()/scrbitmap.getWidth())/2;
								w=dstbitmap.getWidth();
								h=scrbitmap.getHeight()*dstbitmap.getWidth()/scrbitmap.getWidth();

								src.set(0, 0, scrbitmap.getWidth(), scrbitmap.getHeight());
								dst.set(0,
								(dstbitmap.getHeight()-scrbitmap.getHeight()*dstbitmap.getWidth()/scrbitmap.getWidth())/2,
								dstbitmap.getWidth(),
								y+scrbitmap.getHeight()*dstbitmap.getWidth()/scrbitmap.getWidth());
							}
					}


				//等比例全屏
				//当屏幕宽高比大于图片宽高比，以图片宽度为准
				//当屏幕宽高比小于图片宽高比，以图片高度为准
				else if(type==SHOW_SCALE_ALL)
					{
						if(dstbufscale>scrbufscale)//
							{
								//dst.h/dst.w
								src.set(0,
								scrbitmap.getHeight()-(scrbitmap.getWidth()*dstbitmap.getHeight()/dstbitmap.getWidth())/2
								, scrbitmap.getWidth()
								, scrbitmap.getWidth()*dstbitmap.getWidth()/dstbitmap.getHeight()
								);
								dst.set(0,
								0,
								dstbitmap.getWidth(),
								y+dstbitmap.getHeight());
							}

						else
							{
								src.set(
								scrbitmap.getWidth()-(scrbitmap.getHeight()*dstbitmap.getHeight()/dstbitmap.getWidth())/2,
								0,
								scrbitmap.getHeight()*dstbitmap.getHeight()/dstbitmap.getWidth(),
								scrbitmap.getHeight()
								);

								dst.set(0, 0, dstbitmap.getWidth(), y+dstbitmap.getHeight());
							}

					}

				else if(type==SHOW_EXTRUDE)//拉伸全屏
					{
						SCRW=scrbitmap.getWidth();
						SCRH=scrbitmap.getHeight();
						src.set(0, 0, scrbitmap.getWidth(), scrbitmap.getHeight());
						dst.set(0, 0, dstbitmap.getWidth(), dstbitmap.getHeight());
					}

				else if(type==SHOW_CONSTANT)//不变
					{
						SCRW=scrbitmap.getWidth();
						SCRH=scrbitmap.getHeight();
						src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());

						dst.set(0, 0, realCanvas.getWidth(), realCanvas.getHeight());

					}


			}


//直接使用一个屏幕缓存创建屏幕，拉伸显示
		public Screen(Bitmap bitmap)
			{
				this();
				this.x=0;
				this.y=0;
				this.w=bitmap.getWidth();
				this.h=bitmap.getHeight();
				src.set(x, y, (x+w), (y+h));
				dst.set(x, y, (x+w), (y+h));
				canvas=new Canvas(bitmap);
				paint=new Paint();


			}


		//原始尺寸创建
		public Screen(int x,int y,int w,int h)
			{
				this();
				this.x=x;
				this.y=y;
				this.w=w;
				this.h=h;

				bitmap=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
				canvas=new Canvas(bitmap);
				this.paint=new Paint();
				src.set(x, y, (x+w), (y+h));
				dst.set(x, y, (x+w), (y+h));
			}
//创建屏幕，参数：屏幕缓存，显示位置x,y,w,h，显示类型
			/*
		public Screen(Bitmap bitmap,int x,int y,int w,int h,int type)
			{
				this();
				this.bitmap=bitmap;
				this.canvas=new Canvas(bitmap);
				this.paint=new Paint();

				if(type==SHOW_SCALE)//等比例
					{
						//当屏幕宽高比小于图片宽高比，以图片高度为准
						if(bitmap.getWidth()*10/bitmap.getHeight()>w*10/h)
							{
								this.w=w;
								this.h=w*bitmap.getHeight()/bitmap.getWidth();
								this.x=0;
								this.y=y+(h-this.h)/2;
								src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
								dst.set(x, y, x+w, y+h);
								this.SCRX=x;
								this.SCRY=y;
								this.SCRW=bitmap.getWidth();
								this.SCRH=bitmap.getHeight();

							}
						else//当屏幕宽高比大于图片宽高比，以图片宽度为准
							{
								this.h=h;
								this.w=h*bitmap.getWidth()/bitmap.getHeight();
								this.x=x+(w-this.w)/2;
								src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
								dst.set(x, y, x+w, y+h);
								this.SCRX=x;
								this.SCRY=y;
								this.SCRW=bitmap.getWidth();
								this.SCRH=bitmap.getHeight();

							}
					}
				//等比例全屏
				//当屏幕宽高比大于图片宽高比，以图片宽度为准
				//当屏幕宽高比小于图片宽高比，以图片高度为准
				else if(type==SHOW_SCALE_ALL)
					{
						if(w*10/h>bitmap.getWidth()*10/bitmap.getHeight())
							{
								this.x=x;
								this.y=y;
								this.w=w;
								this.h=h;
								this.SCRX=0;
								this.SCRW=bitmap.getWidth();
								this.SCRH=SCRW*h/w;
								this.SCRY=(bitmap.getHeight()-SCRH)/2;
							}
					}

				else if(type==SHOW_EXTRUDE)//拉伸全屏
					{
						this.x=x;
						this.y=y;
						this.w=w;
						this.h=h;
						this.SCRX=0;
						this.SCRY=0;
						this.SCRW=bitmap.getWidth();
						this.SCRH=bitmap.getHeight();
						src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
						dst.set(x, y, x+w, y+h);

					}

				else if(type==SHOW_CONSTANT)//不变
					{
						this.x=x;
						this.y=y;
						this.w=bitmap.getWidth();
						this.h=bitmap.getHeight();
						this.SCRX=x;
						this.SCRY=y;
						this.SCRW=bitmap.getWidth();
						this.SCRH=bitmap.getHeight();
					}

			}
			*/

		public Screen(int x,int y,int w,int h,int bmpw,int bmph)//拉伸显示
			{
				this();
				this.x=x;
				this.y=y;
				this.w=w;
				this.h=h;
				this.SCRX=0;
				this.SCRY=0;
				this.SCRW=bmpw;
				this.SCRH=bmph;
				src.set(0, 0, bmpw, bmph);
				dst.set(x, y, x+w, y+h);
				this.bitmap=Bitmap.createBitmap(bmpw, bmph, Bitmap.Config.ARGB_8888);
				this.paint=new Paint();
				this.canvas=new Canvas(bitmap);
			}

		//设置屏幕缓存
		public 	void setscrbuf(Canvas canvas)
			{
				this.canvas=canvas;
			}
		//获取屏幕缓存
		public Canvas getscrbuf()
			{
				return canvas;
			}
		public void setscrbuf(Bitmap bitmap)
			{
				this.realCanvas=new Canvas(bitmap);
			}

		public int getx(int p0)
			{
				return (p0-x)*SCRW/w;
			}

		public int gety(int p1)
			{
				return (p1-y)*SCRH/h;

			}


		public void setid(int id)
			{
				this.id=id;
			}

		public void refScreen()//刷新屏幕
			{
				realCanvas.drawBitmap(bitmap, src, dst, paint);
			}


		//定时器执行函数
		public abstract void run();
		//绘制函数(将屏幕缓存绘制到屏幕上
		public void drawScreen()
			{
				realCanvas.drawBitmap(bitmap, src, dst, paint);
			}

		//绘制函数，绘制整个屏幕和刷新
		public abstract void draw();
		
		//加载资源
		public abstract int load();
		//入口函数
		public abstract int init();

		

		//按键及触屏事件
		public abstract  int event(int type,int p1,int p2);
		//暂停
		public abstract int pause();
		//恢复
		public abstract int resume();
		//销毁
		public abstract int exit();

		public	int rand()
			{
				return (int)(Math.random()*1000000);
			}

		public	void cls(int r,int g,int b)
			{
				//paint.setARGB(255,r,g,b);
				//canvas.drawARGB(255,r,g,b);
				canvas.drawRGB(r, g, b);
			}

		public	void cls(int a,int r,int g,int b)
			{
				canvas.drawARGB(a, r, g, b);
			}


		//显示一个图片切片
		public void drawBitmap(Bitmap bitmap,int tx,int ty,int tw,int th,int x,int y)
			{
				Rect res ,dst;
				res=new Rect(tx, ty, tx+tw, ty+th);
				dst=new Rect(x, y, x+tw, y+th);
				canvas.drawBitmap(bitmap, res, dst, paint);

			}
			/*
		public void draw_bitmap(Bitmap bitmap,int x,int y,int w,int h,int px,int py)
			{
				this.canvas.drawBitmap(bitmap, new Rect(x, y, (x+w), (y+h)), new Rect(px, py, (px+w), (py+h)), this.paint);
			}
			*/
		//以图片宽度为准显示一张图片
		public void showBitmapW(Bitmap bitmap)
			{
				int x=0;
				Rect scr=new Rect()
				,dst=new Rect();
				int tw=bitmap.getWidth();
				int th=bitmap.getHeight();
				int y=bitmap.getWidth()*(src.bottom-src.top)/(src.right-src.left) ;
				float scr_scale=(float)SCRW/SCRH;
				float img_scale=(float)bitmap.getWidth()/bitmap.getHeight();
				//如果图片宽高比大于屏幕宽高比，以图片宽高为准(图片全屏显示，
				if(img_scale>scr_scale)
					{
						scr=new Rect(0, 0, tw, th);
						dst=new Rect(0, (SCRH-th*SCRW/tw)/2, SCRW, (SCRH-th*SCRW/tw)/2+th*SCRW/tw);
					}
				//如果屏幕宽高比大于图片宽高比，以屏幕宽高为准(屏幕全屏显示
				else
					{
						scr=new Rect(0, (th-SCRH*tw/SCRW)/2, tw, (th-SCRH*tw/SCRW)/2+SCRH*tw/SCRW);
						dst=new Rect(SCRX, SCRY, SCRW, SCRH);
					}
				//显示在屏幕上的图片高度
				//int	th= bitmap.getWidth() * SCRH/SCRW;


				;

				canvas.drawBitmap(bitmap, scr, dst, paint);
			}

		public	void drect(int x,int y,int w,int h,int r,int g,int b)
			{
				paint_graph.setARGB(255, r, g, b);
				//paint.setAlpha(255);
				canvas.drawRect(x, y, (x+w), (y+h), paint_graph);
			}

		public	void drect(int x,int y,int w,int h,int a,int r,int g,int b)
			{
				paint_graph.setARGB(a, r, g, b);
				//paint.setAlpha(255);
				canvas.drawRect(x, y, (x+w), (y+h), paint_graph);
			}
			
			//算法 逆推得到top的值，然后减去top
		public void drawChar(char c,int x,int y,int r,int g,int b,int type)
			{
				char[] r1_char_A = new char[2];
				r1_char_A[0]=c;
				Paint.FontMetrics fontmetrics = this.paint.getFontMetrics();
				this.paint.setARGB(255, r, g, b);
				this.canvas.drawText(r1_char_A, 0, 1, x, 
				y - ( fontmetrics.bottom+fontmetrics.top-this.paint.getTextSize())/2  -fontmetrics.top, this.paint);
			}
		public void dtext(char[] c,int x,int y,int r,int g,int b,int type)
			{
				Paint.FontMetrics r0_FontMetrics = this.paint.getFontMetrics();
				this.paint.setARGB(255, r, g, b);
				this.canvas.drawText(c, 0, c.length,  x,  
				y - (r0_FontMetrics.bottom+r0_FontMetrics.top-this.paint.getTextSize())/ 2 -r0_FontMetrics.top, this.paint);
			}

		public void dtextex(char[] text,int x,int y,int rx,int ry,int rw,int rh,int r,int g,int b) 
			{
				Rect drect = new Rect(rx, ry, (rx+rw), (ry+rh));
				int i = 0;

				while(i<text.length)
					{
						int tx;
						if(drect.contains(x, y))
							{
								this.drawChar(text[i], x, y, r, g, b, 0);
							}
						if(text[i]=='\n')
							{
								y=(int)(y+this.paint.getTextSize()+10);
								tx=rx;
							}
						else
							{
								tx=x+(int)this.getCharWidth(text[i]);
							}
						if(!drect.contains(tx+(int)paint.getTextSize(), y))
							{
								y=y+(int)this.paint.getTextSize()+ 10;
								x=rx;
							}
						else
							{
								x=tx;
							}
						if(y<=this.SCRH)
							{
								i=(i+1);
							}
						else
							{
								return;
							}
					}
			}

		public float getCharWidth(char c)
			{
				char[] text = new char[2];
				text[0]=c;
				return this.paint.measureText(text, 0, 1);
			}

		public float getCharWidth(char[] text,int ptr)
			{
				return this.paint.measureText(text, ptr, ptr);
			}

		/**
		 * 从Assets中读取图片
		 */
		public Bitmap getImageFromAssetsFile(Context context,String fileName)
			{
				Bitmap image = null;
				AssetManager am = context.getResources().getAssets();
				try
					{
						InputStream is = am.open(fileName);
						image=BitmapFactory.decodeStream(is);
						is.close();
					}
				catch(IOException e)
					{
						e.printStackTrace();
					}
				return image;
			}

		public void soundload(Context context,String filename)
			{
				AssetManager asset = context.getResources().getAssets();
				//new AssetManager();

				try
					{
						AssetFileDescriptor r4_AssetFileDescriptor = asset.openFd(filename);
						try
							{
									player.setDataSource(r4_AssetFileDescriptor.getFileDescriptor(), r4_AssetFileDescriptor.getStartOffset(), r4_AssetFileDescriptor.getLength());
								//player.setDataSource(r4_AssetFileDescriptor.getFileDescriptor());
								player.prepare();
								
							}
						catch(IllegalArgumentException e)
							{}
						catch(IllegalStateException e)
							{}
						catch(IOException e)
							{}

					}
				catch(IllegalStateException r1_IllegalStateException)
					{
					}
				catch(IOException r1_IOException)
					{
						r1_IOException.printStackTrace();
						//Logcat.e("文件不存在");
					}

			}

		public void soundplay()
			{

				player.start();
			}

		public void soundpause()
			{
				player.pause();
			}

		public void soundresume()
			{
				player.start();
			}

		public void soundstop()
			{
				player.stop();
			}


		public String sprintf(String text,Object ... obj)
			{
				Formatter r0_Formatter = new Formatter();
				r0_Formatter.format(text, obj);
				return r0_Formatter.toString();
			}


		public void sleep(long ms)
			{
				try
					{
						Thread.sleep(ms);
					}
				catch(InterruptedException e)
					{}
			}
			
	public String getsdcard()
	{
		return Environment.getExternalStorageDirectory().toString();
		//	return path.getAbsolutePath();
	}
			
	public String getSDPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if (sdCardExist)
			{
				sdDir = Environment.getExternalStorageDirectory();//获取sd卡目录
			}
		else 
		{
			return null;
		}
			return sdDir.toString();
	 }
			
			
	public String save_png(String filename, Bitmap bitmap)
	{
		String newfilename=getSDPath()+ filename;
		try 
		{
			FileOutputStream out = new FileOutputStream(newfilename);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

return newfilename;
	}

	
			


		public void setgameView(opgame game)
			{
				this.gameview=game;
			//	this.context=gameview.context;
			}
		public void setContext(Context context)
		{
			this.context=context;
		}

		public void pushEvent(int type)
			{
				if(gameview==null)
					{
						Log.e("XLLOG", "信息：gameview为空值");
					}
				gameview.winEvent(this, type);
			}

	}


	



/*

public  class Screen
{
	public View view;
	public SurfaceView surfaceview;
	public int x,y,w,h;//屏幕显示区域
	public int bmpx,bmpy,bmpw,bmph;//图片区域
	public Canvas canvas;//当前屏幕
	public Bitmap bitmap;//当前屏幕缓存
	public Canvas scrbuf;//上层屏幕
	public Paint paint;
	
	
	
		int type;//显示方式：
		public static  final int 
		SHOW_SCALE=0,//等比例显示
		SHOW_SCALE_WIDTH=1,//以图片宽度拉伸对齐
		SHOW_SCALE_HEIGHT=2,//以图片高度拉伸对齐
		SHOW_SCALE_ALL=3,//等比例全屏显示

		SHOW_EXTRUDE=4,//拉伸全屏
		SHOW_CONSTANT=10;//不变
		
	
	
public	Rect src,dst;
	
public Screen()
	{
		
	}
	
	
	public Screen(Bitmap bitmap,int x,int y,int w,int h,int type)
	{
		this.bitmap=bitmap;
		this.canvas=new Canvas(bitmap);
		this.paint=new Paint();
		
		if(type==SHOW_SCALE)
		{
			if(bitmap.getWidth()*10/bitmap.getHeight()>w*10/h)
				{
					this.w=w;
					this.h=w* bitmap.getHeight()/bitmap.getWidth();
					this.x=0;
					this.y=y+(h-this.h)/2;
					src.set(0,0,bitmap.getWidth(),bitmap.getHeight());
					dst.set(x,y,x+w,y+h);
				}
			else
			{
				this.h=h;
				this.w=h*bitmap.getWidth()/bitmap.getHeight();
				this.x=x+(w-this.w)/2;
				src.set(0,0,bitmap.getWidth(),bitmap.getHeight());
				dst.set(x,y,x+w,y+h);
			}
		}
		//等比例全屏
		//当屏幕宽高比大于图片宽高比，以图片宽度为准
		//当屏幕宽高比小于图片宽高比，以图片高度为准
		else if(type==SHOW_SCALE_ALL)
			{
				if(w*10/h > bitmap.getWidth()*10/bitmap.getHeight())
					{
           this.x=x;
					 this.y=y;
					 this.w=w;
					 this.h=h;
					 this.bmpx= 0;
					 this.bmpw= bitmap.getWidth();
					 this.bmph= bmpw* h/w;
					 this.bmpy= (bitmap.getHeight()-bmph)/2;
					}
			}
		
		else if(type==SHOW_EXTRUDE)//拉伸全屏
		{
			this.x=x;
			this.y=y;
			this.w=w;
			this.h=h;
			this.bmpx=0;
			this.bmpy=0;
			this.bmpw=bitmap.getWidth();
			this.bmph=bitmap.getHeight();
			src.set(0,0,bitmap.getWidth(),bitmap.getHeight());
			dst.set(x,y,x+w,y+h);
			
		}
		
		else if(type==SHOW_CONSTANT)//不变
		{
			this.x=x;
			this.y=y;
			this.w=bitmap.getWidth();
			this.h=bitmap.getHeight();
			this.bmpx=x;
			this.bmpy=y;
			this.bmpw=bitmap.getWidth();
			this.bmph=bitmap.getHeight();
		}
		
	}
	
	public Screen(int x,int y,int w,int h,int bmpw,int bmph)//拉伸显示
	{
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.bmpx=0;
		this.bmpy=0;
		this.bmpw=bmpw;
		this.bmph=bmph;
		src.set(0,0,bmpw,bmph);
		dst.set(x,y,x+w,y+h);
		this.bitmap=Bitmap.createBitmap(bmpw,bmph,Bitmap.Config.ARGB_8888);
		this.paint=new Paint();
		this.canvas=new Canvas(bitmap);
	}
	
	//设置上层屏幕(必须被调用)
	public void setscrbuf(Canvas canvas)
	{
		this.scrbuf=canvas;
	}
	
	public void show()//将当前屏幕显示出来
	{
		
		scrbuf.drawBitmap(bitmap,src,dst,paint);
	}
	
	//绘制函数
	public  void draw()
	{}
	
	//按键及触屏事件
		public  int event(int type,int p1,int p2)
		{
			return 0;
		}
}

*/
