package com.zyx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyx.VO.ResultVO;
import com.zyx.entity.Student;
import com.zyx.entity.Teacher;
import com.zyx.form.LoginForm;
import com.zyx.mapper.StudentMapper;
import com.zyx.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public ResultVO login(LoginForm loginForm) {
        //1.判断用户是否存在（手机号）
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("phone",loginForm.getPhone());
        Student student=getOne(queryWrapper);
        ResultVO resultVO=new ResultVO();
        if(student==null){
            resultVO.setCode(-1);
        }else{// 2.判断密码是否正确
            if(!student.getPassword().equals(loginForm.getPassword())){
                resultVO.setCode(-2);
            }else {
                resultVO.setCode(0);
                resultVO.setData(student);
            }

        }
        return resultVO;

    }

}
