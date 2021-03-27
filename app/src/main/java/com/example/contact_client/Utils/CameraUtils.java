package com.example.contact_client.Utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/*
*  相机相册工具类
* */
public class CameraUtils {
    /*
    *  相机Intent
    * */
    public static Intent getTakePhotoIntent(Context context, File outputImagePath) {
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                // 从文件中创建uri
                Uri uri = Uri.fromFile(outputImagePath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                // 兼容Android7.0使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, outputImagePath.getAbsolutePath());
                Uri uri = context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        return intent;
    }

    /*
    *  相册Intent
    * */
    public static Intent getSelectPhotoIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        return intent;
    }

    /*
    *  判断sdcard是否被挂载
    * */
    private static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /*
    *  Android4.4及以上处理图片的方法
    * */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getImageOnKitKatPath(Intent data, Context context) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("uri = intent.getData : ", ""+uri);
        if(DocumentsContract.isDocumentUri(context, uri)) {
            // 数据表里指定的行
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d("getDocumentId(uri) :", ""+docId);
            Log.d("uri.getAuthority() :", ""+uri.getAuthority());
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, context);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null, context);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null, context);
        }
        return imagePath;
    }

    /*
    *  通过uri和selection来获取真是的图片路径，从相册获取图片时要用到
    * */
    private static String getImagePath(Uri uri, String selection, Context context) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
        return path;
    }

    /*
    *  更改图片显示角度
    * */
    public static void updateImageDirection(String filepath, Bitmap bitmap, ImageView imageView) {
        // 图片旋转角度
        int degree = 0;
        // 根据图片的filepath获取到一个ExifInterface的对象
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        degree = 0;
                        break;
                }
            }
            // 如果degree不为0
            if (degree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(degree);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            }
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
    }

    /*
    *  Android4.4以下处理图片的方法
    * */
    public static String getImageBeforeKitKatPath(Intent data, Context context) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null, context);
        return imagePath;
    }

    /*
    *  比例压缩
    * */
    public static Bitmap compression(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        // 如果图片大于1M，进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (outputStream.toByteArray().length / 1024 > 1024) {
            // 重置outputStream，即清空outputSteam
            outputStream.reset();
            // 压缩50%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds设回true
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        options.inJustDecodeBounds = false;
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float height = 800f;//这里设置高度为800f
        float width = 480f;//这里设置宽度为480f

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int zoomRatio = 1;//be=1表示不缩放
        if (outWidth > outHeight && outWidth > width) {//如果宽度大的话根据宽度固定大小缩放
            zoomRatio = (int) (options.outWidth / width);
        } else if (outWidth < outHeight && outHeight > height) {//如果高度高的话根据宽度固定大小缩放
            zoomRatio = (int) (options.outHeight / height);
        }
        if (zoomRatio <= 0) {
            zoomRatio = 1;
        }
        options.inSampleSize = zoomRatio;//设置缩放比例
        options.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        //压缩好比例大小后再进行质量压缩
        bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        return bitmap;
    }
}
