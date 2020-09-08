package com.xl.tool;

/**
 * suthor:风的影子
 * <p>
 * date:#{DATE}#{TIME}
 * desc:
 * version:1.0
 **/

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.TextView;
import android.widget.Toast;



import com.xl.game.math.Str;
import com.xl.game.tool.AppTool;
import com.xl.game.tool.FileUtils;
import com.xl.game.tool.HttpUtil;
import com.xl.game.tool.ViewTool;

import java.io.File;





public class DownLoadListener implements DownloadListener,DownLoad.DownLoadListener
{

    @Override
    public void onProgress(int progress)
    {
        dlg_progress.setProgress(progress);
    }

    @Override
    public void onMsg(int type, String text)
    {
        //下载完成 安装
        if(type == DownLoad.DOWN_COMPLETE)
        {
            dlg_text.setText(text);
            if(down!=null)
                AppTool.packageApp(context,down.getPath());
            if(dialog_down!=null)
            {
                if(dialog_down.isShowing())
                    dialog_down.dismiss();
            }
        }
        else if(type == DownLoad.DOWN_ERROR)
        {
            dlg_text.setText(text);
        }
        else if(type == DownLoad.DOWN_POSITION)
        {
            dlg_text.setText(text);

        }
        else if(type == DownLoad.DOWN_START)
        {
            dlg_text.setText(text);
        }
    }

    private Activity context;
    private TextView dlg_text;
    com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate dlg_progress;
    DownLoadThread down;
    Dialog dialog_down;

    public DownLoadListener(Activity context)
    {
        this.context = context;
    }
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,long contentLength)
    {
        String checkUrl = url;
        //去除get参数
        if(checkUrl.indexOf('?')>=0)
        {
            checkUrl = checkUrl.substring(0,checkUrl.indexOf('?'));
        }
		/*
		 Log.e("tag", "url="+url);
		 Log.e("tag", "userAgent="+userAgent);
		 Log.e("tag", "contentDisposition="+contentDisposition);
		 Log.e("tag", "mimetype="+mimetype);
		 Log.e("tag", "contentLength="+contentLength);
		 */

        if (mimetype.equals("application/vnd.android.package-archive") || checkUrl.endsWith(".apk"))
        {
            String text_filelen = HttpUtil._get(url,"filelen");
            String text_downloadmode = HttpUtil._get(url,"downloadmode");
            //如果携带有指定参数，就调用内部下载
            if(text_downloadmode!=null && text_filelen!=null)
            {
                downloadDialog(context,url);
            }
            //否则调用浏览器下载
            else
            {
                downloadBrowser(url);
            }
            if (contentLength > 0)
            {
                //UpdateManagerListener.startDownloadTask((Activity)context, url);
                //Toast.makeText(context, "该下载可能不会有进度提示", 1).show();
            }
        }
        else
        {
            downloadBrowser(url);
        }
    }



    //调用浏览器下载
    public void downloadBrowser(String url)
    {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try
        {
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "未找到浏览器程序", Toast.LENGTH_SHORT).show();
        }
    }

    //使用内部下载 下载路径最好是：getApplicationContext().getExternalCacheDir()
    public void downloadDialog(final Activity context, final String url)
    {
        View dlg_pageapk = ViewTool.getView(context, R.layout.dialog_pageapk);
        dlg_progress = (com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate)dlg_pageapk.findViewById(R.id.dlg_progress);
        dlg_text = (TextView)dlg_pageapk.findViewById(R.id.dlg_text);

        String filename=HttpUtil._get(url,"name");
        String text_filelen = HttpUtil._get(url,"filelen");
        final int filelen = Str.atoi(text_filelen);
        if(filename==null){
            filename = "app.apk";
        }
//        context.getApplicationContext().getExternalCacheDir().getAbsolutePath()
        down = new DownLoadThread(url,context.getApplicationContext().getExternalCacheDir().getAbsolutePath(),filename,this);

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

    //使用内部下载
    public void downloadDialog(final Context context,final String url,String filename)
    {
        View dlg_pageapk = ViewTool.getView(context, R.layout.dialog_pageapk);
        dlg_progress = (com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate)dlg_pageapk.findViewById(R.id.dlg_progress);
        dlg_text = (TextView)dlg_pageapk.findViewById(R.id.dlg_text);

//		String filename=HttpUtil._get(url,"name");
        String text_filelen = HttpUtil._get(url,"filelen");
        final int filelen = Str.atoi(text_filelen);
//		if(filename==null){
//			filename = "app.apk";
//		}
        File file = new File(filename);
        down = new DownLoadThread(url,file.getPath(),file.getName(),this);

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


    //判断是否可以下载
    public static boolean isDownLoad(){
        return true;
    }



}


