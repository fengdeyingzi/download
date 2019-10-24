package com.xl.game.view;
import android.graphics.Rect;
import android.graphics.Canvas;

/*
物体接口，拥有物体基本属性，如移动，绘制，碰撞      

*/


public interface  Body
{
	void logc();
	
	void draw(Canvas canvas);
	
	void setx(int x);
	
	void sety(int y);
	
	//void setxy();
	
	int getx();
	
	int gety();
	
	int getWidth();
	int getHeight();
	
	boolean impact(int x,int y);
	
	
	boolean contains(Body body);
	
}
