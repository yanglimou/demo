package com.systudio.exceluploaddemo;

import com.systudio.exceluploaddemo.argumentResolver.ExcelColumnOrder;
import lombok.Data;

@Data
public class Device {
    @ExcelColumnOrder(2)
    private String status;
    @ExcelColumnOrder(3)
    private String style;
    @ExcelColumnOrder(1)
    private String name;
    @ExcelColumnOrder(0)
    private String id;
}
