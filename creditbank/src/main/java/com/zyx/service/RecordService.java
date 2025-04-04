package com.zyx.service;

import com.zyx.VO.DecryptVO;
import com.zyx.VO.PageVO;
import com.zyx.VO.RecordVO;
import com.zyx.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyx.form.CreateForm;
import com.zyx.form.DecryptForm;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
public interface RecordService extends IService<Record> {
public Boolean create(CreateForm createForm) throws Exception;
public PageVO studentList(Integer studentId, Integer page, Integer size);
public PageVO teacherList(Integer teacherId,Integer page, Integer size);
public DecryptVO decrypt(DecryptForm decryptForm) throws Exception;
void updateAffirmStatus(Integer id, Integer affirm);
public TransactionResponse uploadToBlockchain(String recordId, String description, String remark) throws Exception;
}
