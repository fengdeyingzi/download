package com.xl.tool;






import android.support.annotation.NonNull;

import com.xl.game.tool.Log;
import com.xl.game.tool.Urlquest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 文件下载器

 */

public class DownLoad
{
    String TAG ="DownLoad";
    private String url;
    private String path;
    private long fileSize;
    private String FileName;
    private int progress;
    private int contime=10000;
    private int readtime=15000;
    private int state;
    private boolean isRun;
    public static final int
            DOWN_NONE=0, //未下载
            DOWN_START=1, //下载开始
            DOWN_POSITION=2, //下载进度
            DOWN_COMPLETE=3, //下载完成
            DOWN_ERROR = -1; //下载错误
    DownLoadListener listener;
    private static int downLoadFilePosition = 0;
    Call download_call;


    public DownLoad(String url,String path,String filename)
    {
        this.url=url;
        this.path=path;
        this.FileName=filename;
        this.progress = 0;
        this.state = DOWN_NONE;
    }

    public File getFile()
    {
        return new File(path,FileName);
    }

    //设置文件长度
    public void setFileSize(int len)
    {
        this.fileSize = len;
    }

    public void sendMsg(int type,String text)
    {
        System.out.println(text);
        if(listener!=null)
            listener.onMsg(type,text);
        this.state = type;
    }

    //设置进度% 百分比
    public void setProgress(int progress)
    {
        this.progress = progress;
        System.out.println(""+progress+"%");
        if(listener!=null)
            listener.onProgress(progress);
    }

    public void setDownLoadListener(DownLoadListener listener)
    {
        this.listener = listener;
    }

    //开始下载
    public void start()
    {
        isRun=true;
//		downFile(this.url,this.path,this.FileName);
        download(this.url,this.path,  this.FileName);
    }

    //停止下载
    public void stop()
    {
        isRun=false;

        download_call.cancel();
    }

    //获取完成状态
    public int getState()
    {
        return this.state;
    }

