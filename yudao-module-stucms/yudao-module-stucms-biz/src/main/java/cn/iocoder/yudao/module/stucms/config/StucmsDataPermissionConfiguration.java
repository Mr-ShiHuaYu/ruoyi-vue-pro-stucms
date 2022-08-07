package cn.iocoder.yudao.module.stucms.config;

import cn.iocoder.yudao.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * stucms 模块的数据权限 Configuration
 */
@Configuration(proxyBeanMethods = false)
public class StucmsDataPermissionConfiguration {
    @Bean
    public DeptDataPermissionRuleCustomizer stucmsDeptDataPermissionRuleCustomizer() {
        return rule -> {
            // 基于部门的数据权限,拼接的sql: where dept_id in (xx)
            rule.addDeptColumn(StudentDO.class, "dept_id");
            // 基于用户的数据权限,拼接的sql: where 自定义列名(默认user_id) = 登录启用的ID
            // rule.addUserColumn(StudentDO.class, "user_id");
        };
    }
}
