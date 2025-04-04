package com.zyx.controller;


import com.zyx.VO.ResultVO;
import com.zyx.form.LoginForm;
import com.zyx.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
   private TeacherService teacherService;

    @GetMapping("/login")
    public ResultVO login(LoginForm loginForm){
        ResultVO resultVO=this.teacherService.login(loginForm);
        return resultVO;


    }
}

