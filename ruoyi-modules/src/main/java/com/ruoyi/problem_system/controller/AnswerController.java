package com.ruoyi.problem_system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.*;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ruoyi.problem_system.domain.Answer;
import com.ruoyi.problem_system.service.IAnswerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 回答Controller
 *
 * @author ruoyi
 * @date 2023-11-27
 */
@RestController
@RequestMapping("/problemSystem/answer")
@Api(tags = "答案管理",value = "/problemSystem/answer")
public class AnswerController extends BaseController
{
    @Autowired
    private IAnswerService answerService;

    /**
     * 查询回答列表
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:answer:list')")
    @GetMapping("/list")
    @ApiOperation(value = "list",httpMethod = "GET",notes = "查询回答列表" )
    public TableDataInfo list(
            @ApiParam(value = "根据answer中的属性进行查询", required = true)
            Answer answer)
    {
        startPage();
        List<Answer> list = answerService.selectAnswerList(answer);
        return getDataTable(list);
    }

    /**
     * 导出回答列表
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:answer:export')")
    @Log(title = "回答", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "export",httpMethod = "POST",notes = "导出回答列表" )
    public void export(
            @ApiParam(value = "根据answer中的属性进行查询",name = "answer", required = true)
            HttpServletResponse response, Answer answer)
    {
        List<Answer> list = answerService.selectAnswerList(answer);
        ExcelUtil<Answer> util = new ExcelUtil<Answer>(Answer.class);
        util.exportExcel(response, list, "回答数据");
    }

    /**
     * 获取回答详细信息
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:answer:query')")
    @GetMapping(value = "/{answerId}")
    @ApiOperation(value = "getInfo",httpMethod = "GET",notes = "通过answerId查询特定的answer" )
    public AjaxResult getInfo(
            @ApiParam(value = "根据answerId进行查询",name = "answerId", required = true)
            @PathVariable("answerId") Long answerId)
    {
        return success(answerService.selectAnswerByAnswerId(answerId));
    }

    /**
     * 新增回答
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:answer:add')")
    @Log(title = "回答", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "add",httpMethod = "POST",notes = "添加一个answer" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "answerId", value = "答案id", required = true, dataType = "Long", paramType = "HEADER"),
            @ApiImplicitParam(name = "answerContent", value = "答案内容", required = true, dataType = "String", paramType = "HEADER"),
            @ApiImplicitParam(name = "userId", value = "答题人id",required = true,dataType = "Long", paramType = "HEADER"),
            @ApiImplicitParam(name = "problemStatu", value = "答案状态",dataType = "Integer", paramType = "HEADER"),
    })
    public AjaxResult add(@RequestBody Answer answer)
    {
        return toAjax(answerService.insertAnswer(answer));
    }

    /**
     * 修改回答
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:answer:edit')")
    @Log(title = "回答", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "edit",httpMethod = "PUT",notes = "根据answer中的answerId，修改其他内容" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "answerId", value = "答案id", required = true, dataType = "Long", paramType = "HEADER"),
            @ApiImplicitParam(name = "problemId", value = "问题id", dataType = "Long", paramType = "HEADER"),
            @ApiImplicitParam(name = "answerContent", value = "答案内容", required = true, dataType = "String", paramType = "HEADER"),
            @ApiImplicitParam(name = "userId", value = "答题人id",dataType = "Long", paramType = "HEADER"),
            @ApiImplicitParam(name = "creationTime", value = "答题事件", dataType = "Date", paramType = "HEADER"),
            @ApiImplicitParam(name = "problemStatu", value = "答案状态",dataType = "Integer", paramType = "HEADER"),
    })
    public AjaxResult edit(@RequestBody Answer answer)
    {
        return toAjax(answerService.updateAnswer(answer));
    }

    /**
     * 删除回答
     */
    //@PreAuthorize("@ss.hasPermi('problemSystem:answer:remove')")
    @Log(title = "回答", businessType = BusinessType.DELETE)
	@DeleteMapping("/{answerIds}")
    @ApiOperation(value = "remove",httpMethod = "DELETE",notes = "更具answerId删除answer" )
    public AjaxResult remove(
            @ApiParam(value = "根据answerId进行删除",name = "answerId", required = true)
            @PathVariable Long[] answerIds)
    {
        return toAjax(answerService.deleteAnswerByAnswerIds(answerIds));
    }
}
