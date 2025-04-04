package com.zyx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyx.VO.ResultVO;
import com.zyx.entity.Teacher;
import com.zyx.form.LoginForm;
import com.zyx.mapper.TeacherMapper;
import com.zyx.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {



    @Override
    public ResultVO login(LoginForm loginForm) {
      //1.判断用户是否存在（手机号）
        QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("phone",loginForm.getPhone());
        Teacher teacher=getOne(queryWrapper);
        ResultVO resultVO=new ResultVO();
        if(teacher==null){
            resultVO.setCode(-1);
        }else{// 2.判断密码是否正确
           if(!teacher.getPassword().equals(loginForm.getPassword())){
               resultVO.setCode(-2);
           }else {
               resultVO.setCode(0);
               resultVO.setData(teacher);
           }

        }
        return resultVO;

    }
}