    public String FormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param fileName 文件名
     */
    public void download(final String url, final String saveDir, final String fileName) {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        download_call = okHttpClient.newCall(request);
        download_call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
//				listener.onDownloadFailed();
                sendMsg(DOWN_ERROR,"下载失败，连接出错");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = saveDir;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    if(total>0){// 获取内容长度为0
                        fileSize = total;
                        //throw new RuntimeException("无法获知文件大小 ");
                    }
                    File file = new File(savePath, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
//						listener.onDownloading(progress);
                        downLoadFilePosition  = progress;
                        fileSize = total;
                        sendMsg(DOWN_POSITION, "" + FormetFileSize(sum) + "/" + FormetFileSize(fileSize));
                    }
                    fos.flush();
                    fos.close();
                    // 下载完成
//					listener.onDownloadSuccess();
                    sendMsg(DOWN_COMPLETE, "下载完成"+downLoadFilePosition);
                } catch (Exception e) {
//					listener.onDownloadFailed();
                    sendMsg(DOWN_ERROR,"下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

// ————————————————
//	版权声明：本文为CSDN博主「Fantasychong丶」的原创文章，遵循CC 4.0 by-sa版权协议，转载请附上原文出处链接及本声明。
//	原文链接：https://blog.csdn.net/u014078990/article/details/82882433

    //下载文件 参数 url 路径 文件名
    public void downFile(@NonNull String url, String path, String fileName)
    {
        int pos=0; //进度百分比
        URL Url = null ;
        URLConnection conn=null;
        InputStream input=null;
        FileOutputStream out =null;
        int download_type = DOWN_START;
        Log.e(TAG,"开始下载文件，文件名："+fileName);
        if (fileName == null || fileName.length()==0)
            this.FileName = Urlquest.getUrlFileName(url);
        else
            this.FileName = fileName; // 取得文件名，如果输入新文件名，则使用新文件名　

        try
        {
            Url= new URL(url);
        }
        catch (MalformedURLException e)
        {
            sendMsg(DOWN_ERROR,"获取下载地址出错");
            return;
        }
        try
        {
            conn= Url.openConnection();
        }
        catch (IOException e)
        {
            sendMsg(DOWN_ERROR,"打开连接出错");
            return;
        }
        if(!isRun)return;
        conn.setConnectTimeout(contime);
        conn.setReadTimeout(readtime);
        sendMsg(DOWN_START,"连接中...");
        try
        {
            conn.connect();
        }
        catch (IOException e)
        {
            sendMsg(DOWN_ERROR,"连接出错");
            return;
        }
        if(!isRun)return;
        try
        {
            input = conn.getInputStream();
        }
        catch (IOException e)
        {
            sendMsg(DOWN_ERROR,"IO异常");
            return;
        }
        int length = conn.getContentLength();// 根据响应获取文件大小

        if (length > 0)
        { // 获取内容长度为0
            this.fileSize = length;
            //throw new RuntimeException("无法获知文件大小 ");
        }

        if (input == null)
        { // 没有下载流　
            sendMsg(DOWN_ERROR,"无法获取文件");
            return;
            //throw new RuntimeException("无法获取文件");
        }
        File file = new File(path);
        if(!file.isDirectory())
        {
            file.mkdirs();
        }
        try
        {
            out= new FileOutputStream(new File(file, this.FileName));
        }
        catch (FileNotFoundException e)
        {
            sendMsg(DOWN_ERROR,"创建文件失败，请检查内存卡");
            return;
        }
        // 创建写入文件内存流，　　通过此流向目标写文件　
        byte buf[] = new byte[1024 * 10];
        downLoadFilePosition = 0;
        int numread;
        sendMsg(DOWN_START,"开始下载...");
        download_type = DOWN_POSITION;
        if(!isRun)return;
        try
        {
            while ((numread = input.read(buf)) != -1)
            {
                out.write(buf, 0, numread);
                downLoadFilePosition += numread;
                pos = (int)(100*downLoadFilePosition / fileSize);
                if (pos != this.progress)
                    setProgress(pos);
                sendMsg(download_type, "" + FormetFileSize(downLoadFilePosition) + "/" + FormetFileSize(fileSize));
                if(!isRun)
                {
                    try
                    {
                        input.close();
                        out.flush();
                        out.close();
                    }
                    catch(IOException e)
                    {
                        download_type = DOWN_ERROR;
                        sendMsg(DOWN_ERROR,"流关闭错误");
                        return;
                    }
                    finally {
                        download_type=  DOWN_ERROR;
                        sendMsg(DOWN_ERROR,"下载取消");

                    }




                }
            }
        }
        catch (IOException e)
        {
            download_type= DOWN_ERROR;
            sendMsg(DOWN_ERROR,"下载文件出错\n");
        }
        catch(NullPointerException e)
        {
            download_type= DOWN_ERROR;
            sendMsg(DOWN_ERROR,"下载出错\n");
        }
        finally
        {
            try
            {
                input.close();
            }
            catch(IOException e)
            {
                download_type= DOWN_ERROR;
                sendMsg(DOWN_ERROR,"网络流关闭错误");
                return;
            }
            try
            {
                out.flush();
                out.close();
            }
            catch(IOException e)
            {
                download_type = DOWN_ERROR;
                sendMsg(DOWN_ERROR,"文件关闭错误");
                return;
            }
            //关键 判断文件是否下载完成
            if(download_type!=DOWN_ERROR)
                sendMsg(DOWN_COMPLETE, "下载完成"+downLoadFilePosition);
        }
		/*
		catch (Exception ex)
		{
			sendMsg(DOWN_ERROR,"文件流错误");
		}*/
    }
	/*
	 //通过此代码就可以实现将内容保存到SD卡等设备上，当然要使用网络，必须得有网络的访问权限。这个需要自己添加，在这里不再添加。　　上面的代码没有实现进度条功能，如果要实现进度条功能，我现在考虑到的就是使用消息进行发送提示，首先实现一个消息。　
	 private Handler downloadHandler = new Handler()
	 { // 用于接收消息，处理进度条
	 @Override　
	 public void handleMessage(Messagemsg)
	 { // 接收到的消息，并且对接收到的消息进行处理　
	 if (!Thread.currentThread().isInterrupted())
	 {
	 switch (msg.what)
	 {
	 case DOWN_START:
	 pb.setMax(fileSize); //设置开始长度　
	 case DOWN_POSITION:
	 pb.setProgress(downLoadFilePosition); //设置进度
	 break;
	 case DOWN_COMPLETE:
	 Toast.makeText(DownLoadFileTest.this, "下载完成！", 1).show(); // 完成提示　
	 break;
	 case Down_ERROR:
	 String error = msg.getData().getString("下载出错！");　
	 Toast.makeText(DownLoadFileTest.this, error, 1).show();　
	 break;
	 }
	 }
	 super.handleMessage(msg);
	 }
	 };
	 */


    public interface DownLoadListener
    {
        public void onProgress(int progress);
        public void onMsg(int type, String text);

    }

}
