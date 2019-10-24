package com.xl.game.view;
import android.view.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import java.io.*;
import android.os.*;
import android.view.SurfaceHolder.Callback;
import android.graphics.Paint.*;
import java.util.Formatter;

/*
 gameView
 做游戏专用

 */


public abstract class gameViewEx extends SurfaceView implements Callback,Runnable,opgame
{
  private Screen screen;
	private	int loads;//执行次数，第一次执行初始化，第二次执行恢复屏幕
	int timercd; //全局定时器变量控制
	private	Runnable runnable;

	//创建surface
	@Override
	public void surfaceCreated(SurfaceHolder p1)
	{
		uptime=System.currentTimeMillis();
    
		if(loads==0)
		{
			Screen_W=getWidth();
			Screen_H=getHeight();
			dealBitmap=Bitmap.createBitmap(Screen_W, Screen_H, Bitmap.Config.ARGB_8888);
			dealcanvas=new Canvas(dealBitmap);




			deal_paint=new Paint();
			deal_paint.setFilterBitmap(true);
			deal_paint.setAntiAlias(true);
			text_paint=new Paint();
			//canvas_paint=new Paint();
			draw_paint=new Paint();



			timercd=1;
			//	handler=new Handler();
			runnable=this;
			loads++;
			init();
			Log.e("XLLOG", "信息：game_init");
		}
		else
		{
			resume();
			Log.e("XLLOG", "信息：game_resume");
		}



	}

	//屏幕发生改变
	@Override
	public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4)
	{
		Screen_W=getWidth();
		Screen_H=getHeight();



	}

	//屏幕被销毁 但程序仍在运行
	@Override
	public void surfaceDestroyed(SurfaceHolder p1)
	{
		if(timercd==0)
		{
		timerStop();
		}
		pause();
		Log.e("XLLOG", "信息：game_pause");
	}
	


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
	_BACK=KeyEvent.KEYCODE_BACK;

	//绘制相关
	public Canvas dealcanvas;
	public Bitmap dealBitmap; //屏幕
	public Paint deal_paint; //屏幕画笔
	public Paint text_paint; //文字画笔
	public Paint draw_paint; //图片画笔


	//屏幕相关
	public static int SCRW,SCRH;//屏幕缓存宽度，屏幕缓存高度
	public static int Screen_W,Screen_H; //手机屏幕宽高
	public static int SCRP;

	//定时器相关
	public int ftp; //帧数
	int ftptime; //每一帧执行的时间
//	private Handler handler;



	public Context context;
	public boolean MS;//触屏移动检测

	public SurfaceHolder sfh;

	//	public Rect src,dst;

	public gameViewEx(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context=context;
		gameView_init(context);

	}



	public gameViewEx(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context=context;
		gameView_init(context);
	}


	public	gameViewEx(Context context)
	{

		super(context);
		this.context=context;

		gameView_init(context);
	}

	int gameView_init(Context context)
	{
		Log.e("XLLOG", "信息：gameView_init");
		this.setKeepScreenOn(true);
		sfh=this.getHolder();
		sfh.addCallback(this);

		//setscrsize(480,480*Screen_H/Screen_W);





		return 0;
	}


//将屏幕缓存绘制到屏幕上
	public void draw()
	{
		Canvas canvas=null;
		try{
			//锁定画布
			canvas=sfh.lockCanvas();
			if(canvas!=null){
				//canvas.drawColor (Color.WHITE);
				canvas.drawBitmap(dealBitmap, 0, 0, deal_paint);
			}
		}
		catch(Exception e){
			Log.e("XLLOG", "draw is Error!");
		}
		finally{
			//备注1
			if(canvas!=null)//备注2
			//解除并显示
				sfh.unlockCanvasAndPost(canvas);
		}
	}


//开启全局定时器
	public int timerOpen(int ftp)
	{

		if(timercd!=0){
			timercd=0;
			this.ftp=ftp;
			this.ftptime=1000/ftp;
			postDelayed(this, ftptime);
		}
		else{
			Log.e("XLLOG", "信息：定时器已经开启");
			return -1;
		}
		return 0;
	}

	public int timerStop()
	{
		if(timercd==0){
			timercd=-1;
			removeCallbacks(this);
			return 0;
		}
		else{
			//timercd=-1;
			Log.e("XLLOG", "信息：定时器已经关闭");
			return -1;
		}

//	return 0;
	}

	long uptime;
