package cn.iocoder.yudao.module.stucms.service.exam;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamUpdateReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.exam.ExamDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 考试 Service 接口
 *
 * @author hua
 */
public interface ExamService {

    /**
     * 创建考试
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExam(@Valid ExamCreateReqVO createReqVO);

    /**
     * 更新考试
     *
     * @param updateReqVO 更新信息
     */
    void updateExam(@Valid ExamUpdateReqVO updateReqVO);

    /**
    * 批量删除考试
    *
    * @param ids 编号
    */
    void deleteExam(Collection<Long> ids);

    /**
     * 删除考试
     *
     * @param id 编号
     */
    void deleteExam(Long id);

    /**
     * 获得考试
     *
     * @param id 编号
     * @return 考试
     */
    ExamDO getExam(Long id);

    /**
     * 获得考试列表
     *
     * @param ids 编号
     * @return 考试列表
     */
    List<ExamDO> getExamList(Collection<Long> ids);

    /**
     * 获得考试分页
     *
     * @param pageReqVO 分页查询
     * @return 考试分页
     */
    PageResult<ExamDO> getExamPage(ExamPageReqVO pageReqVO);

    /**
     * 获得考试列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 考试列表
     */
    List<ExamDO> getExamList(ExamExportReqVO exportReqVO);

}
