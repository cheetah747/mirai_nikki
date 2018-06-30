package com.sibyl.mirainikki.activity.ReadActivity.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来维护一个正在使用的文件路径树List
 * Created by Sasuke on 2016/6/27.
 */
public class FileTreeData {
    List<FilesItem> treeList;

    public FileTreeData(){
        treeList = new ArrayList<FilesItem>();
    }

    class FilesItem{
        String fileName;//文件名
        File file;//文件路径
        List<FilesItem> subList;//子级文件列表(点击后才获取，并缓存进来)
    }

}
