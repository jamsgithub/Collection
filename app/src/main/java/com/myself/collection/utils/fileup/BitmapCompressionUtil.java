package com.myself.collection.utils.fileup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Jams
 * @date 2019/4/1 17:01
 * @des todo
 * @
 * @upAuthor
 * @upDate 2019/4/1 17:01
 * @upDes todo
 */
public class BitmapCompressionUtil {
    private static volatile BitmapCompressionUtil instance;
    private BitmapCompressionUtil(){}

    public static BitmapCompressionUtil getInstance(){
        if (instance == null){
            synchronized (BitmapCompressionUtil.class){
                if (instance == null){
                    instance = new BitmapCompressionUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 质量压缩
     * @param image
     * @return
     */
    public Bitmap compressionImg(Bitmap image){
        int options = 90;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while ( baos.toByteArray().length / 1024>100) {
            baos.reset();//重置baos即清空baos
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少10
            options -= 10;
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    public int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();
    }


}
