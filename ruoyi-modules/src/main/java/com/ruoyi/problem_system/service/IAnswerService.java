package com.ruoyi.problem_system.service;

import java.util.List;
import com.ruoyi.problem_system.domain.Answer;

/**
 * 回答Service接口
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public interface IAnswerService 
{
    /**
     * 查询回答
     * 
     * @param answerId 回答主键
     * @return 回答
     */
    public Answer selectAnswerByAnswerId(Long answerId);

    /**
     * 查询回答列表
     * 
     * @param answer 回答
     * @return 回答集合
     */
    public List<Answer> selectAnswerList(Answer answer);

    /**
     * 新增回答
     * 
     * @param answer 回答
     * @return 结果
     */
    public int insertAnswer(Answer answer);

    /**
     * 修改回答
     * 
     * @param answer 回答
     * @return 结果
     */
    public int updateAnswer(Answer answer);

    /**
     * 批量删除回答
     * 
     * @param answerIds 需要删除的回答主键集合
     * @return 结果
     */
    public int deleteAnswerByAnswerIds(Long[] answerIds);

    /**
     * 删除回答信息
     * 
     * @param answerId 回答主键
     * @return 结果
     */
    public int deleteAnswerByAnswerId(Long answerId);
}
