package com.example.coursedesign.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class TransitionBundleFactory {
    public static final String TEMP_IMAGE_FILE_NAME = "activity_transition_image.png";
    private static final String TAG = "Transition";

    public static Bundle createTransitionBundle(Context context, View fromView, Bitmap bitmap) {
        // Bitmap is Optional
        String imageFilePath = null;
        if (bitmap != null) {
            TransitionAnimation.bitmapCache = new WeakReference<>(bitmap);
            imageFilePath = saveImage(context, bitmap);
        }
        int[] screenLocation = new int[2];
        fromView.getLocationOnScreen(screenLocation);
        final TransitionData transitionData = new TransitionData(context, screenLocation[0], screenLocation[1], fromView.getMeasuredWidth(), fromView.getMeasuredHeight(), imageFilePath);
        return transitionData.getBundle();
    }

    /**
     * 创建TransitionBundle
     *
     * @param context 上下文
     * @param fromView 动画起始的View
     * @param imageFilePath 图片url
     *
     * */
    public static Bundle createTransitionBundle(Context context, View fromView, String imageFilePath) {
        int[] screenLocation = new int[2];
        fromView.getLocationOnScreen(screenLocation);
        final TransitionData transitionData = new TransitionData(context, screenLocation[0], screenLocation[1], fromView.getMeasuredWidth(), fromView.getMeasuredHeight(), imageFilePath);
        return transitionData.getBundle();
    }

    private static String saveImage(final Context context, final Bitmap bitmap) {
        final String imageSavePath = context.getFilesDir().getAbsolutePath() + "/activity_transition/";
        new File(imageSavePath).mkdirs();
        final File imageFile = new File(imageSavePath, TEMP_IMAGE_FILE_NAME);
        final String imageFilePath = imageFile.getAbsolutePath();
        new Thread(new Runnable() {
            @Override
            public void run() {
                TransitionAnimation.isImageFileReady = false;
                Boolean isDebug = (Boolean) BuildConfigUtils.getBuildConfigValue(context, "DEBUG");
                BufferedOutputStream bos = null;
                try {
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                    imageFile.createNewFile();
                    bos = new BufferedOutputStream(new FileOutputStream(imageFile));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                } catch (FileNotFoundException e) {
                    if (isDebug) {
                        Log.i(TAG, "file not found", e);
                    }
                } catch (IOException e) {
                    if (isDebug) {
                        Log.i(TAG, "can't create file", e);
                    }
                } finally {
                    try {
                        bos.close();
                    } catch (Exception e) {
                        if (isDebug) {
                            //IOException, NullPointerException
                            Log.i(TAG, "fail save image", e);
                        }
                    }
                    TransitionAnimation.isImageFileReady = true;
                }
                synchronized (TransitionAnimation.lock) {
                    TransitionAnimation.lock.notify();
                }
            }
        }).start();
        return imageFilePath;
    }

}
