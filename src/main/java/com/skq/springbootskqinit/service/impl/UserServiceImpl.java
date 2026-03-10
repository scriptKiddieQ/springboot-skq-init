package com.skq.springbootskqinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skq.springbootskqinit.exception.BusinessException;
import com.skq.springbootskqinit.exception.ErrorCode;
import com.skq.springbootskqinit.exception.ThrowUtils;
import com.skq.springbootskqinit.model.entity.User;
import com.skq.springbootskqinit.service.UserService;
import com.skq.springbootskqinit.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @author qwe
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2026-03-10 10:53:47
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




