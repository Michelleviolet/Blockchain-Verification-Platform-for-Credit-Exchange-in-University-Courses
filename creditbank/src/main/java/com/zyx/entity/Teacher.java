package com.zyx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Teacher implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 姓名
     */
      private String name;

      /**
     * 电话/登录账号
     */
      private Long phone;

      /**
     * 登录密码
     */
      private String password;

      /**
     * 院校
     */
      private String school;

      /**
     * 课程
     */
      private String course;

      /**
     * 公钥
     */
      private String publickey;


}
