package com.zhihu.daily.meizu.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

public class ImageUtils {
	private Context context;
	private RequestQueue queue;
	private final ImageLoader imageLoader;
	private static ImageUtils imageUtils;
	private final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
	private final int cacheSize = maxMemory / 8;
	private LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(
			cacheSize) {
		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			return bitmap.getByteCount();
		}
	};
	private ImageCache imageCache = new ImageCache() {
		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mMemoryCache.put(url, bitmap);
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mMemoryCache.get(url);
		}
	};
	
	public void clear(){
		mMemoryCache = new LruCache<String, Bitmap>(
				cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};
	}
	
	public ImageCache getImageCache(){
		return imageCache;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	private ImageUtils(Context context) {
		this.context = context;
		queue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(queue, imageCache);
	}

	public static ImageUtils getInstance(Context context) {
		if (imageUtils == null)
			imageUtils = new ImageUtils(context);

		return imageUtils;
	}

	public void getImage(ImageView imageView, String path,int defaultDrawable) {
		ImageListener imageListener = ImageLoader.getImageListener(imageView,
				defaultDrawable, defaultDrawable);
		imageLoader.get(path, imageListener,context);
	}
}
