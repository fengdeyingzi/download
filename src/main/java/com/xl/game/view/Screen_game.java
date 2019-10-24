package com.xl.game.view;
import android.graphics.*;

public class Screen_game extends Screen
{

	@Override
	public int load()
	{
		// TODO: Implement this method
		return 0;
	}


		


		@Override
		public void run()
			{
				// TODO: Implement this method
			}

		@Override
		public int init()
			{
				// TODO: Implement this method
				return 0;
			}

		@Override
	public	int pause()
			{
				// TODO: Implement this method
				return 0;
			}

		@Override
		public int resume()
			{
				// TODO: Implement this method
				return 0;
			}

		@Override
		public int exit()
			{
				// TODO: Implement this method
				return 0;
			}


		int game_ftp=30;//帧数
		static int game_times;//时间
		int ref_time; 
	
		Bitmap tu;
		Prect tu1[];//主角切片
		Bitmap zha;
		Prect baozha;
		p pman;//主角
		p pbaozha;
		boolean Loop=true;//线程循环判断

		int state_man;//主角状态
		public static final int
		LIFE=0,
		OVER=1;
		int state_key;//按键状态
		int cup;//生命

		int i;

		int game_time;//定时器

		public static final int
		UP=0,
		DOWN=1;



		final int INIT=0;

		class Stome
			{
				int x,y;
				int size;
				Stome()
					{
						x = y = 0;
						size = 0;
					}
			}//石头

		Stome stome []=new Stome[5];
		
		Screen_game()
		{
			
			
		}

		private void cls()
			{
				realCanvas.drawRGB (245, 245, 245);
			}
		
		public void draw()
			{
				cls();
				int i,j;

				//加载石头
				for (i = 0;i < 5;i++)
					{
						for (j = 0;j < stome[i].size;j++)
							{
								realCanvas.drawRect (stome[i].x, stome[i].y + j * 40,stome[i].x+ 40,stome[i].y+ 40, paint);
								//drect(stome[i].x, stome[i].y+j*20, 20, 20, 68, 168 ,230);
							}
					}


				pman.draw (realCanvas);
				if(state_man==OVER)
					{
						pbaozha.draw(realCanvas);
						//text_draw("游戏结束",SCRW/2,0,SCRH,TEXT_CENTER);
					}
			}

		
			
			
			
		public int event(int type, int p1, int p2)
			{
				int px,py;
				//绝对坐标转相对坐标
				px= getx(p1); //(p1-x)*bmpw/w;
				py= gety(p2);    //(p2-y)*bmph/h;
				
				
				
				
				return 0;
			}

}
