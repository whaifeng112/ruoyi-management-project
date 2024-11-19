package com.ruoyi.problem_system.mapper;

import java.util.List;
import com.ruoyi.problem_system.domain.Problem;

/**
 * 提问于回答Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public interface ProblemMapper 
{
    /**
     * 查询提问于回答
     * 
     * @param problemId 提问于回答主键
     * @return 提问于回答
     */
    public Problem selectProblemByProblemId(Long problemId);

    /**
     * 查询提问于回答列表
     * 
     * @param problem 提问于回答
     * @return 提问于回答集合
     */
    public List<Problem> selectProblemList(Problem problem);

    /**
     * 新增提问于回答
     * 
     * @param problem 提问于回答
     * @return 结果
     */
    public int insertProblem(Problem problem);

    /**
     * 修改提问于回答
     * 
     * @param problem 提问于回答
     * @return 结果
     */
    public int updateProblem(Problem problem);

    /**
     * 删除提问于回答
     * 
     * @param problemId 提问于回答主键
     * @return 结果
     */
    public int deleteProblemByProblemId(Long problemId);

    /**
     * 批量删除提问于回答
     * 
     * @param problemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProblemByProblemIds(Long[] problemIds);
}
