package cn.iocoder.yudao.module.stucms.enums.student;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JishuEnum {
    // 寄宿
    JISHU("0")
    // 非寄宿
    , FEIJISHU("1");
    private final String jishu;
}
