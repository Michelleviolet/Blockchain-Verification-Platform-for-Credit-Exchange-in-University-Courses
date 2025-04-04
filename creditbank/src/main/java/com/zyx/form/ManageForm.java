package com.zyx.form;

import lombok.Data;

@Data
public class ManageForm {
    private Integer authorizeid; // 授权记录ID
    private String privatekey;  // 私钥

    private Integer recordid;    // 档案ID
    private Integer teacherid;   // 教师ID
}
