package com.zyx.VO;

import lombok.Data;

@Data
public class ManagerVO {
    private Integer authorizeid;
    private Integer recordid;     // 档案ID（对应 authorize.record_id）
    private String createtime;    // 申请授权时间（对应 authorize.createtime）
    private Integer studentid;    // 学生ID（对应 record.student_id）
    private Integer teacherid;    // 老师ID（对应 authorize.teacher_id）
    private String course;       // 课程名称（对应 teacher.course）
    private String tschool;      // 教师学校（对应 teacher.school）
    private Integer state;       // 授权状态（对应 authorize.state）
}
