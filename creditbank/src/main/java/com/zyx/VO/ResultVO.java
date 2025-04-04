package com.zyx.VO;

import lombok.Data;

@Data
public class ResultVO<T> {
    private Integer code;//状态
    private T data;//数据

}
