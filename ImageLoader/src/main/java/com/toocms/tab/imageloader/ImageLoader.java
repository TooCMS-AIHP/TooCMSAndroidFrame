package com.toocms.tab.imageloader;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.toocms.tab.imageloader.cache.CacheCallback;
import com.toocms.tab.imageloader.cache.ImageCacheAsyncTask;
import com.toocms.tab.imageloader.transform.GlideCircleTransform;
import com.toocms.tab.imageloader.transform.GlideRoundTransform;

import java.io.File;

/**
 * 异步加载图片类
 * <p>
 * <font color='red'>废弃第一个参数为Context的所有方法</font>
 * <p>
 * <font color='red'>废弃原因：当Activity/Fragment销毁时Glide的实例没有被销毁，导致出现崩溃异常</font>
 *
 * @author Zero @date 2017年3月18日
 * @version 4.0
 */
public class ImageLoader {

    private static final String FILE = "file://";

    /**
     * 通过URL加载图片
     *
     * @param url
     * @param imageView
     * @param loadingImage
     */
    public static void loadUrl2Image(String url, ImageView imageView, @DrawableRes int loadingImage) {
        GlideLoader.create(imageView).loadImage(url, loadingImage, null);
    }

    /**
     * 加载资源图片
     *
     * @param resId
     * @param imageView
     * @param loadingImage
     */
    public static void loadResId2Image(@DrawableRes int resId, ImageView imageView, @DrawableRes int loadingImage) {
        GlideLoader.create(imageView).load(resId, loadingImage, null);
    }

    /**
     * 通过FILE加载图片
     *
     * @param file
     * @param imageView
     * @param loadingImage
     */
    public static void loadFile2Image(File file, ImageView imageView, @DrawableRes int loadingImage) {
        GlideLoader.create(imageView).loadImage(FILE + file.getAbsolutePath(), loadingImage, null);
    }

    /**
     * 通过FILE加载图片
     *
     * @param localPath
     * @param imageView
     * @param loadingImage
     */
    public static void loadFile2Image(String localPath, ImageView imageView, @DrawableRes int loadingImage) {
        GlideLoader.create(imageView).loadImage(FILE + localPath, loadingImage, null);
    }

    /**
     * 通过URL加载圆形图片
     *
     * @param url
     * @param imageView
     * @param loadingImage
     */
    public static void loadUrl2CircleImage(String url, ImageView imageView, @DrawableRes int loadingImage) {
        GlideLoader.create(imageView).loadImage(url, loadingImage, new GlideCircleTransform());
    }

    /**
     * 加载资源文件为圆形图片
     *
     * @param resId
     * @param imageView
     * @param loadingImage
     */
    public static void loadResId2CircleImage(@DrawableRes int resId, ImageView imageView, @DrawableRes int loadingImage) {
        GlideLoader.create(imageView).load(resId, loadingImage, new GlideCircleTransform());
    }

    /**
     * 通过URL加载圆角图片
     *
     * @param url
     * @param imageView
     * @param loadingImage
     * @param radius
     */
    public static void loadUrl2RoundImage(String url, ImageView imageView, @DrawableRes int loadingImage, int radius) {
        GlideLoader.create(imageView).loadImage(url, loadingImage, new GlideRoundTransform(radius));
    }

    /**
     * 加载资源文件为圆角图片
     *
     * @param resId
     * @param imageView
     * @param loadingImage
     * @param radius
     */
    public static void loadResId2RoundImage(@DrawableRes int resId, ImageView imageView, @DrawableRes int loadingImage, int radius) {
        GlideLoader.create(imageView).load(resId, loadingImage, new GlideRoundTransform(radius));
    }

    /**
     * 通过URL获取图片文件
     *
     * @param url
     * @param callback
     */
    public static void loadUrl2File(String url, CacheCallback callback) {
        new ImageCacheAsyncTask(callback).execute(url);
    }
}
