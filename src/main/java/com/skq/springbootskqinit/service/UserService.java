package com.skq.springbootskqinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.skq.springbootskqinit.model.dto.UserQueryRequest;
import com.skq.springbootskqinit.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skq.springbootskqinit.model.vo.LoginUserVO;
import com.skq.springbootskqinit.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author qwe
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-03-10 10:53:47
*/
public interface UserService extends IService<User> {

    // region 用户基本功能

    // 密码加密盐值（实际项目建议放到配置文件中）
    String getEncryptPassword(String userPassword);

    /**
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    LoginUserVO getLoginUserVO(User user);

    User getLoginUser(HttpServletRequest request);

    UserVO getUserVO(User user);

    List<UserVO> getUserVOList(List<User> userList);

    boolean userLogout(HttpServletRequest request);

    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    // endregion
}
