package com.example.zane.easyimageprovider.download.execute;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.zane.easyimageprovider.download.request.BitmapRequest;
import com.example.zane.easyimageprovider.utils.BitmapDecode;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Created by Zane on 16/9/24.
 * Email: zanebot96@gmail.com
 */

public class BitmapCallback implements Callable<Bitmap>, ThreadPoolQueuePolicy{

    private BitmapRequest request;

    public BitmapCallback(BitmapRequest request){
        this.request  = request;
    }

    @Override
    public Bitmap call() throws Exception {
        final URL imgURL;
        Bitmap bitmap = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try{
            imgURL = new URL(request.uri);
            Log.i("BitmapCallback", imgURL + " url");
            conn = (HttpURLConnection) imgURL.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            //conn.setRequestMethod("GET");
            is = conn.getInputStream();
            Log.i("BitmapCallback", conn.getInputStream() + " is");
            //高效加载
            //bitmap = BitmapDecode.decodeRequestBitmap(is, request.getImageViewWidth(), request.getImageViewHeight());
            bitmap = BitmapFactory.decodeStream(is);
            Log.i("BitmapCallback", bitmap + " bitmap");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                is.close();
            }
            if (conn != null){
                conn.disconnect();
            }
        }
        return bitmap;
    }

    @Override
    public int getPolicy() {
        return request.ID;
    }
}