package com.ruoyi.problem_system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.problem_system.mapper.AnswerMapper;
import com.ruoyi.problem_system.domain.Answer;
import com.ruoyi.problem_system.service.IAnswerService;

import javax.xml.crypto.Data;

/**
 * 回答Service业务层处理
 *
 * @author ruoyi
 * @date 2023-11-27
 */
@Service
public class AnswerServiceImpl implements IAnswerService
{
    @Autowired
    private AnswerMapper answerMapper;

    /**
     * 查询回答
     *
     * @param answerId 回答主键
     * @return 回答
     */
    @Override
    public Answer selectAnswerByAnswerId(Long answerId)
    {
        return answerMapper.selectAnswerByAnswerId(answerId);
    }

    /**
     * 查询回答列表
     *
     * @param answer 回答
     * @return 回答
     */
    @Override
    public List<Answer> selectAnswerList(Answer answer)
    {
        return answerMapper.selectAnswerList(answer);
    }

    /**
     * 新增回答
     *
     * @param answer 回答
     * @return 结果
     */
    @Override
    public int insertAnswer(Answer answer)
    {
        answer.setCreationTime(new Date());
        return answerMapper.insertAnswer(answer);
    }

    /**
     * 修改回答
     *
     * @param answer 回答
     * @return 结果
     */
    @Override
    public int updateAnswer(Answer answer)
    {
        return answerMapper.updateAnswer(answer);
    }

    /**
     * 批量删除回答
     *
     * @param answerIds 需要删除的回答主键
     * @return 结果
     */
    @Override
    public int deleteAnswerByAnswerIds(Long[] answerIds)
    {
        return answerMapper.deleteAnswerByAnswerIds(answerIds);
    }

    /**
     * 删除回答信息
     *
     * @param answerId 回答主键
     * @return 结果
     */
    @Override
    public int deleteAnswerByAnswerId(Long answerId)
    {
        return answerMapper.deleteAnswerByAnswerId(answerId);
    }
}
