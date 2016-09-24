package com.example.zane.easyimageprovider.download.request;

import android.util.Log;
import android.widget.ImageView;

import com.example.zane.easyimageprovider.builder.EasyImageLoadRecord;
import com.example.zane.easyimageprovider.download.EasyImageLoadConfiguration;
import com.example.zane.easyimageprovider.download.policy.ImageLoadPolicy;
import com.example.zane.easyimageprovider.utils.ImageViewHelper;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by Zane on 16/9/24.
 * Email: zanebot96@gmail.com
 */

//封装的请求类型Request ,存入分发队列
public class BitmapRequest implements Comparable<BitmapRequest>{

    private EasyImageLoadRecord r;
    private Reference<ImageView> imageViewReference;

    public String uri;
    //https, http, uri
    public String uriHead;
    public ImageLoadPolicy policy;
    //请求ID = thread ID
    public int ID;

    public BitmapRequest(EasyImageLoadRecord r){
        this.r = r;
        init();
    }

    private void init(){
        policy = EasyImageLoadConfiguration.getInstance().getLoadPolicy();
        uri = r.uri;
        uriHead = parseSchema(uri);

        //防止list加载乱序
        // TODO: 16/9/24 防止乱序测试
        imageViewReference = new WeakReference<ImageView>(r.imageView);
        r.imageView.setTag(uri);
    }

    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            return uri.split("://")[0];
        } else {
            throw new IllegalArgumentException("uri should be right format with ://!");
        }
    }

    @Override
    public int compareTo(BitmapRequest another) {
        return policy.compare(this, another);
    }

    public ImageView getImageView() {
        return imageViewReference.get();
    }

    public int getImageViewWidth() {
        return ImageViewHelper.getImageViewWidth(imageViewReference.get());
    }

    public int getImageViewHeight() {
        return ImageViewHelper.getImageViewHeight(imageViewReference.get());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        result = prime * result + ((imageViewReference == null) ? 0 : imageViewReference.get().hashCode());
        result = prime * result + ID;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BitmapRequest other = (BitmapRequest) obj;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        if (imageViewReference == null) {
            if (other.imageViewReference != null)
                return false;
        } else if (!imageViewReference.get().equals(other.imageViewReference.get()))
            return false;
        if (ID != other.ID)
            return false;
        return true;
    }
}
