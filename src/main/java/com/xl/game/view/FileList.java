package com.xl.game.view;
import android.view.KeyEvent;
import java.io.File;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.Context;
import java.util.Vector;
import android.util.Log;
import com.xl.game.math.Str;

/*
view
文件浏览器

*/


public class FileList
{
	OnClickListener listener;
	Context context;
	File curfile;
	String path;
		Vector <Item> filelist;
	Bitmap[] icon;
	private int ic_load;
	int gb;
	
	int listy;
	int x,y,w,h;
	int itemHeight;
	int type;//显示的类型
	int size; //文件数目
	public static final int 
	_GBDOWN=0,
	_GBUP=1; //光标按下，光标释放
	
	public static final int
	_MUSIC=1,
	_ZIP=2,
	_TXT=3,
	_VIDEO=4,
	_RC=5,
	_IMG=6;
	
	
	class Item
	{
				String filename;//文件名
				String hz; //后缀
				int type;  //文件类型
				
				
				Item(String name)
				{
					this.filename=name;
					
					char cn[]=filename.toCharArray();
					int hptr=Str.strrchr(filename.toCharArray(),'.');
					
					if(Str.strhz(cn,".mp3".toCharArray())==0)
					{
						type=_MUSIC;
					}
					else if(Str.strhz(cn,".png".toCharArray())==0)
					{
						type=_IMG;
					}
					else if(Str.strhz(cn,".txt".toCharArray())==0)
					{
						type=_TXT;
					}
					else 
					{
						type=0;
					}
					
				}
	}
	
	
	Paint paint_text,paint_icon,paint_bg,paint_item;
	
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

	//初始化
public	FileList(Context context)
	{
		filelist=new Vector<Item>();
		listy=0;
		itemHeight=60;
		type=_GBUP;
		gb=0;
		paint_bg=new Paint();
		paint_bg.setColor(0xfff0f0f0);
		paint_item=new Paint();
		paint_icon=new Paint();
		paint_text=new Paint();
		paint_text.setTextSize(30);
		icon=new Bitmap[30];
	}
	
	//设置视图的位置x,y,w,h
public	void setView(int x,int y,int w,int h)
	{
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
	}
	//获取文件列表
	public int fileLoad(String filepath)
	{
		
		File file=new File(filepath);
		this.path=filepath;
		String list[]=file.list();
		for(String str:list)
		{
			filelist.addElement(new Item(str));
		}
		size=filelist.size();
		return 0;
	}
	//获取文件路径
	public String getpath()
	{
		return path;
	}
	
	//获取文件名
	public String getfilename(int n)
	{
		if(n>=filelist.size())return null;
		return filelist.elementAt(n).filename;
	}
	
	//设置指定后缀的文件图标
	public void addIcon(String hz,Bitmap bmp)
	{
		//this.hz[ic_load]=hz;
		this.icon[ic_load]=bmp;
		ic_load++;
	}
	//设置显示的文件类型
	public void setFileType(String hz)
	{
		
	}
	//设置是否显示文件夹
	public void setDistory(boolean n)
	{
		
	}
	
	//设置只显示文件夹
	public void isDistorys()
	{
		
	}
	//设置只显示文件
	public void isFiles()
	{
		
	}
	//显示列表
	public int drawList(Canvas canvas)
	{
		int starty;
		int startn;
		int listn;
		if(listy<0)
		{
			starty=listy%itemHeight;//绘制的起始y坐标
			startn=-listy/itemHeight;//绘制的起点id
			listn=h/itemHeight;//绘制的列表数
			
		}
		else
		{
			starty=listy;
			startn=0;
			listn= (h-starty)/itemHeight;
		}
			Log.e("XLLOG","信息："+y+" "+listy);
		//显示背景
		canvas.drawRect(x,y,x+w,y+h,paint_bg);
		//显示光标
		canvas.drawRect(x,y+starty+gb*itemHeight-startn*itemHeight,480,y+starty+gb*itemHeight+itemHeight-startn*itemHeight,paint_item);
		for(int i=0;i<listn;i+=1)
		{
			if((startn+i)>=size)
				break;
			Item item=filelist.elementAt(startn + i);
			
			//显示列表
			canvas.drawRect(x,y+starty+i*itemHeight,x+w,y+starty+i*itemHeight+1,paint_item);
			//显示文字
			canvas.drawText(item.filename, x+50,y+starty+i*itemHeight+30,paint_text);
			//Log.e("XLLOG","信息："+filelist.elementAt(startn+i).filename);
			//显示图标
			if(icon[item.type]!=null)
			canvas.drawBitmap(icon[item.type],x+2,starty+i*itemHeight+2,paint_icon);
			
		}
		
		
		
		return 0;
	}
	//设置监听
	public void setOnClickListener(OnClickListener listener)
	{
		this.listener=listener;
	}
	
	int ux,uy;
	//event事件
	public int event(int type,int p1,int p2)
	{
		int x=p1;
		int y=p2;
		if(type==MS_DOWN)
		{
			//ux=listx;
			uy=y;
			this.type=_GBDOWN;
		}
		else if(type==MS_MOVE)
		{
			listy+=(y-uy);
			gb=(((-listy)+y)/itemHeight);
			uy=y;
			this.type=_GBUP;
		}
		else if(type==MS_UP)
		{
			if(this.type==_GBDOWN)
			{
				gb=(((-listy)+y)/itemHeight);
				if(listener!=null)
				listener.onClick(this,gb);
			}
			this.type=_GBUP;
			
		}
	return 0;
	}
	
	
		public static abstract interface OnClickListener
			{
        public abstract void onClick(FileList p1,int type);

			}
	
}
