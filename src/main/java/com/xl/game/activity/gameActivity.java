package com.xl.game.activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.util.*;

import java.io.*;
import android.graphics.drawable.*;
import android.net.*;
import android.database.*;
import android.content.pm.*;
//	import org.apache.http.util.*;
import com.xl.game.view.opgame;
//import com.xl.game.tool.Logcat;
import com.xl.game.view.gameView;



/*
xl game 包

v2.0


*/




public class gameActivity extends Activity
{
private	opgame game;

	private static final int IMAGE_SELECT = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		Log.e("gameActivity","onCreate");
        super.onCreate(savedInstanceState);
		//设置竖屏模式		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		
		//去除标题栏
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		getWindow().setFlags(1024, 1024);

		
// 获取屏幕宽高
			Display display = getWindowManager().getDefaultDisplay();
		//	System.out.println(".......");
		
		;
		;
		gameView.Screen_W =display.getWidth();
		gameView.Screen_H =display.getHeight();
		
			
			//setContentView(R.layout.main);
			//sketchpadview=(initView)findViewById(R.id.sketchpad_main);
			//System.out.println("activity....");
		//mpc=new initView(this);
			
			//sketchpadview.setBackgroundColor(0xff5d5d5d);
			
			//sketchpadview.setOnClickListener(this);
	
			
			System.out.println("activity初始化");
      // setContentView(game);
			 //mpc.setBackgroundColor(Color.argb(255,50,50,50));
			 
			 //BitmapDrawable scrbufdr=new BitmapDrawable(scrbuf_bmp);
			 //sketchpadview.setBackgroundDrawable(scrbufdr);

			//Rect frame = new Rect();
			//getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		  //statusBarHeight = frame.top;

			//contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//statusBarHeight是上面所求的状态栏的高度
			//titleBarHeight = contentTop - statusBarHeight;
			
			 
			 
			 
	}

	//设置game
	public void setGame(opgame gameview)
	{
		this.game=gameview;
	}
	
protected	void onStart()
	{
		Log.e("geActivity","onStart");
		super.onStart();
	}

@Override
protected void onRestart()
	{
		Log.e("gameActivity","onRestart");
		// TODO: Implement this method
		super.onRestart();
	}

	
	
	/*
@Override
public View onCreateView(View parent,String name,Context context,AttributeSet attrs)
	{
//		Logcat.e("onCreateView");
		// TODO: Implement this method
		return super.onCreateView(parent, name, context, attrs);
}

*/

	
@Override
protected void onPause()
	{
		Log.e("gameActivity","onPause");
		// TODO: Implement this method
		//game.pause();
		super.onPause();
	}

@Override
protected void onResume()
	{
		Log.e("gameActivity","onResume");
		// TODO: Implement this method
		//game.resume();
		super.onResume();
	}

@Override
protected void onDestroy()
	{
		// TODO: Implement this method
		if(game!=null)
		game.exitApp();
		super.onDestroy();
	}
		
		
		
		

	public void onClick(View v)
	{
		}
	
	/*	
	
	public boolean onTouchEvent(MotionEvent event)
	{
		
		//mpc.EVENT_A=true;
		return true;
	}
*/
	public boolean onKeyDown(int keycode, KeyEvent event)
	{
		
		
		game.event(gameView.KY_DOWN, keycode,0);
		 //return true;
		return super.onKeyDown(keycode,event);
	}


	public boolean onKeyUp(int keycode, KeyEvent event)
	{
		
		
		game.event(gameView.KY_UP, keycode,0);
    //return true;
		return super.onKeyUp(keycode,event);
	}
	
	
	
	/*
	//接收参数传递
	protected void onActivityResult( int requestCode, int resultCode, Intent data)
	 {
		 if (resultCode == Activity.RESULT_OK)
			 {
			 Uri uri = data.getData();
			 
			 Cursor cursor = this .getContentResolver().query(uri, null , null , null , null );
			 cursor.moveToFirst();
			 
			 for ( int i = 0 ; i < cursor.getColumnCount(); i++)
				 { // 取得图片uri的列名和此列的详细信息
				 System.out.println(i + "-" + cursor.getColumnName(i) + "-" + cursor.getString(i));
				 }
				 
				System.out.println("路径："+uri.getEncodedPath());
				 System.out.println("路径2："+data.getAction());
				 
				//sketchpadview.img_name=uri.getPath();
				
			 }
		 }
	
	*/
	public  void open_img()
	{
		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, IMAGE_SELECT);
		
		
	}
	
	
	
}
	
	

