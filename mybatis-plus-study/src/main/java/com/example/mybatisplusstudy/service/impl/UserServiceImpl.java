package com.example.mybatisplusstudy.service.impl;

import com.example.mybatisplusstudy.entity.User;
import com.example.mybatisplusstudy.mapper.UserMapper;
import com.example.mybatisplusstudy.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ylm
 * @since 2020-07-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
