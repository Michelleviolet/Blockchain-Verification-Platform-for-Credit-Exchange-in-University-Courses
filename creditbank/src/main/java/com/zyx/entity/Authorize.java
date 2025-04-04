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
    public class Authorize implements Serializable {

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
     * 记录id
     */
      private Integer recordId;

      /**
     * 学分档案（老师公钥加密）
     */
      private String descriptionEnbyt;

      /**
     * 备注（老师公钥加密）
     */
      private String remarkEnbyt;

      /**
     * 是否确认 （0未确认，1已确认）
     */
      private Integer state;

      /**
     * 档案创建时间
     */
      private String createtime;


}
