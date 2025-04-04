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
    public class Student implements Serializable {

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
     * 电话/账号
     */
      private Long phone;

      /**
     * 登陆密码
     */
      private String password;

      /**
     * 学校
     */
      private String school;

      /**
     * 学号
     */
      private String stuId;

      /**
     * 公钥
     */
      private String publickey;


}
