package com.zyx.VO;

import lombok.Data;


@Data
public class ViewRecordVO {
    private Integer id;             // 档案ID
    private String createtime;      // 创建时间
    private String name;            // 学生姓名
    private Long phone;           // 学生电话
    private String school;          // 学生学校
    private String stuid;           // 学生学号
    private String course;          // 课程名称
    private String tschool;         // 教师学校
    private Integer affirm;         // 确认状态
    private String description;     // 描述
    private String remark;          // 备注
    public Integer state;
}
