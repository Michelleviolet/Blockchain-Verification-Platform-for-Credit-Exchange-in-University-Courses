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
    public class Record implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 老师id
     */
      private Integer teacherId;

      /**
     * 学生id
     */
      private Integer studentId;

      /**
     * 档案创建时间
     */
      private String createtime;

      /**
     * 学分档案（学生公钥加密）
     */
      private String descriptionEnbys;

      /**
     * 备注（学生公钥加密）
     */
      private String remarkEnbys;

      /**
     * 是否确认 （0未确认，1已确认）
     */
      private Integer affirm;


}
