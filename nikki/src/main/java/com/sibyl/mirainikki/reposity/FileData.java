package com.sibyl.mirainikki.reposity;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.sibyl.mirainikki.MyApplication.MyApplication;
import com.sibyl.mirainikki.MyToast.MyToast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Sasuke on 2016/5/10.
 */
public class FileData {
    public static File sdFile;
    public static File nikkiFile;
    public static File miraiCacheFile;//用来存系统剪切板里的文字的，写到txt里，然后再把txt用solid explorer传到电脑，实现手机->电脑的剪切板同步。
    public static File rootFile;
    public static String tail = ".mirai";

    public static void initFilePath(){
        try {
            /*sdFile = new File(Environment.getExternalStorageDirectory().getCanonicalPath().toString()
            +"/Android/data");*/
            sdFile = new File(Environment.getExternalStorageDirectory().getCanonicalPath().toString());
            rootFile = MyApplication.getInstance().getFilesDir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //にっきrootフォルダのディレクトリ
//        rootFile = new File(sdFile.toString()+"/Android/data/にっき");
        if(!rootFile.exists()){
            rootFile.mkdirs();
        }

        //如果日期是空的，因为后面要直接调用，所以一定要校验
        if(TextUtils.isEmpty(TimeData.getDate())){
            TimeData.initNow();
        }
        //就直接放在应用目录的 /files 文件夹里算了。。。
        String nikkiFilePath = rootFile.getAbsolutePath().toString()
//                                +File.separator+TimeData.getYearMonth()
//                                +File.separator+TimeData.getDate() + tail;
                                    +File.separator +TimeData.getYear() + tail;
        nikkiFile = new File(nikkiFilePath);

        String miraiCacheFilePath = sdFile.getAbsolutePath().toString() + File.separator + "0カード" + File.separator + "MiraiCache.txt";
        miraiCacheFile = new File(miraiCacheFilePath);

}


    /**
     * 444 r--r--r--
     600 rw-------
     644 rw-r--r--
     666 rw-rw-rw-
     700 rwx------
     744 rwxr--r--
     755 rwxr-xr-x
     777 rwxrwxrwx
     * @param view
     */
    public static void createMyFile(Context view,File file){
//        File monthFolder = new File(rootFile.getAbsoluteFile().toString()+File.separator+TimeData.getYearMonth());
//        if(!monthFolder.exists()){
//            monthFolder.mkdirs();
//        }

        if(file == null || !file.exists()){
            try {
                file.createNewFile();
                Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
            } catch (IOException e) {
                MyToast.show(view, "ファイルの作成は失敗しました", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }
    }

    public static File getSdFile() {
        return sdFile;
    }


    public static File getNikkiFile() {
        return nikkiFile;
    }

    public static File getMiraiCacheFile(){
        return miraiCacheFile;
    }

    public static File getRootFile() {
        return rootFile;
    }
}
