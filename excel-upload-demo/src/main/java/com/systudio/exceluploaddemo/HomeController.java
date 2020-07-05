package com.systudio.exceluploaddemo;

import com.systudio.exceluploaddemo.argumentResolver.CurrentUser;
import com.systudio.exceluploaddemo.argumentResolver.ExcelToList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class HomeController {


    @RequestMapping("/")
    public User index(@CurrentUser User user) {
        return user;
    }

    @RequestMapping("/excelToUser")
    public List<User> excelToUser(@ExcelToList(className = User.class) List<User> userList) {
        return userList;
    }

    @RequestMapping("/excelToDevice")
    public List<Device> excelToDevice(@ExcelToList(className = Device.class) List<Device> deviceList) {
        return deviceList;
    }
}   
