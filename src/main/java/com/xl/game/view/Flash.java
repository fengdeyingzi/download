package com.xl.game.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import com.xl.game.tool.XL;

public class Flash implements Cloneable
	{
		static final int _ADDPICPRECT = 6;
		static final int _SETBITMAP = 0;
		static final int _SETDRAWTYPE = 7;
		static final int _SETPICPRECTSIZE = 5;
		static final int _SETPICSIZE = 2;
		static final int _SETPRECT = 4;
		static final int _SETPRECTSIZE = 1;
		static final int _SETWH = 3;
		Bitmap bitmap;
		Bitmap bitmap_draw;
		Canvas canvas_bitmap;
		int drawtype;
		public int ftps;
		public int h;
		int n;
		private Paint paint;
		Prect[] pbmp;
		Picturex[] picture;
		int prectsize;
		public int w;

		class Picturex 
			{
				int loadmath;
				int math;
				int[] pbmpID;

				int[] x;
				int[] y;

				public Picturex()
					{

					}


				/*
				 public Picturex(Flash flash)
				 {
				 this.this$0=flash;
				 }
				 */


			}

		public Flash()
			{
				this.paint=new Paint();
				this.n=0;
				this.drawtype=1;
			}

		public Flash(int ftps)
			{
				this.paint=new Paint();
			}
			
		public int readFlash(Context context,String name)
			{
				String str=
				XL.getTextFromAssets(context,name);
				char text[] = str.toCharArray();
				char setbitmap[] = {'s','e','t','B','i','t','m','a','p'};
				char setprectsize[] = {'s','e','t','P','r','e','c','t','S','i','z','e'};
				char setpicsize[] = {'s','e','t','P','i','c','S','i','z','e'};
				char setwh[] = {'s','e','t','W','H'};
				char setprect[] = {'s','e','t','P','r','e','c','t'};
				char setpicprectsize[] = {'s','e','t','P','i','c','P','r','e','c','t','S','i','z','e'};
				char addpicprect[] = {'a','d','d','P','i','c','P','r','e','c','t'};


				int lines;//行数
				int i;
				int ptr;
				int endptr;
				int type;//函数类型
				int per[] = new int[20]; //参数列表
				char bitmapname [] = new char[100];//文本参数
				//获取文本的行数
				lines=0;
				for(i=0;i<text.length;i++)
					{
						if(text[i]=='\n')
							lines++;
					}
				//循环
				ptr=0;
				for(i=0;i<lines;i++)
					{
						//获取一行的内容

						endptr=str.indexOf('\n',ptr);

						//跳转到第一个字母
						ptr=toFirst(text,ptr);
						//解析函数
						if(settexttwo(text,ptr,setbitmap))
							{
								type=_SETBITMAP;

							}
						else if(settexttwo(text,ptr,setprectsize))
							{
								type=_SETPRECTSIZE;
							}
						else if(settexttwo(text,ptr,setpicsize))
							{
								type=_SETPICSIZE;
							}
						else if(settexttwo(text,ptr,setwh))
							{
								type=_SETWH;
							}
						else if(settexttwo(text,ptr,setprect))
							{
								type=_SETPRECT;
							}
						else if(settexttwo(text,ptr,setpicprectsize))
							{
								type=_SETPICPRECTSIZE;
							}
						else if(settexttwo(text,ptr,addpicprect))
							{
								type=_ADDPICPRECT;
							}
						else 
							type=-1;
						//跳转到参数
						ptr=toPerameter(text,ptr);

						
						if(type==_SETBITMAP)
							{
								bitmapname = new char[endptr-ptr];
								for(int j=0;j<endptr-ptr;j++)
									{
										bitmapname[j]=text[ptr+j];
									}
							}
						else
						{
							//生成参数列表
							atoiEx(text,ptr,per);
							
						}
						//执行函数
						switch(type)
							{
								case _SETBITMAP:
									Bitmap bitmap=getImageFromAssetsFile(context,new String(bitmapname));
                 setBitmap(bitmap);
								 Log.e("XLLOG","信息：setbitmap");
									break;
								case _SETPRECTSIZE:
									setPrectSize(per[0]);
									Log.e("XLLOG","信息：setprectsize");
									break;
								case	_SETPICSIZE:
									setPicSize(per[0]);
									Log.e("XLLOG","信息：setpicsize");
									break;
								case	_SETWH:
									Log.e("XLLOG","信息：setwh"+per[0]+" "+per[1]);
									setWH(per[0],per[1]);
									
									
									break;
								case	_SETPRECT:
									setPrect(per[0],per[1],per[2],per[3],per[4]);
									Log.e("XLLOG","信息：setprect");
									break;
								case	_SETPICPRECTSIZE:
									setPicPrectSize(per[0],per[1]);
									Log.e("XLLOG","信息：setpicpretsize");
									break;
								case	_ADDPICPRECT:
									addPicPrect(per[0],per[1],per[2],per[3]);
									Log.e("XLLOG","信息：addpicprect");
									break;
							}


						ptr=endptr+1;

					}


				return 0;
			}
			

		

		int atoi(char[] text,int ptr)
			{
				int r0_int = 0;
				int r2_int = (-1);
				int r1_int = 0;
				while(ptr<text.length)
					{
						switch(text[ptr])
							{
								case '0': {
											r1_int=(r1_int*10)+0;
											if(r0_int!=r2_int)
												{
													ptr+=1;
												}
											else
												{
													return r1_int;
												}
										}
								case '1': {
											r1_int=(r1_int*10)+1;
											if(r0_int!=r2_int)
												{
													return r1_int;
												}
											else
												{
													ptr+=1;
												}
											break;
										}
								case '2': {
											r1_int=(r1_int*10)+2;
											if(r0_int!=r2_int)
												{
													ptr+=1;
												}
											else
												{
													return r1_int;
												}
										}
								case '3': {
											r1_int=(r1_int*10)+3;
											if(r0_int!=r2_int)
												{
													return r1_int;
												}
											else
												{
													ptr+=1;
												}
											break;
										}
								case '4': {
											r1_int=(r1_int*10)+4;
											if(r0_int!=r2_int)
												{
													ptr+=1;
												}
											else
												{
													return r1_int;
												}
										}
								case '5': {
											r1_int=(r1_int*10)+5;
											if(r0_int!=r2_int)
												{
													return r1_int;
												}
											else
												{
													ptr+=1;
												}
											break;
										}
								case '6': {
											r1_int=(r1_int*10)+6;
											if(r0_int!=r2_int)
												{
													ptr+=1;
												}
											else
												{
													return r1_int;
												}
										}
								case '7': {
											r1_int=(r1_int*10)+7;
											if(r0_int!=r2_int)
												{
													return r1_int;
												}
											else
												{
													ptr+=1;
												}
											break;
										}
								case '8': {
											r1_int=(r1_int*10)+8;
											if(r0_int!=r2_int)
												{
													ptr+=1;
												}
											else
												{
													return r1_int;
												}
										}
								case '9': {
											r1_int=(r1_int*10)+9;
											if(r0_int!=r2_int)
												{
													return r1_int;
												}
											else
												{
													ptr+=1;
												}
											break;
										}
							}
						r0_int=r2_int;
						if(r0_int!=r2_int)
							{
								ptr+=1;
							}
						else
							{
								return r1_int;
							}
					}
				return r1_int;
			}

		void atoiEx(char[] text,int ptr,int[] per)
			{
				int aa = 0;
				int i =0;
				int math = 0;
				for(;ptr<text.length;ptr++)
					{
						switch(text[ptr])
							{
								case '\n':
									per[i]=math;
									math=0;
									i++;
										aa=-1;
										
									break;
								case '0': 

									math=(math*10)+0;
									
									break;
								case '1': 
											math=(math*10)+1;
											
											break;
										
								case '2': 
									math=(math*10)+2;
									
									break;

								case '3': 
											math=(math*10)+3;
											
											break;
										
								case '4': 
											math=(math*10)+4;
											
										break;
								case '5': 
											math=(math*10)+5;
											
											break;
										
								case '6': 
											math=(math*10)+6;
											
										break;
								case '7': 
											math=(math*10)+7;
											
											break;
										
								case '8': 
											math=(math*10)+8;
											
										break;
								case '9': 
											math=(math*10)+9;
											
											break;
								case ',':
									
									per[i]=math;
									math=0;
									i++;
									
									break;
								
								default:
								   per[i]=math;
									 i++;
									 math=0;
							    
							}
           if(aa==-1)
					 {
						 break;
					 }
           
					}
			}

		public Object clone()
			{
				try
					{
						return ((Flash) (super.clone()));
					}
				catch(CloneNotSupportedException r0_CloneNotSupportedException)
					{
						r0_CloneNotSupportedException.printStackTrace();
						return null;
					}
			}

		public void create(Bitmap bitmap)
			{
				this.bitmap=bitmap;
				this.setPicSize(1);
				this.setPrectSize(1);
				this.setPrect(0, 0, 0, bitmap.getWidth(), bitmap.getHeight());
				int[] r2_int_A = new int[1];
				r2_int_A[0]=0;
				this.n=0;
				this.setPic(0, r2_int_A, 1, new int[1], new int[1]);
				this.setDrawType(1);
				this.w=bitmap.getWidth();
				this.h=bitmap.getHeight();
			}

		public void draw(Canvas canvas,int x,int y)
			{
				this.drawPic(canvas, this.n, x, y);
				if(this.drawtype==1)
					{
						if(this.n<(this.ftps-1))
							{
								this.n=(this.n+1);
								return;
							}
						else
							{
								this.n=0;
								return;
							}
					}
				else if(this.n<(this.ftps-1))
					{
						this.n=(this.n+1);
						return;
					}
				else
					{
						return;
					}
			}

		public void drawPic(Canvas canvas,int n,int x,int y)
			{
				int i = 0;
				while(i<this.picture[n].math)
					{
						this.pbmp[this.picture[n].pbmpID[i]].draw(canvas, (this.picture[n].x[i]+x), (this.picture[n].y[i]+y));
						i+=1;
					}
			}

		public int getFtps()
			{
				return this.ftps;
			}

		public int getHeight()
			{
				return this.h;
			}

		// 
		Bitmap getImageFromAssetsFile(Context context,String text)
			{
				Bitmap bitmap;

				AssetManager r0_AssetManager = context.getResources().getAssets();
				try
					{
						InputStream Inputstream = r0_AssetManager.open(text);
						bitmap=BitmapFactory.decodeStream(Inputstream);
						Inputstream.close();
						return bitmap;
					}
				catch(IOException r0_IOException)
					{
						bitmap=null;
					}
				return bitmap;

			}

		public int getLoad()
			{
				return this.n;
			}

		public int getWidth()
			{
				return this.w;
			}

			
			
		 
			
		
		public int readFlashx(Context context,String filename)
			{
				String text = XL.getTextFromAssets(context, filename);
				char[] textc = text.toCharArray();
				char[] setbitmap = new char[] { 's', 'e', 't', 'B', 'i', 't', 'm', 'a', 'p' };
				char[] setprectsize = new char[] { 's', 'e', 't', 'P', 'r', 'e', 'c', 't', 'S', 'i', 'z', 'e' };
				char[] setpicsize = new char[] { 's', 'e', 't', 'P', 'i', 'c', 'S', 'i', 'z', 'e' };
				char[] setwh = new char[] { 's', 'e', 't', 'W', 'H' };
				char[] setprect = new char[] { 's', 'e', 't', 'P', 'r', 'e', 'c', 't' };
				char[] setpicprectsize = new char[] { 's', 'e', 't', 'P', 'i', 'c', 'P', 'r', 'e', 'c', 't', 'S', 'i', 'z', 'e' };
				char[] addpicprect = new char[] { 'a', 'd', 'd', 'P', 'i', 'c', 'P', 'r', 'e', 'c', 't' };
				char[] setdrawtype = new char[] { 's', 'e', 't', 'D', 'r', 'a', 'w', 'T', 'y', 'p', 'e' };
				int[] per = new int[20];
				char[] r9_char_A = new char[2];
				int type = 0;
				int r8_int = 0;
				while(type<textc.length)
					{
						int r3_int;
						if(textc[type]=='\n')
							{
								r3_int=r8_int+1;
							}
						else
							{
								r3_int=r8_int;
							}
						type+=1;
						r8_int=r3_int;
					}
				type=0;
				int r10_int = 0;
				char[] r3_char_A = r9_char_A;
				while(r10_int<r8_int)
					{
						int r22_int = text.indexOf(10, type);
						int ptr = this.toFirst(textc, type);
						Log.e("XLLOG", new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("\u4fe1\u606f\uff1a\u83b7\u53d6\u4e00\u884c\u5185\u5bb9").append(r10_int).toString()).append(" ").toString()).append(textc[ptr]).toString());
						if(this.settexttwo(textc, ptr, setbitmap))
							{
								type=0;
								Log.e("XLLOG", "\u4fe1\u606f\uff1asetBimap");
							}
						else if(this.settexttwo(textc, ptr, setprectsize))
							{
								type=1;
								Log.e("XLLOG", "\u4fe1\u606f\uff1asetPrectsize");
							}
						else if(this.settexttwo(textc, ptr, setpicsize))
							{
								type=2;
							}
						else if(this.settexttwo(textc, ptr, setwh))
							{
								type=3;
							}
						else if(this.settexttwo(textc, ptr, setprect))
							{
								type=4;
							}
						else if(this.settexttwo(textc, ptr, setpicprectsize))
							{
								type=5;
							}
						else if(this.settexttwo(textc, ptr, addpicprect))
							{
								type=6;
							}
						else if(this.settexttwo(textc, ptr, setdrawtype))
							{
								type=7;
							}
						else
							{
								type=(-1);
							}
						int r5_int = this.toPerameter(textc, ptr);
						this.atoiEx(textc, r5_int, per);
						if(type==0)
							{
								r3_char_A=new char[(r22_int-r5_int)];
								ptr=0;
								while(ptr<(r22_int-r5_int))
									{
										r3_char_A[ptr]=textc[(r5_int+ptr)];
										ptr+=1;
									}
								r9_char_A=r3_char_A;
							}
						else
							{
								r9_char_A=r3_char_A;
							}
						switch(type)
							{
								case 0: {
											this.setBitmap(this.getImageFromAssetsFile(context, new String(r9_char_A)));
											Log.e("XLLOG", new StringBuffer().append("setbitmap").append(new String(r9_char_A)).toString());
											break;
										}
								case 1: {
											this.setPrectSize(per[0]);
											Log.e("XLLOG", new StringBuffer().append("\u4fe1\u606f\uff1asetprectsize").append(per[0]).toString());
											break;
										}
								case 2: {
											this.setPicSize(per[0]);
											Log.e("XLLOG", new StringBuffer().append("\u4fe1\u606f\uff1asetpicsize").append(per[0]).toString());
											break;
										}
								case 3: {
											this.setWH(per[0], per[1]);
											Log.e("XLLOG", "setWH");
											break;
										}
								case 4: {
											this.setPrect(per[0], per[1], per[2], per[3], per[4]);
											break;
										}
								case 5: {
											this.setPicPrectSize(per[0], per[1]);
											break;
										}
								case 6: {
											this.addPicPrect(per[0], per[1], per[2], per[3]);
											break;
										}
								case 7: {
											this.setDrawType(per[0]);
											break;
										}
							}
						type=r22_int+1;
						r10_int=(r10_int+1);
						r3_char_A=r9_char_A;
					}
				return 0;
			}

		public void setBitmap(Bitmap bitmap)
			{
				this.bitmap=bitmap;
			}

		void setDrawType(int type)
			{
				this.drawtype=type;
			}

		public void setFtps(int ftps)
			{
				this.ftps=ftps;
				if(this.picture!=null)
					{
						this.picture=null;
					}
				this.picture=new Picturex[ftps];
				int i = 0;
				while(i<ftps)
					{
						this.picture[i]=new Picturex();
						this.picture[i].loadmath=0;
						i+=1;
					}
			}

		public void setPic(int r2_int,int[] r3_int_A,int r4_int,int[] r5_int_A,int[] r6_int_A)
			{
				this.picture[r2_int].pbmpID=r3_int_A;
				this.picture[r2_int].math=r4_int;
				this.picture[r2_int].x=r5_int_A;
				this.picture[r2_int].y=r6_int_A;
			}

		public void setPicPrectSize(int picid,int prectsize)
			{
				this.picture[picid].pbmpID=new int[prectsize];
				this.picture[picid].x=new int[prectsize];
				this.picture[picid].y=new int[prectsize];
			}

		public void setPicRect(int picid,int prectid,int prect,int x,int y)
			{
				this.picture[picid].pbmpID[prectid]=prect;
				this.picture[picid].x[prectid]=x;
				this.picture[picid].y[prectid]=y;
			}

		public void setPicSize(int r5_int)
			{
				this.picture=new Picturex[r5_int];
				this.ftps=r5_int;
				int r0_int = 0;
				while(r0_int<r5_int)
					{
						this.picture[r0_int]=new Picturex();
						this.picture[r0_int].loadmath=0;
						r0_int+=1;
					}
			}

		public void setPrect(int id,int x,int y,int w,int h)
			{
				if(this.pbmp==null)
					{
						Log.e("XLLOG", "pbmp为空");
					}
				this.pbmp[id]=new Prect(this.bitmap, x, y, w, h);
			}

		public void setPrectSize(int r2_int)
			{
				this.pbmp=new Prect[r2_int];
				this.prectsize=r2_int;
			}
			
		public void addPicPrect(int picid,int prectid,int x,int y)
			{
				this.picture[picid].pbmpID[this.picture[picid].loadmath]=prectid;
				//	Picturex this.picture = this.picture[picid];
				this.picture[picid].math++;
				this.picture[picid].x[this.picture[picid].loadmath]=x;
				this.picture[picid].y[this.picture[picid].loadmath]=y;
				//this.picture=this.picture[picid];
				this.picture[picid].loadmath++;
			}
			

		public void setWH(int w,int h)
			{
				this.w=w;
				this.h=h;
			//	this.bitmap_draw=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			}

		boolean settexttwo(char[] text,int ptr,char[] text2)
			{
				int r0_int = 0;
				while(r0_int<text2.length)
					{
						if(text[(ptr+r0_int)]==text2[r0_int])
							{
								r0_int+=1;
							}
						else
							{
								return false;
							}
					}
				return true;
			}

		public void start()
			{
				this.n=0;
			}

		int toFirst(char[] text,int ptr)
			{
				
				while(ptr<text.length)
					{
						if(text[ptr]>='A')
							{
								if(text[ptr]>'\u0085')
									{
									}
								else
									{
										return ptr;
									}
							}
						if(text[ptr]!='\n')
							{
								ptr+=1;
							}
						else
							{
								return ptr;
							}
					}
				return ptr;
			}

		int toPerameter(char[] text,int ptr)
			{
				
				while(ptr<text.length)
					{
						if(text[ptr]==' ')
							{
								return (ptr+1);
							}
						else
							{
								ptr++;
							}
					}
				return ptr;
			}
	}
