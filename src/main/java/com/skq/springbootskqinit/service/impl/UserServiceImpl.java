package com.skq.springbootskqinit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skq.springbootskqinit.exception.BusinessException;
import com.skq.springbootskqinit.exception.ErrorCode;
import com.skq.springbootskqinit.exception.ThrowUtils;
import com.skq.springbootskqinit.model.dto.UserQueryRequest;
import com.skq.springbootskqinit.model.entity.User;
import com.skq.springbootskqinit.model.vo.LoginUserVO;
import com.skq.springbootskqinit.model.vo.UserVO;
import com.skq.springbootskqinit.service.UserService;
import com.skq.springbootskqinit.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.skq.springbootskqinit.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author qwe
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2026-03-10 10:53:47
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{


    //region 用户基本功能

    // 密码加密盐值（实际项目建议放到配置文件中）
    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "your-salt-value";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
    /**
     * 用户注册方法
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 注册成功返回用户ID，失败抛出异常
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 基础参数校验
        if (!StringUtils.hasText(userAccount) || !StringUtils.hasText(userPassword) || !StringUtils.hasText(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度不能小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码长度不能小于8位");
        }

        // 2. 账号格式校验（示例：仅允许字母和数字）
        String validPattern = "^[a-zA-Z0-9]+$";
        if (!Pattern.matches(validPattern, userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号仅支持字母和数字");
        }

        // 3. 两次密码一致校验
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        // 4. 账号唯一性校验
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该账号已存在");
        }

        // 5. 密码加密（MD5 + 盐值）
        String encryptPassword = getEncryptPassword(userPassword);

        // 6. 构建用户对象并入库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，请重试");
        }

        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR, "参数为空");
        ThrowUtils.throwIf(userAccount.length() < 4 || userPassword.length() < 8,
                ErrorCode.PARAMS_ERROR, "账号或密码格式错误");
        // 2.对用户传递的密码加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 3. 使用 LambdaQueryWrapper 查询数据库中的用户是否存在
        User user = this.lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword)
                .one();
//        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户不存在");
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 4.保存用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        return this.getLoginUserVO(user);
    }

    /**
     * 获取脱敏类的用户信息
     *
     * @param user
     * @return
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 判断是否已经登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库中查询（追求性能可注释，直接返回上述结果（上述会导致修改用户名后仍旧拿到旧值））
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR, "请求参数为空");
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    // endregion
}




