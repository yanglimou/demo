package com.systudio.exceluploaddemo.argumentResolver;

import java.lang.annotation.*;
/**
* @description: 实体类属性注解，用于和excel的列索引进行映射
 * excel每一列的数据到底应该和对象的哪一个字段匹配我们是不知道的，所以通过这个索引来实现这个映射关系
* @author: yanglimou
* @time: 2020/5/29 0:29
*/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumnOrder {
    int value();
}
