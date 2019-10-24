package com.xl.game.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.graphics.Matrix;

public class Prect {
	public Bitmap bitmap;
	public Paint paint;
	public int ph;
	public int pw;
	public int px;
	public int py;
	public Rect rect;

	public Prect(Bitmap bitmap)
	{
		
		
	
		this.bitmap = bitmap;
		
	
		this.paint = new Paint();
		paint.setFilterBitmap(true);
		
		this.px = 0;
		this.py = 0;
		this.pw = bitmap.getWidth();
		this.ph = bitmap.getHeight();
		
		rect = new Rect(this.px, this.py, (this.px + this.pw), (this.py + this.ph));
		
	}

	public Prect(Bitmap bitmap, int x, int y, int w, int h)
	{
		
		
		
		this.bitmap = bitmap;
		//this.bitmap.setHasAlpha(false);
		paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setAntiAlias(true);
		
		this.px = x;
		this.py = y;
		this.pw = w;
		this.ph = h;
		
		this.rect = new Rect(x, y, (x + w), (y + h));
		
	}

	public void draw(Canvas canvas, int x, int y) {
	
		
		Rect dealrect = new Rect(x, y, (x + this.pw), (y + this.ph));
		canvas.drawBitmap(this.bitmap, this.rect, dealrect, paint);
	}

	public Paint getPaint() {
		return this.paint;
	}

	public void setPaint(Paint r6_Paint) {
		this.paint = r6_Paint;
	}

	public void setRect(int x, int y, int w, int h) {
		
		
		this.px = x;
		this.py = y;
		this.pw = w;
		this.ph = h;
		this.rect.set(x, y, (x + w), (y + h));
	}
	
	//Bitmap 的旋转与镜像
		Bitmap convert(Bitmap bitmap, int width, int height)
		{int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas canvas = new Canvas(newb);
		Matrix matrix = new Matrix();
		matrix.postScale(1, -1);   //镜像垂直翻转
		matrix.postScale(-1, 1);   //镜像水平翻转
		matrix.postRotate(-90);  //旋转-90度
		Bitmap new2 = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		canvas.drawBitmap(new2, new Rect(0, 0, new2.getWidth(), new2.getHeight()),new Rect(0, 0, w, h), null);
		return newb;
		}
	
}
