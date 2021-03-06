package com.sibyl.mirainikki.reposity;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.sibyl.mirainikki.MyToast.MyToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sasuke on 2016/5/10.
 */
public class FileData {
    public static File sdFile;
    private static File nikkiFile;//应该是当前年份的mirai文件
    public static File miraiCacheFile;//用来存系统剪切板里的文字的，写到txt里，然后再把txt用solid explorer传到电脑，实现手机->电脑的剪切板同步。
    public static File rootFile;
    public static String tail = ".mirai";
    public static String fileProviderAuth = "com.sibyl.mirainikki.fileProvider";
    public static File nomediaFile;

    public static void initFilePath(){
        try {
            /*sdFile = new File(Environment.getExternalStorageDirectory().getCanonicalPath().toString()
            +"/Android/data");*/
            sdFile = new File(Environment.getExternalStorageDirectory().getCanonicalPath().toString());
            rootFile = new File(sdFile.getAbsolutePath() + File.separator + "0カード/しりょう/ドキュメント/MIRAINIKKI/");
            nomediaFile = new File(rootFile.getCanonicalPath() + File.separator + ".nomedia");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //にっきrootフォルダのディレクトリ
//        rootFile = new File(sdFile.toString()+"/Android/data/にっき");
        if(!rootFile.exists()){
            rootFile.mkdirs();
        }

        //メディアファイルを排除
        if(!nomediaFile.exists()){
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //如果日期是空的，因为后面要直接调用，所以一定要校验
        if(TextUtils.isEmpty(TimeData.getDate())){
            TimeData.initNow();
        }
        //就直接放在应用目录的 /files 文件夹里算了。。。
        getNikkiFile();
//        String nikkiFilePath = rootFile.getAbsolutePath().toString()
////                                +File.separator+TimeData.getYearMonth()
////                                +File.separator+TimeData.getDate() + tail;
//                                    +File.separator +TimeData.getYear() + tail;
//        nikkiFile = new File(nikkiFilePath);

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
        //如果年份有变化，那就需要重新用TimeData.getYear()再获取一下，不能直接读静态变量
        String nikkiFilePath = rootFile.getAbsolutePath().toString()
//                                +File.separator+TimeData.getYearMonth()
//                                +File.separator+TimeData.getDate() + tail;
                +File.separator +TimeData.getYear() + tail;
        nikkiFile = new File(nikkiFilePath);
        return nikkiFile;
    }

    public static File getMiraiCacheFile(){
//        return miraiCacheFile;
        return new File(sdFile.getAbsolutePath().toString() + File.separator + "0カード"
                +File.separator + "MiraiCache"
                + (new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date()))
                +".url");
    }

    public static File getRootFile() {
        return rootFile;
    }

    public static String getBackgroundImgCache(){
        return FileData.getRootFile().getAbsolutePath() + File.separator + "BACKGROUND.JPG";
    }

    public static String getYouIconCache(){
        return FileData.getRootFile().getAbsolutePath() + File.separator + "YOU.JPG";
    }

    public static String getMeIconCache(){
        return FileData.getRootFile().getAbsolutePath() + File.separator + "ME.JPG";
    }
}
