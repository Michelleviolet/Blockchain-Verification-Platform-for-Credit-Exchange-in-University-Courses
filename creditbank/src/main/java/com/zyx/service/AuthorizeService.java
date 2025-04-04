package com.zyx.service;

import com.zyx.VO.DecryptVO;
import com.zyx.VO.PageVO;
import com.zyx.VO.ResultVO;
import com.zyx.entity.Authorize;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyx.form.CreateForm;
import com.zyx.form.ManageForm;
import com.zyx.form.RequestForm;
import com.zyx.form.TDecryptForm;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
public interface AuthorizeService extends IService<Authorize> {
    public Boolean request(RequestForm requestForm)throws Exception;

    public PageVO managerlist(Integer studentId, Integer page, Integer size);
    public ResultVO<Object> confirmAuthorization(ManageForm manageForm);

    public DecryptVO tdecrypt(TDecryptForm tdecryptForm)throws Exception;


}
