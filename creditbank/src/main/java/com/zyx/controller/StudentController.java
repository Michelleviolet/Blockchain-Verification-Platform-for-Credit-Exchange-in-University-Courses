package com.zyx.controller;


import com.zyx.VO.ResultVO;
import com.zyx.form.LoginForm;
import com.zyx.service.StudentService;
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
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public ResultVO login(LoginForm loginForm) {
        ResultVO resultVO = this.studentService.login(loginForm);
        return resultVO;
    }
}

