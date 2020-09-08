package com.xl.tool;


//apk下载线程
/*
 //使用内部下载
 public void downloadDialog(final Context context,final String url)
 {
 View dlg_pageapk = ViewTool.getView(context,R.layout.dialog_pageapk);
 dlg_progress = (com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate)dlg_pageapk.findViewById(R.id.dlg_progress);
 dlg_text = (TextView)dlg_pageapk.findViewById(R.id.dlg_text);

 final String filename="none.apk";
 String text_filelen = HttpUtil._get(url,"filelen");
 final int filelen = Str.atoi(text_filelen);
 down = new DownLoadThread(context,url, this);


 down.setDownLoadListener(DownLoadListener.this);
 down.start();
 dialog_down = new AlertDialog.Builder(context)
 .setTitle("正在下载")
 .setView(dlg_pageapk)

 .setNegativeButton("取消", new DialogInterface.OnClickListener()
 {

 @Override
 public void onClick(DialogInterface p1, int p2)
 {
 down.stopx();
 }


 })
 .setCancelable(false)
 .show();
 }

*/
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.xl.game.math.Str;
import com.xl.game.tool.FileUtils;
import com.xl.game.tool.HttpUtil;
import java.io.File;
import com.xl.game.tool.Log;

public class DownLoadThread extends Thread implements DownLoad.DownLoadListener
{
    String TAG ="DownLoadThread";
    @Override
    public void onProgress(int progress)
    {
        Message m = new Message();
        m.what = 1;

        m.arg1 = progress;
        handler .sendMessage(m);
    }

    @Override
    public void onMsg(int type, String text)
    {
        Message m = new Message();
        m.what = 2;
        m.arg1 = type;
        m.obj = text;
        handler.sendMessage(m);
    }

    String url;
    String filename;
    int filelen = 0;
    DownLoad down;
    DownLoad.DownLoadListener listenerSur;
    Handler handler;
    public class ThreadHandler extends Handler
    {
        //public DownLoad.DownLoadListener listener;


        public ThreadHandler(DownLoad.DownLoadListener listener)
        {
            listenerSur = listener;
        }

        public void handleMessage(Message msg)
        {
            if(msg.what==1)
            {
                if(listenerSur!=null)
                    listenerSur.onProgress(msg.arg1);
                if(msg.obj!=null)
                {

                }
            }
            else if(msg.what==2)
            {
                if(listenerSur != null)
                {
                    listenerSur.onMsg(msg.arg1,(String)msg.obj);
                }
            }
        }
    }


    public DownLoadThread( String url, String path,String name,DownLoad.DownLoadListener listener)
    {
        super();
        this.url =url;
        this.listenerSur = listener;
        this.filename=name;
        Log.e(TAG,"创建下载，文件名"+this.filename);
        String text_filelen = HttpUtil._get(url,"filelen");
        handler = new ThreadHandler(listener);
        final int filelen = Str.atoi(text_filelen);
        down = new DownLoad(url, path, filename);

        if(filelen>0)
            down.setFileSize(filelen);
        down.setDownLoadListener(this);

    }

    //设置文件名
    public void setFileName(String filename)
    {
        this.filename = filename;
    }

    //获取下载的文件完整路径
    public String getPath()
    {
        return down.getFile().getPath();
    }

    public void run()
    {
        down.start();
    }

    public void stopx()
    {
        down.stop();
    }

    public void setDownLoadListener(DownLoad.DownLoadListener listener)
    {
        this.listenerSur = listener;
    }
}
