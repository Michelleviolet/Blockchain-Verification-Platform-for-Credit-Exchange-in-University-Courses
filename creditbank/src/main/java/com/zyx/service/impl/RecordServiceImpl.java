package com.zyx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyx.VO.DecryptVO;
import com.zyx.VO.PageVO;
import com.zyx.VO.RecordVO;
import com.zyx.VO.ViewRecordVO;
import com.zyx.entity.Authorize;
import com.zyx.entity.Record;
import com.zyx.entity.Student;
import com.zyx.entity.Teacher;
import com.zyx.form.CreateForm;
import com.zyx.form.DecryptForm;
import com.zyx.mapper.AuthorizeMapper;
import com.zyx.mapper.RecordMapper;
import com.zyx.mapper.StudentMapper;
import com.zyx.mapper.TeacherMapper;
import com.zyx.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyx.util.CommonUtil;
import com.zyx.util.RSAUtil;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

import org.fisco.bcos.sdk.client.Client;  // ✅ 正确导入
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;

import java.util.ArrayList;
import java.util.List;
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
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AuthorizeMapper authorizeMapper;
    @Override
    public Boolean create(CreateForm createForm) throws Exception {
        //1.判断学生用户是否存在；手机号存在；手机号姓名是否一致
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("phone",createForm.getPhone());
        Student student=this.studentMapper.selectOne(queryWrapper);
        if(student==null){
            return false;
        }else{
            if(!student.getName().equals(createForm.getName())){
                return false;
            }else {
                //2.用户存在，通过手机号查询id，新增数据
                Record record = new Record();
                record.setStudentId(student.getId());
                record.setTeacherId(createForm.getTeacherid());
                record.setDescriptionEnbys(RSAUtil.encrypt((createForm.getDescription()).getBytes(), student.getPublickey()));
                record.setRemarkEnbys(RSAUtil.encrypt((createForm.getRemark()).getBytes(), student.getPublickey()));
                record.setAffirm(0);
                record.setCreatetime(CommonUtil.createDate());

                int insert = this.recordMapper.insert(record);
                if (insert != 1) return false;
            }

        }return true;

    }

    @Override
    public PageVO studentList(Integer studentId, Integer page, Integer size){
        Page<Record> recordPage =new Page<>(page,size);

        QueryWrapper<Record> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("student_id",studentId);
        Page<Record> resultPage = this.recordMapper.selectPage(recordPage, queryWrapper);
        List<Record> recordList = resultPage.getRecords();
        List<RecordVO> recordVOList=new ArrayList<>();
        for(Record record :recordList){
            RecordVO recordVO=new RecordVO();
            Student student=this.studentMapper.selectById(record.getStudentId());
            Teacher teacher=this.teacherMapper.selectById(record.getTeacherId());
            recordVO.setId(record.getId());
            recordVO.setCreatetime(record.getCreatetime());
            recordVO.setName(student.getName());
            recordVO.setPhone(student.getPhone());
            recordVO.setSchool(student.getSchool());
            recordVO.setStuid(student.getStuId());
            recordVO.setCourse(teacher.getCourse());
            //弹窗数据
            recordVO.setTschool(teacher.getSchool());
            recordVO.setAffirm(record.getAffirm());
            recordVO.setDescription(record.getDescriptionEnbys());
            recordVO.setRemark(record.getRemarkEnbys());
            recordVOList.add(recordVO);
        }
        PageVO pageVO=new PageVO();
        pageVO.setData(recordVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }


    // RecordServiceImpl.java
    @Override
    public PageVO teacherList( Integer teacherId,Integer page, Integer size) {
        // 分页查询 record 表
        Page<Record> recordPage = new Page<>(page, size);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        Page<Record> resultPage = this.recordMapper.selectPage(recordPage, queryWrapper);
        List<Record> recordList = resultPage.getRecords();

        // 提取所有 student_id 和 teacher_id
        Set<Integer> studentIds = recordList.stream()
                .map(Record::getStudentId)
                .collect(Collectors.toSet());
        Set<Integer> teacherIds = recordList.stream()
                .map(Record::getTeacherId)
                .collect(Collectors.toSet());

        // 批量查询 student 和 teacher 信息
        Map<Integer, Student> studentMap = studentMapper.selectBatchIds(studentIds).stream()
                .collect(Collectors.toMap(Student::getId, student -> student));
        Map<Integer, Teacher> teacherMap = teacherMapper.selectBatchIds(teacherIds).stream()
                .collect(Collectors.toMap(Teacher::getId, teacher -> teacher));

        // 获取当前页所有 record 的 id
        Set<Integer> recordIds = recordList.stream()
                .map(Record::getId)
                .collect(Collectors.toSet());

        // 查询 authorize 表中存在的 record_id 和 teacher_id 组合
        QueryWrapper<Authorize> authorizeQuery = new QueryWrapper<>();
        authorizeQuery.in("record_id", recordIds)
                .eq("teacher_id", teacherId)
                .select("record_id", "state");
        List<Authorize> authorizeList = authorizeMapper.selectList(authorizeQuery);

        //构建 Map<id, state>
        Map<Integer, Integer> authorizeStateMap = new HashMap<>();
        for (Authorize authorize : authorizeList) {
            authorizeStateMap.put(authorize.getRecordId(), authorize.getState());
        }

        // 转换为 ViewRecordVO 列表
        List<ViewRecordVO> viewRecordVOList = recordList.stream().map(record -> {
            ViewRecordVO viewRecordVO = new ViewRecordVO();
            Student student = studentMap.get(record.getStudentId());
            Teacher teacher = teacherMap.get(record.getTeacherId());

            // 填充基础字段
            viewRecordVO.setId(record.getId());
            viewRecordVO.setCreatetime(record.getCreatetime());

            // 填充学生信息（处理空值）
            if (student != null) {
                viewRecordVO.setName(student.getName());
                viewRecordVO.setPhone(student.getPhone());
                viewRecordVO.setSchool(student.getSchool());
                viewRecordVO.setStuid(student.getStuId());
            } else {
                viewRecordVO.setName("未知学生");
                viewRecordVO.setPhone(Long.valueOf("N/A"));
                viewRecordVO.setSchool("未知学校");
                viewRecordVO.setStuid("N/A");
            }

            // 填充教师信息（处理空值）
            if (teacher != null) {
                viewRecordVO.setCourse(teacher.getCourse());
                viewRecordVO.setTschool(teacher.getSchool());
            } else {
                viewRecordVO.setCourse("未知课程");
                viewRecordVO.setTschool("未知学校");
            }

            // 填充其他字段
            viewRecordVO.setAffirm(record.getAffirm());
            viewRecordVO.setDescription(record.getDescriptionEnbys());
            viewRecordVO.setRemark(record.getRemarkEnbys());

            // 动态设置 state：存在授权记录为 0，否则为 -1，已授权则为1
            if (authorizeStateMap.containsKey(record.getId())) {
                Integer state = authorizeStateMap.get(record.getId());
                viewRecordVO.setState(state); // 直接使用查询到的 state（0 或 1）
            } else {
                viewRecordVO.setState(-1); // 无记录设为 -1
            }

            return viewRecordVO;
        }).collect(Collectors.toList());

        // 构造分页结果
        PageVO pageVO = new PageVO();
        pageVO.setData(viewRecordVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public DecryptVO decrypt(DecryptForm decryptForm)throws Exception{
        String privateKey=decryptForm.getPrivatekey();
        DecryptVO decryptVO=new DecryptVO();
         decryptVO.setDescription(RSAUtil.decrypt(decryptForm.getDescription(), privateKey));
         decryptVO.setRemark(RSAUtil.decrypt(decryptForm.getRemark(),privateKey));
         return decryptVO;


    }

    @Override
    public void updateAffirmStatus(Integer id, Integer affirm) {
        Record record = recordMapper.selectById(id);
        if (record == null) {
            throw new RuntimeException("档案不存在");
        }
        record.setAffirm(affirm);
        recordMapper.updateById(record);
    }


    private final BcosSDK bcosSDK;

    public RecordServiceImpl() {
        // 从类路径或指定路径加载配置文件
        String configFile = "src/main/resources/config-example.toml";
        this.bcosSDK = BcosSDK.build(configFile);
    }

    @Override
    public TransactionResponse uploadToBlockchain(String recordId, String description, String remark) throws Exception {
        // 初始化 Client 和 TransactionProcessor
        Client client = bcosSDK.getClient(1); // 群组 ID 硬编码或从配置读取
        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
        AssembleTransactionProcessor processor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                client, keyPair, "src/main/resources/abi/", ""
        );

        // 构造交易参数
        List<Object> params = new ArrayList<>();
        params.add(recordId);
        params.add(description);
        params.add(remark);

        // 调用合约
        return processor.sendTransactionAndGetResponseByContractLoader(
                "Record",
                "0x6fd4a8ddf3d32f6e9aaf615049c8bed150ce8f68",
                "addRecord",
                params
        );
    }


}
