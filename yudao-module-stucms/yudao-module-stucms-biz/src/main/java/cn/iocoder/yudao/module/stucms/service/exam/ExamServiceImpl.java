package cn.iocoder.yudao.module.stucms.service.exam;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.*;
import cn.iocoder.yudao.module.stucms.convert.exam.ExamConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.exam.ExamDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.exam.ExamMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.EXAM_NOT_EXISTS;

/**
 * 考试 Service 实现类
 *
 * @author hua
 */
@Service
@Validated
public class ExamServiceImpl implements ExamService {

    @Resource
    private ExamMapper examMapper;

    @Override
    public Long createExam(ExamCreateReqVO createReqVO) {
        // 插入
        ExamDO exam = ExamConvert.INSTANCE.convert(createReqVO);
        examMapper.insert(exam);
        // 返回
        return exam.getExamId();
    }

    @Override
    public void updateExam(ExamUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateExamExists(updateReqVO.getExamId());
        // 更新
        ExamDO updateObj = ExamConvert.INSTANCE.convert(updateReqVO);
        examMapper.updateById(updateObj);
    }

    @Override
    public void deleteExam(Long id) {
        // 校验存在
        this.validateExamExists(id);
        // 删除
        examMapper.deleteById(id);
    }

    @Override
    public void deleteExam(Collection<Long> ids) {
        // 校验存在
        if (CollUtil.isEmpty(ids)) {
            throw exception(EXAM_NOT_EXISTS);
        }
        ids.forEach(this::validateExamExists);
        // 删除
        examMapper.deleteBatchIds(ids);
        // TODO 删除考试后,同时删除成绩表中的考试
    }

    private void validateExamExists(Long id) {
        if (examMapper.selectById(id) == null) {
            throw exception(EXAM_NOT_EXISTS);
        }
    }

    @Override
    public ExamDO getExam(Long id) {
        return examMapper.selectById(id);
    }

    @Override
    public List<ExamDO> getExamList(Collection<Long> ids) {
        return examMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ExamDO> getExamPage(ExamPageReqVO pageReqVO) {
        return examMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ExamDO> getExamList(ExamExportReqVO exportReqVO) {
        return examMapper.selectList(exportReqVO);
    }

    @Override
    public List<ExamSimpleVO> getSimpleExamList() {
        List<ExamDO> list = this.examMapper.selectList();
        return ExamConvert.INSTANCE.convertSimpleList(list);
    }
}
