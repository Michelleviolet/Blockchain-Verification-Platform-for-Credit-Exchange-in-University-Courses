package com.zyx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyx.VO.*;
import com.zyx.entity.Authorize;
import com.zyx.entity.Teacher;
import com.zyx.form.*;
import com.zyx.mapper.AuthorizeMapper;
import com.zyx.mapper.RecordMapper;
import com.zyx.mapper.StudentMapper;
import com.zyx.mapper.TeacherMapper;
import com.zyx.service.AuthorizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyx.util.BlockchainException;
import com.zyx.util.CommonUtil;
import com.zyx.util.RSAUtil;
import com.zyx.util.ResultVOUtil;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
@Service
public class AuthorizeServiceImpl extends ServiceImpl<AuthorizeMapper, Authorize> implements AuthorizeService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AuthorizeMapper authorizeMapper;
    @Override
    public Boolean request(RequestForm requestForm) throws Exception {

        Authorize authorize=new Authorize();
        authorize.setRecordId(requestForm.getRecordid());
        authorize.setTeacherId(requestForm.getTeacherid());
        authorize.setDescriptionEnbyt("无数据，请申请授权");
        authorize.setRemarkEnbyt("无数据，请申请授权");
        authorize.setState(0);

        authorize.setCreatetime(CommonUtil.createDate());
        int insert = this.authorizeMapper.insert(authorize);
        if (insert != 1) {return false;}
        else {return true; }

    }

    @Override
    public PageVO managerlist(Integer studentId, Integer page, Integer size) {
        // 1. 计算偏移量
        long offset = (long) (page - 1) * size;

        // 2. 查询分页数据
        List<ManagerVO> managerVOList = authorizeMapper.selectManagerList(
                studentId, size, offset
        );

        // 3. 查询总数
        Long total = authorizeMapper.countManagerList(studentId);

        // 4. 构造分页结果
        PageVO pageVO = new PageVO();
        pageVO.setData(managerVOList);
        pageVO.setTotal(total);
        return pageVO;
    }

    private final String contractAddress = "0x6fd4a8ddf3d32f6e9aaf615049c8bed150ce8f68";
    private final Integer groupId = 1;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> confirmAuthorization(ManageForm manageForm) {
        BcosSDK sdk = null;
        try {
            // 1. 验证私钥
//            if (!validatePrivateKey(manageForm.getPrivatekey())) {
//                return ResultVOUtil.fail();
//            }

            // 2. 初始化区块链 SDK
            sdk = BcosSDK.build("src/main/resources/config-example.toml");
            Client client = sdk.getClient(this.groupId); // 使用硬编码的群组ID
            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
            AssembleTransactionProcessor processor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                    client, keyPair, "src/main/resources/abi/", ""
            );

            // 3. 调用区块链查询接口
            List<Object> params = new ArrayList<>();
            params.add(manageForm.getRecordid().toString());
            TransactionResponse response = processor.sendTransactionAndGetResponseByContractLoader(
                    "Record", this.contractAddress, "getRecord", params // 使用硬编码的合约地址
            );

            // 4. 解析区块链返回数据
            List<Object> returnValues = response.getReturnObject();
            if (returnValues == null||returnValues.size() !=2 ) {
                throw new BlockchainException("区块链返回数据格式错误");
            }


            String description = (String) returnValues.get(0);
            String remark = (String) returnValues.get(1);


            String sprivatekey=manageForm.getPrivatekey();
            Teacher teacher=this.teacherMapper.selectById(manageForm.getTeacherid());
            String tpublickey=teacher.getPublickey();

            String description_decrypt=RSAUtil.decrypt(description, sprivatekey);
            String remark_decrypt=RSAUtil.decrypt(remark, sprivatekey);

            String description_enbyt=RSAUtil.encrypt(description_decrypt.getBytes(),tpublickey);
            String remark_enbyt=RSAUtil.encrypt(remark_decrypt.getBytes(),tpublickey);
            // 5. 更新数据库
            Authorize authorize = authorizeMapper.selectById(manageForm.getAuthorizeid());
            if (authorize == null) {
                return ResultVOUtil.fail();
            }
            authorize.setDescriptionEnbyt(description_enbyt);
            authorize.setRemarkEnbyt(remark_enbyt);
            authorize.setState(1);
            authorizeMapper.updateById(authorize);

            return ResultVOUtil.success("授权成功");

        } catch (Exception e) {
            throw new RuntimeException("操作失败: " + e.getMessage());
        }
    }

//    // 私钥验证逻辑（示例）
//    private boolean validatePrivateKey(String privateKey) {
//        return privateKey != null && privateKey.matches("^[a-fA-F0-9]{64}$");
//    }


    @Override
    public DecryptVO tdecrypt(TDecryptForm tdecryptForm)throws Exception{
        String privateKey=tdecryptForm.getPrivatekey();
        Integer recordid=tdecryptForm.getRecordid();
       Integer teacherid=tdecryptForm.getTeacherid();

        // 1. 根据 recordid 和 teacherid 查询 authorize 表
        QueryWrapper<Authorize> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("record_id", recordid)
                .eq("teacher_id", teacherid)
                .select("description_enbyt", "remark_enbyt"); // 明确查询需要的字段

        Authorize authorize = authorizeMapper.selectOne(queryWrapper);

        // 2. 检查查询结果是否存在
        if (authorize == null) {
            throw new RuntimeException("未找到授权记录");
        }

        // 3. 提取加密数据
        String encryptedDescription = authorize.getDescriptionEnbyt();
        String encryptedRemark = authorize.getRemarkEnbyt();

        //封装返回结果
            DecryptVO decryptVO = new DecryptVO();
            decryptVO.setDescription(RSAUtil.decrypt(encryptedDescription, privateKey)); // 替换为解密后的数据
            decryptVO.setRemark(RSAUtil.decrypt(encryptedRemark, privateKey));           // 替换为解密后的数据

            return decryptVO;


    }

    }










