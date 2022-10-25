package com.blog.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    //bean拷贝
    //单个拷贝
    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象  newInstance()默认空参构建
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    //集合拷贝
    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        //stream()流形式    map转换  collect收集操作
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());

    }
}
