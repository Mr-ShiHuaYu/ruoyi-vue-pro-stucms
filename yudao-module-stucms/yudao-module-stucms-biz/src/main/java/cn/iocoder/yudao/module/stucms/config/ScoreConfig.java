package cn.iocoder.yudao.module.stucms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取学生成绩评分标准相关配置
 *
 * @author ysh
 */
@Component
@ConfigurationProperties(prefix = "stucms")
public class ScoreConfig {

    @Value("${youxiu:90}")
    public Integer youxiu;

    @Value("${lianghao:80}")
    public Integer lianghao;

    @Value("${jige:60}")
    public Integer jige;

    public Float getYouxiu() {
        return Float.valueOf(this.youxiu) / 100;
    }

    public Float getLianghao() {
        return Float.valueOf(this.lianghao) / 100;
    }

    public Float getJige() {
        return Float.valueOf(this.jige) / 100;
    }
}
