package cn.iocoder.yudao.module.stucms.enums.student;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LiuShouEnum {
    // 留守
    YES(0)
    // 不是留守
    , NO(1);
    private final Integer liushou;
}
