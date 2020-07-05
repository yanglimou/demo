package com.systudio.exceluploaddemo.argumentResolver;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @description: ExcelToList注解的参数处理器
* @author: yanglimou
* @time: 2020/5/29 0:21
*/
@Slf4j
public class ExcelToListHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    //有ExcelToList注解的参数走下面的处理方法
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ExcelToList.class);
    }
    //将文件变成List的具体方法
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        log.info("进入解析器");
        //获取参数注解
        ExcelToList parameterAnnotation = methodParameter.getParameterAnnotation(ExcelToList.class);
        String parameterName = parameterAnnotation.value();
        log.info("文件参数名是：{}", parameterName);
        //获取参数类型
        Class parameterClass = parameterAnnotation.className();
        log.info("参数类型是：{}", parameterClass.getName());
        //获取跳过行
        int skipRows = parameterAnnotation.skipRows();
        log.info("跳过行：{}", skipRows);
        //获取上传文件
        MultipartHttpServletRequest multipartHttpServletRequest = nativeWebRequest.getNativeRequest(MultipartHttpServletRequest.class);
        MultipartFile file = multipartHttpServletRequest.getFile(parameterName);
        log.info("文件名：{}", file.getOriginalFilename());
        return parseExcel(file.getInputStream(), parameterClass, skipRows);
    }
    //将Excel输入流转成具体实体类的List集合
    private List parseExcel(InputStream inputStream, Class targetClass, int skipRows) throws IOException, IllegalAccessException, InstantiationException {
        List list = new ArrayList();
        Map<Integer, Field> fieldsMap = getFieldsMap(targetClass);
        //通过输入流得到WorkBook，excel处理对象
        Workbook workbook = WorkbookFactory.create(inputStream);
        //得到第一个sheet页
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        //循环行
        for (int rowIndex = skipRows; rowIndex <= lastRowNum; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            short lastCellNum = row.getLastCellNum();
            //通过反射创建实体对象
            Object target = targetClass.newInstance();
            //循环列
            for (short columnIndex = 0; columnIndex < lastCellNum; columnIndex++) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    log.info("{}：{}——{}", rowIndex, columnIndex, cell.toString());
                    //将excel中具体的之写入到对象的属性中，通过列索引和对象属性ExcelColumnOrder注解的值来进行一一映射
                    if (fieldsMap.containsKey(Integer.valueOf(columnIndex))) {
                        Field field = fieldsMap.get(Integer.valueOf(columnIndex));
                        field.set(target, cell.toString());
                    }
                }
            }
            list.add(target);
        }

        log.info("list:{}", JSONObject.toJSON(list));
        return list;
    }
    //将对象类型得到字段注解值和字段的map集合，方便parseExcel方法知道某一列的数据应该写到哪一个属性上
    private Map<Integer, Field> getFieldsMap(Class targetClass) {
        Map<Integer, Field> fieldsMap = new HashMap();
        //获取全部属性字段
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //只有有ExcelColumnOrder注解的字段来才会写数据
            if (field.isAnnotationPresent(ExcelColumnOrder.class)) {
                ExcelColumnOrder excelColumnOrder = field.getAnnotation(ExcelColumnOrder.class);
                int value = excelColumnOrder.value();
                //将属性设置成可访问，避免一些private属性无法通过反射写入数据
                field.setAccessible(true);
                fieldsMap.put(Integer.valueOf(value), field);
            }
        }
        return fieldsMap;
    }

}
