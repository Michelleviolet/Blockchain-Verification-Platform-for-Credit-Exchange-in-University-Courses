package com.zyx.service;

import com.zyx.VO.ResultVO;
import com.zyx.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyx.form.LoginForm;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
public interface StudentService extends IService<Student> {
    public ResultVO login(LoginForm loginForm);
}
