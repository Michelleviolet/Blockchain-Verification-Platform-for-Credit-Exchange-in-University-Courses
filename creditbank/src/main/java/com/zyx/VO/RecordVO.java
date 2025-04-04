package com.zyx.VO;


import lombok.Data;

//查看档案的视图
@Data
public class RecordVO {
    private Integer id;//档案的id
    private String createtime;
    private String name;
    private Long phone;
    private String school;
    private String stuid;
    private String course;
    //弹窗需要展示的数据
    private String tschool;
    private String description;
    private String remark;
    private Integer affirm;


}
