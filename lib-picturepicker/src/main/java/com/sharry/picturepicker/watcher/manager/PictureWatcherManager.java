package com.sharry.picturepicker.watcher.manager;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;

import com.sharry.picturepicker.support.loader.IPictureLoader;
import com.sharry.picturepicker.support.loader.PictureLoader;
import com.sharry.picturepicker.support.permission.PermissionsCallback;
import com.sharry.picturepicker.support.permission.PermissionsManager;
import com.sharry.picturepicker.watcher.impl.PictureWatcherActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Sharry on 2018/6/19.
 * Email: SharryChooCHN@Gmail.com
 * Version: 1.0
 * Description: 图片查看器的管理类
 */
public class PictureWatcherManager {

    public static final String TAG = PictureWatcherManager.class.getSimpleName();

    public static PictureWatcherManager with(@NonNull Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return new PictureWatcherManager(activity);
        } else {
            throw new IllegalArgumentException("PictureWatcherManager.with -> Context can not cast to Activity");
        }
    }

    private String[] mPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private Activity mActivity;
    private WatcherConfig mConfig;
    private PictureWatcherFragment mWatcherFragment;
    private View mTransitionView;

    private PictureWatcherManager(Activity activity) {
        this.mActivity = activity;
        this.mWatcherFragment = getCallbackFragment(mActivity);
    }

    /**
     * 设置共享元素
     */
    public PictureWatcherManager setSharedElement(View transitionView) {
        mTransitionView = transitionView;
        return this;
    }

    /**
     * 设置图片预览的配置
     */
    public PictureWatcherManager setConfig(@NonNull WatcherConfig config) {
        this.mConfig = config;
        return this;
    }

    /**
     * 设置图片加载方案
     */
    public PictureWatcherManager setPictureLoader(@NonNull IPictureLoader loader) {
        PictureLoader.setPictureLoader(loader);
        return this;
    }

    /**
     * 调用图片查看器的方法
     */
    public void start() {
        startForResult(null);
    }

    /**
     * 调用图片查看器, 一般用于相册
     */
    public void startForResult(@Nullable final WatcherCallback callback) {
        // 1. 验证是否实现了图片加载器
        if (PictureLoader.getPictureLoader() == null) {
            throw new UnsupportedOperationException("PictureLoader.load -> please invoke setPictureLoader first");
        }
        // 2. 请求权限
        PermissionsManager.getManager(mActivity)
                .request(mPermissions)
                .execute(new PermissionsCallback() {
                    @Override
                    public void onResult(boolean granted) {
                        if (granted) {
                            startForResultActual(callback);
                        }
                    }
                });
    }

    /**
     * 真正执行 Activity 的启动
     */
    private void startForResultActual(@Nullable final WatcherCallback callback) {
        mWatcherFragment.setPickerCallback(callback);
        PictureWatcherActivity.startActivityForResult(mActivity, mWatcherFragment, mConfig, mTransitionView);
    }

    /**
     * 获取用于回调的 Fragment
     */
    private PictureWatcherFragment getCallbackFragment(Activity activity) {
        PictureWatcherFragment pictureWatcherFragment = findCallbackFragment(activity);
        if (pictureWatcherFragment == null) {
            pictureWatcherFragment = PictureWatcherFragment.newInstance();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().add(pictureWatcherFragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return pictureWatcherFragment;
    }

    /**
     * 在 Activity 中通过 TAG 去寻找我们添加的 Fragment
     */
    private PictureWatcherFragment findCallbackFragment(Activity activity) {
        return (PictureWatcherFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

}
