package com.systudio.exceluploaddemo;

import com.alibaba.fastjson.JSONObject;
import com.systudio.exceluploaddemo.argumentResolver.ExcelColumnOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PoiTest {
    @Test
    public void parseExcel() throws IOException, IllegalAccessException, InstantiationException {
        InputStream inputStream = new FileInputStream("d:/java_project/demo/user.xls");
        Class targetClass = User.class;


        List list = new ArrayList();
        Map<Integer, Field> fieldsMap = getFieldsMap(targetClass);
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            short lastCellNum = row.getLastCellNum();
            Object target = targetClass.newInstance();
            for (short columnIndex = 0; columnIndex < lastCellNum; columnIndex++) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    log.info("{}：{}——{}", rowIndex, columnIndex, cell.toString());
                    if (fieldsMap.containsKey(Integer.valueOf(columnIndex))) {
                        Field field = fieldsMap.get(Integer.valueOf(columnIndex));
                        field.set(target, cell.toString());
                    }
                }
            }
            list.add(target);
        }

        log.info("list:{}", JSONObject.toJSON(list));
    }

    private Map<Integer, Field> getFieldsMap(Class targetClass) {
        Map<Integer, Field> fieldsMap = new HashMap();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ExcelColumnOrder.class)) {
                ExcelColumnOrder excelColumnOrder = field.getAnnotation(ExcelColumnOrder.class);
                int value = excelColumnOrder.value();
                field.setAccessible(true);
                fieldsMap.put(Integer.valueOf(value), field);
            }
        }
        return fieldsMap;
    }
}
