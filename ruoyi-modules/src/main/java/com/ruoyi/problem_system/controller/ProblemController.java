package com.ruoyi.problem_system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.problem_system.domain.Problem;
import com.ruoyi.problem_system.service.IProblemService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 提问于回答Controller
 *
 * @author ruoyi
 * @date 2023-11-27
 */
@RestController
@RequestMapping("/problemSystem/problem_answer")
@Api( value = "/problemSystem/problem_answer",tags = "问题管理")
public class ProblemController extends BaseController
{
    @Autowired
    private IProblemService problemService;

    /**
     * 查询提问于回答列表
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:problem_answer:list')")Admin-Token
    @ApiOperation(value = "list",httpMethod = "GET",notes = "查询问题列表" )
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户权限token",dataType = "String", paramType = "HEADER"),
    })
    public TableDataInfo list(
            @ApiParam(name = "problem",value = "问题",required = true) Problem problem)
    {
        startPage();
        List<Problem> list = problemService.selectProblemList(problem);
        return getDataTable(list);
    }

    /**
     * 导出提问于回答列表
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:problem_answer:export')")
    @Log(title = "提问于回答", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "export",httpMethod = "POST",notes = "导出问题列表" )
    public void export(HttpServletResponse response,@ApiParam(name = "problem",value = "问题",required = true) Problem problem)
    {
        List<Problem> list = problemService.selectProblemList(problem);
        ExcelUtil<Problem> util = new ExcelUtil<Problem>(Problem.class);
        util.exportExcel(response, list, "提问于回答数据");
    }

    /**
     * 获取提问于回答详细信息
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:problem_answer:query')")
    @GetMapping(value = "/{problemId}")
    @ApiOperation(value = "getInfo",httpMethod = "GET",notes = "通过problemId查询特定的problem" )
    public AjaxResult getInfo(
            @ApiParam(name = "problemId",value = "问题Id",required = true)
            @PathVariable("problemId") Long problemId)
    {
        return success(problemService.selectProblemByProblemId(problemId));
    }

    /**
     * 新增提问于回答
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:problem_answer:add')")
    @Log(title = "提问于回答", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "add",httpMethod = "POST",notes = "添加一个problem" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemContent", value = "问题文本", required = true, dataType = "String", paramType = "HEADER"),
            @ApiImplicitParam(name = "problemId", value = "问题id", dataType = "Long", paramType = "HEADER"),
            @ApiImplicitParam(name = "creationTime", value = "提问时间", dataType = "Date", paramType = "HEADER"),
            @ApiImplicitParam(name = "problemStatu", value = "问题删除状态（0未删除，1为已经删除）",dataType = "Integer", paramType = "HEADER"),
            @ApiImplicitParam(name = "openStatu", value = "开放回答状态（0开发，1关闭）",dataType = "Integer", paramType = "HEADER"),
    })
    public AjaxResult add(@RequestBody Problem problem)
    {
        return toAjax(problemService.insertProblem(problem));
    }

    /**
     * 修改提问于回答
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:problem_answer:edit')")
    @Log(title = "提问于回答", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "edit",httpMethod = "PUT",notes = "根据problem中的problemId，修改其他内容" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemContent", value = "问题文本", required = true, dataType = "String", paramType = "HEADER"),
            @ApiImplicitParam(name = "problemId", required = true, value = "问题id", dataType = "Long", paramType = "HEADER"),
            @ApiImplicitParam(name = "creationTime", value = "提问时间", dataType = "Date", paramType = "HEADER"),
            @ApiImplicitParam(name = "problemStatu", value = "问题删除状态（0未删除，1为已经删除）",dataType = "Integer", paramType = "HEADER"),
            @ApiImplicitParam(name = "openStatu", value = "开放回答状态（0开发，1关闭）",dataType = "Integer", paramType = "HEADER"),
    })
    public AjaxResult edit(@RequestBody Problem problem)
    {
        return toAjax(problemService.updateProblem(problem));
    }

    /**
     * 删除提问于回答
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:problem_answer:remove')")
    @Log(title = "提问于回答", businessType = BusinessType.DELETE)
	@DeleteMapping("/{problemIds}")
    @ApiOperation(value = "remove",httpMethod = "DELETE",notes = "更具problemId删除problem" )
    public AjaxResult remove(
            @ApiParam(name = "problemId",value = "问题Id",required = true)
            @PathVariable Long[] problemIds)
    {
        return toAjax(problemService.deleteProblemByProblemIds(problemIds));
    }
}
