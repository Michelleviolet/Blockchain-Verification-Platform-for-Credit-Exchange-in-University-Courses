package com.zyx.service;

import com.zyx.VO.ResultVO;
import com.zyx.entity.Teacher;
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
public interface TeacherService extends IService<Teacher> {
    public ResultVO login(LoginForm loginForm);

}
