package com.zyx.controller;


import com.zyx.VO.DecryptVO;
import com.zyx.VO.PageVO;
import com.zyx.VO.RecordVO;
import com.zyx.VO.ResultVO;
import com.zyx.entity.Record;
import com.zyx.form.CreateForm;
import com.zyx.form.DecryptForm;
import com.zyx.form.RecordChainRequest;
import com.zyx.mapper.RecordMapper;
import com.zyx.service.RecordService;
import com.zyx.util.ResultVOUtil;
import lombok.val;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    //新建档案
    @PostMapping("/create")
    public ResultVO create(@RequestBody CreateForm createForm) throws Exception {
        Boolean create = this.recordService.create(createForm);//返回值-创建成功或失败
        if (!create) {
            return ResultVOUtil.fail();
        } else {
            return ResultVOUtil.success(null);//此处不需要data的值
        }
    }

    //查询mycord
    @GetMapping("/studentlist/{id}/{page}/{size}")
    public ResultVO studentlist(@PathVariable("id") Integer studentId ,@PathVariable("page") Integer page,@PathVariable("size") Integer size){
        PageVO resultPage = this.recordService.studentList(studentId,page,size);//record表里的所有内容
        return ResultVOUtil.success(resultPage);
    }

    //查询viewrcord
    @GetMapping("/teacherlist/{id}/{page}/{size}")
    public ResultVO teacherlist(@PathVariable("id") Integer teacherId ,@PathVariable("page") Integer page,@PathVariable("size") Integer size){
        PageVO resultPage = this.recordService.teacherList(teacherId,page,size);//record表里的所有内容
        return ResultVOUtil.success(resultPage);
    }


    //解密
    @PostMapping("/decrypt")
    public ResultVO decrypt(@RequestBody DecryptForm decryptForm) throws Exception {
        DecryptVO resultDecrypt=this.recordService.decrypt(decryptForm);
        return ResultVOUtil.success(resultDecrypt);
    }
    // 确认档案
    @PostMapping("/affirm")
    public ResultVO affirmAndUploadToChain(@RequestBody RecordChainRequest request) {
        try {
            // 1. 更新数据库
            recordService.updateAffirmStatus(request.getId(), 1);

            // 2. 调用区块链
            TransactionResponse response = recordService.uploadToBlockchain(
                    request.getId().toString(),
                    request.getDescription(),
                    request.getRemark()
            );

            return ResultVOUtil.success(response.getReturnObject());
        } catch (Exception e) {
            return ResultVOUtil.fail();
        }
    }




}

