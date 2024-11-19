package com.ruoyi.problem_system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.problem_system.mapper.ProblemMapper;
import com.ruoyi.problem_system.domain.Problem;
import com.ruoyi.problem_system.service.IProblemService;

/**
 * 提问于回答Service业务层处理
 *
 * @author ruoyi
 * @date 2023-11-27
 */
@Service
public class ProblemServiceImpl implements IProblemService
{
    @Autowired
    private ProblemMapper problemMapper;

    /**
     * 查询提问于回答
     *
     * @param problemId 提问于回答主键
     * @return 提问于回答
     */
    @Override
    public Problem selectProblemByProblemId(Long problemId)
    {
        return problemMapper.selectProblemByProblemId(problemId);
    }

    /**
     * 查询提问于回答列表
     *
     * @param problem 提问于回答
     * @return 提问于回答
     */
    @Override
    public List<Problem> selectProblemList(Problem problem)
    {
        return problemMapper.selectProblemList(problem);
    }

    /**
     * 新增提问于回答
     *
     * @param problem 提问于回答
     * @return 结果
     */
    @Override
    public int insertProblem(Problem problem)
    {
        problem.setCreationTime(new Date());
        return problemMapper.insertProblem(problem);
    }

    /**
     * 修改提问于回答
     *
     * @param problem 提问于回答
     * @return 结果
     */
    @Override
    public int updateProblem(Problem problem)
    {
        return problemMapper.updateProblem(problem);
    }

    /**
     * 批量删除提问于回答
     *
     * @param problemIds 需要删除的提问于回答主键
     * @return 结果
     */
    @Override
    public int deleteProblemByProblemIds(Long[] problemIds)
    {
        return problemMapper.deleteProblemByProblemIds(problemIds);
    }

    /**
     * 删除提问于回答信息
     *
     * @param problemId 提问于回答主键
     * @return 结果
     */
    @Override
    public int deleteProblemByProblemId(Long problemId)
    {
        return problemMapper.deleteProblemByProblemId(problemId);
    }
}
