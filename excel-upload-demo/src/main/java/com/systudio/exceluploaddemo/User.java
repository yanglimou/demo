package com.systudio.exceluploaddemo;

import com.systudio.exceluploaddemo.argumentResolver.ExcelColumnOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @ExcelColumnOrder(0)
    private String id;
    @ExcelColumnOrder(1)
    private String name;
}
