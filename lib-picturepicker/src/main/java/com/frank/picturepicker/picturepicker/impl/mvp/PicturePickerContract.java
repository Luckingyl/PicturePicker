package com.frank.picturepicker.picturepicker.impl.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.ImageView;

import com.frank.picturepicker.picturepicker.impl.data.PictureFolder;
import com.frank.picturepicker.picturepicker.manager.PickerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 2018/6/13.
 * Email: frankchoochina@gmail.com
 * Version: 1.0
 * Description: PicturePicture MVP 的约束
 */
public interface PicturePickerContract {

    interface IView {

        /**
         * 显示选中的图片文件夹
         *
         * @param folderName 文件夹的名称
         * @param uris       文件夹中的数据
         */
        void displayPictures(String folderName, List<String> uris);

        /**
         * 更新确定和预览文本的内容
         */
        void updateEnsureAndPreviewTextContent(int curPicked, int total);

        /**
         * 通知选中的图片集合变更了
         */
        void notifyUserPickedSetChanged();

        /**
         * 通过相机拍摄了一张照片
         * @param path
         */
        void notifyCameraTakeOnePicture(String path);

        /**
         * 展示消息通知
         */
        void showMsg(String msg);

        /**
         * 从资源文件获取 String
         */
        String getString(@StringRes int resId);
    }

    interface IPresenter {

        /**
         * 绑定 View
         */
        void attach(IView view);

        /**
         * 配置用户已经选中的图片集合
         *
         * @param userPicked 用户已经选中的图片集合
         */
        void setupUserPickedSet(ArrayList<String> userPicked);

        /**
         * 配置阈值
         */
        void setupThreshold(int threshold);

        /**
         * 初始化 Model 的数据
         */
        void initData(Context context, PickerConfig config);

        /**
         * 获取需要展示的图片
         *
         * @param position
         */
        void fetchDisplayPictures(int position);

        /**
         * 获取所有图片文件夹
         */
        ArrayList<PictureFolder> fetchAllPictureFolders();

        /**
         * 获取用户选中的所有图片
         */
        ArrayList<String> fetchUserPickedSet();

        /**
         * 处理图片被选中了
         */
        boolean performPictureChecked(String imagePath);

        /**
         * 处理图片被移除了
         */
        void performPictureUnchecked(String imagePath);

        /**
         * 处理图片被点击了
         */
        void performPictureClicked(ArrayList<String> curPaths, int position, ImageView sharedElement);

        /**
         * 处理预览按钮被点击了
         */
        void performPreviewClicked();

        /**
         * 处理底部菜单被点击了
         */
        void performBottomMenuClicked();

        /**
         * 确认按钮被点击了
         */
        void performEnsureClicked();

        /**
         * 相机按钮被点击了
         */
        void performCameraClicked();
    }

    interface IModel {

        /**
         * 设置用户已经选中的图片集合
         */
        void setUserPickedSet(@NonNull ArrayList<String> userPicked);

        /**
         * 获取系统图片
         */
        void getSystemPictures(Context context, final ModelInitializeCallback listener);

        /**
         * 设置图片选择的阈值
         */
        void setThreshold(int threshold);

        /**
         * 获取图片选择的阈值
         */
        int getThreshold();

        /**
         * 获取当前需要显示的文件模型
         */
        PictureFolder getPictureFolderAt(int index);

        /**
         * 获取所有的图片文件夹
         */
        ArrayList<PictureFolder> getAllPictureFolders();

        /**
         * 获取当前正在展示的图片集合
         */
        PictureFolder getCurDisplayFolder();

        /**
         * 获取用户选中的图片
         */
        ArrayList<String> getUserPickedSet();

        /**
         * 添加用户选中的图片
         */
        void addPickedPicture(String imagePath);

        /**
         * 移除用户选中的图片
         */
        void removePickedPicture(String imagePath);
    }

    interface ModelInitializeCallback {
        void onComplete(List<PictureFolder> pictureFolders);

        void onFailed(Throwable throwable);
    }
}
