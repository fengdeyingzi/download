package com.xl.game.view;

public interface  opgame
{
	public int init();
	public int event(int type,int p1,int p2);
	int winEvent(Screen screen,int type);
	public int pause();
	public int resume();
	public int exitApp();
	
	
}