//定时器
	public void run()
	{
		//当运算时间小于系统时间时，直接进行逻辑处理
		if(uptime<System.currentTimeMillis())
		{
			uptime+=ftptime;
			timerCD();
		}


		postDelayed(this, ftptime);
		uptime+=ftptime;
		timerCD();
		screen.draw();
    draw();

	}

	

	public boolean onTouchEvent(MotionEvent event)
	{
		// 获得触摸的坐标
		int p1 = (int)event.getX() ;
		int p2 = (int)event.getY();
		int type=-1;
		//System.out.println("y坐标"+y);
		switch(event.getAction()){

			case MotionEvent.ACTION_DOWN:
				MS=false; type=MS_DOWN;


				break;
			case MotionEvent.ACTION_MOVE:
				MS=true; type=MS_MOVE;break;
			case MotionEvent.ACTION_UP:
				type=MS_UP;







				//event(type,x,y);
				//return false;
		}
		event(type, p1, p2);
		//return EVENT_A;
		return true;
	}

	void timerCD()
	{
		screen.run();
	}
	public abstract int init();
	public int event(int type, int p1, int p2)
	{
		screen.event(type,p1,p2);
		return 0;
	}
	public int pause()
	{
	  screen.pause();
		return 0;
	}
	
	public int resume()
	{
		screen.resume();
		return 0;
	}
	public abstract int exitApp();

	
	

	//检测触屏是否移动
	public boolean isMove()
	{
		return MS;
	}



	public Bitmap setscrsize(int screenw, int screenh)
	{
		this.SCRW=screenw;
		this.SCRH=screenh;
		this.SCRP=SCRW/80;
		this.text_paint.setTextSize(SCRP*5);
		Bitmap bitmap = Bitmap.createBitmap(SCRW, SCRH, Bitmap.Config.ARGB_8888);
		
		return bitmap;
//src .set (0, 0, SCRW, SCRH);
//dst .set(0, 0, Screen_W, Screen_H);

	}
	
	public void setScreen(Screen screen)
	{
		
		this.screen=screen;
		screen.init();
	}

	public int getscrWidth()
	{
		return SCRW;
	}

	public int getscrHeight()
	{
		return SCRH;
	}



	/*

	 int timetype=0;
	 public timer xl_time[]=new timer[10];
	 public int timercreate()
	 {

	 return ++timetype;

	 }



	 public void timerstart(
	 int timer_id,//定时器句柄
	 int time,//时长
	 int data,//数据id(每个定时器数据不一样)
	 Runnable timercd,//定时器函数
	 int type//是否循环，0不循环，1循环
	 )
	 {
	 xl_time[timer_id] = new timer ();
	 xl_time[timer_id].handler = new Handler ();
	 xl_time[timer_id].time = time;
	 xl_time[timer_id].handler. postDelayed (timercd, time);
	 xl_time[timer_id].loop = type;
	 //System.out.println("定时器"+timer_id);
	 }



	 public void timerdel(int timerid)
	 {
	 xl_time[timerid].handler.removeCallbacks (xl_time[timerid].runnable);
	 }

	 public void timerstop(int timeid)
	 {
	 xl_time[timeid].handler.removeCallbacks (xl_time[timeid].runnable);
	 }

	 */







	public String sprintf(String text,Object ... obj)
	{
		Formatter r0_Formatter = new Formatter();
		r0_Formatter.format(text, obj);
		return r0_Formatter.toString();
	}

	public	void bmpshowflip(Bitmap bmp, int x, int y, int tw, int w, int h, int type, int tx, int ty, int color)
	{

		Rect rect =new Rect(tx*bmp.getWidth()/tw, ty*bmp.getWidth()/tw, (tx+w)*bmp.getWidth()/tw, (ty+h)*bmp.getWidth()/tw);
		Rect scrrect =new Rect(x*Screen_W/SCRW, y*Screen_W/SCRW, (x+tw)*Screen_W/SCRW, (y+tw*h/w)*Screen_W/SCRW);
		dealcanvas.drawBitmap(bmp, rect, scrrect, deal_paint);
	}

	public int rand()
	{
		return (int)(Math.random()*1000000);
	}

	public void cls(int r, int g, int b)
	{

		dealcanvas.drawRGB(r, g, b);
	}

	public void cls(int a, int r, int g, int b)
	{
		dealcanvas.drawARGB(a, r, g, b);
	}

	/*
	 public void dtext(String text, int x, int y, int r, int g, int b, int type, int fontsize)
	 {
	 int ascenty;
	 text_paint.setARGB(255, r, g, b);
	 // FontMetrics对象
	 FontMetrics fontMetrics = text_paint.getFontMetrics();
	 ascenty=y-(int)fontMetrics.ascent;
	 //System.out.println("坐标"+ascenty);
	 dealcanvas.drawText(text, x, ascenty, text_paint);


	 }
	 */


	//算法 逆推得到top的值，然后减去top
	public void drawChar(char c,int x,int y,int r,int g,int b,int type)
	{
		char[] r1_char_A = new char[2];
		r1_char_A[0]=c;
		Paint.FontMetrics fontmetrics = this.text_paint.getFontMetrics();
		this.text_paint.setARGB(255, r, g, b);
		this.dealcanvas.drawText(r1_char_A, 0, 1, x, 
		y - ( fontmetrics.bottom+fontmetrics.top-this.text_paint.getTextSize())/2  -fontmetrics.top, this.text_paint);
	}
	public void dtext(char[] c,int x,int y,int r,int g,int b,int type)
	{
		Paint.FontMetrics r0_FontMetrics = this.text_paint.getFontMetrics();
		this.text_paint.setARGB(255, r, g, b);
		this.dealcanvas.drawText(c, 0, c.length,  x,  
		y - (r0_FontMetrics.bottom+r0_FontMetrics.top-this.text_paint.getTextSize())/ 2 -r0_FontMetrics.top, this.text_paint);
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
				y=(int)(y+this.text_paint.getTextSize()+10);
				tx=rx;
			}
			else
			{
				tx=x+(int)this.getCharWidth(text[i]);
			}
			if(!drect.contains(tx+(int)text_paint.getTextSize(), y))
			{
				y=y+(int)this.text_paint.getTextSize()+ 10;
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
		return this.text_paint.measureText(text, 0, 1);
	}

	public float getCharWidth(char[] text,int ptr)
	{
		return this.text_paint.measureText(text, ptr, ptr);
	}


	public static final int 
	TEXT_LEFT=0,
	TEXT_RIGHT=1,
	TEXT_CENTER=2;

	public void draw_text(String text, int x, int y, int h, int type)
	{
		int tx,ty;
		FontMetrics font= text_paint.getFontMetrics();
		float topY = y+font.top;
		float ascentY = y+font.ascent;
		float descentY = y+font.descent;
		float bottomY = y+font.bottom;
		ty=(int)(y+(h-(-topY+bottomY))/2);
		if(type==TEXT_LEFT)
		{
			dealcanvas.drawText(text, x, ty, text_paint);

		}
		else if(type==TEXT_CENTER)
		{
			dealcanvas.drawText(text, x-text_paint.measureText(text)/2, ty, text_paint);
		}
		else if(type==TEXT_RIGHT)
		{
			dealcanvas.drawText(text, x-text_paint.measureText(text), ty, text_paint);
		}
	}

	public void setColor(int r, int g, int b)
	{
		draw_paint.setARGB(255, r, g, b);
	}

	public void setColor(int a, int r, int g, int b)
	{
		draw_paint.setARGB(a, r, g, b);
	}

	public void drect(int x, int y, int w, int h, int r, int g, int b)
	{
		draw_paint.setARGB(255, r, g, b);
		dealcanvas.drawRect(x, y, x+w, y+h, draw_paint);
	}

	/*
	 参数一为渐变起初点坐标x位置，
	 参数二为y轴位置，
	 参数三和四分辨对应渐变终点，
	 最后参数为平铺方式，这里设置为镜像
	 */
	void drawShader(int x,int y,int w,int h,int color1,int color2,int type)
	{

		LinearGradient lg=new LinearGradient(0,0,100,100,color1,color2,Shader.TileMode.MIRROR);  
		Paint p=new Paint();
		p.setShader(lg);
		dealcanvas.drawRect(x,y,x+w,y+h,p);
	}


	Bitmap convert(Bitmap bitmap,int x, int y, int c)
	{
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		//Bitmap newb = Bitmap.createBitmap(ww, wh, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		//Canvas cv = new Canvas(newb);
		Matrix matrix = new Matrix();

		//m.postScale(1, -1); //镜像垂直翻转
		//m.postScale(-1, 1); //镜像水平翻转
		matrix.postRotate(-90); //旋转-90度
		matrix.setRotate(c ,x,y); //设置旋转角度和旋转中心
		matrix.postTranslate(x,y); //中心缩放
		Bitmap newb = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		dealcanvas.drawBitmap(bitmap,  matrix, null);
		return newb;
	}

	public Bitmap ReadBitMap(Context context, int resId)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig=Bitmap.Config.RGB_565;
		opt.inPurgeable=true;
		opt.inInputShareable=true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public abstract int winEvent(Screen id, int type);






	//
}
