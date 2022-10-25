package com.blog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {

    public static String generateFilePath(String fileName){
        //根据日期生成路径   2022/1/15/ 当前日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = sdf.format(new Date());
        //uuid作为文件名 生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀和文件后缀一致 原始文件后缀
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg
        String fileType = fileName.substring(index);
        //拼接
        return new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }
}