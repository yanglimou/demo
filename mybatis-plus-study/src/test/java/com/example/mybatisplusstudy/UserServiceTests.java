package com.example.mybatisplusstudy;

import com.example.mybatisplusstudy.entity.User;
import com.example.mybatisplusstudy.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class UserServiceTests {
    @Autowired
    private IUserService iUserService;

    @Test
    public void testSelect() {
//        User byId = iUserService.getById(1);
//        log.info("{}", byId);
//        iUserService.list().forEach(user->log.info("{}",user));
        List<User> admin = iUserService.lambdaQuery().eq(User::getName, "admin").list();
        log.info("{}",admin);
    }

}
