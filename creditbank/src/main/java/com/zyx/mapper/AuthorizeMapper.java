package com.zyx.mapper;

import com.zyx.VO.ManagerVO;
import com.zyx.entity.Authorize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
@Mapper // 关键注解
public interface AuthorizeMapper extends BaseMapper<Authorize> {
    @Select({
            "SELECT ",
            "  a.id AS authorizeid, ",
            "  a.record_id AS recordid, ",
            "  a.createtime AS createtime, ",
            "  r.student_id AS studentid, ",
            "  a.teacher_id AS teacherid, ",
            "  t.course AS course, ",
            "  t.school AS tschool, ",
            "  a.state AS state ",
            "FROM authorize a ",
            "JOIN record r ON a.record_id = r.id ",
            "JOIN teacher t ON a.teacher_id = t.id ",
            "WHERE r.student_id = #{studentId} ",
            "LIMIT #{pageSize} OFFSET #{offset}"
    })
    @Results({
            @Result(property = "authorizeid", column = "authorizeid"),
            @Result(property = "recordid", column = "recordid"),
            @Result(property = "createtime", column = "createtime"),
            @Result(property = "studentid", column = "studentid"),
            @Result(property = "teacherid", column = "teacherid"),
            @Result(property = "course", column = "course"),
            @Result(property = "tschool", column = "tschool"),
            @Result(property = "state", column = "state")
    })
    List<ManagerVO> selectManagerList(
            @Param("studentId") Integer studentId,
            @Param("pageSize") Integer pageSize,
            @Param("offset") Long offset
    );

    // 获取总数
    @Select({
            "SELECT COUNT(*) ",
            "FROM authorize a ",
            "JOIN record r ON a.record_id = r.id ",
            "WHERE r.student_id = #{studentId}"
    })
    Long countManagerList(@Param("studentId") Integer studentId);
}
