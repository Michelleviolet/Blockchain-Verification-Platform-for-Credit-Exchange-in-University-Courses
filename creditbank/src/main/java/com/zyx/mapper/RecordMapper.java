package com.zyx.mapper;

import com.zyx.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2025-03-14
 */
@Mapper // 关键注解
public interface RecordMapper extends BaseMapper<Record> {

}
