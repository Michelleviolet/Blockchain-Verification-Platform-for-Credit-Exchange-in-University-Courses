package com.zyx.controller;


import com.zyx.VO.DecryptVO;
import com.zyx.VO.PageVO;
import com.zyx.VO.ResultVO;
import com.zyx.form.*;
import com.zyx.service.AuthorizeService;
import com.zyx.service.RecordService;
import com.zyx.util.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/authorize")
public class AuthorizeController {
    @Autowired
    private AuthorizeService authorizeService;


    //请求授权，上传到authorize表项
    @PostMapping("/request")
    public ResultVO create(@RequestBody RequestForm requestForm) throws Exception {
        Boolean request = this.authorizeService.request(requestForm);//返回值-创建成功或失败
        if (!request) {
            return ResultVOUtil.fail();
        } else {
            return ResultVOUtil.success(null);//此处不需要data的值
        }
    }



    @GetMapping("/managerlist/{id}/{page}/{size}")
    public ResultVO managerlist(@PathVariable("id") Integer studentId ,@PathVariable("page") Integer page,@PathVariable("size") Integer size){
        PageVO resultPage = this.authorizeService.managerlist(studentId,page,size);
        return ResultVOUtil.success(resultPage);
    }

    @PostMapping("/confirm")
    public ResultVO<Object> confirmAuthorization(@RequestBody ManageForm manageForm) {
        return authorizeService.confirmAuthorization(manageForm);
    }

    //解密
    @PostMapping("/decrypt")
    public ResultVO decrypt(@RequestBody TDecryptForm tdecryptForm) throws Exception {
        DecryptVO resultDecrypt=this.authorizeService.tdecrypt(tdecryptForm);
        return ResultVOUtil.success(resultDecrypt);
    }





}

