package com.systudio.exceluploaddemo.argumentResolver;

import java.lang.annotation.*;
/**
* @description: 自定义注解，通过这个注解，spring知道这里需要将文件变成List
* @author: yanglimou
* @time: 2020/5/29 0:19
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface ExcelToList {
    //文件参数名，前端传递过来的文件对应的参数名
    String value() default "file";
    //要解析成的List中的对象类型
    Class className();
    //解析excel的时候需要跳过几行，有时候excel中一行或者二行标题
    int skipRows() default 1;
}
