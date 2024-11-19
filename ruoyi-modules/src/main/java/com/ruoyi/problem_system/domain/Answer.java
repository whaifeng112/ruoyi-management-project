package com.ruoyi.problem_system.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 回答对象 answer
 *
 * @author ruoyi
 * @date 2023-11-27
 */
@ApiModel(value = "answer对象",description="用来模拟用户对问题的回答")
public class Answer extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 答案主键 */
    @ApiModelProperty(value = "答案的Id",name = "answerId",dataType="Long")
    private Long answerId;

    /** 回答人ID */
    @ApiModelProperty(value = "回答人ID",dataType="Long")
    @Excel(name = "回答人ID")
    private Long sysUserId;

    /** 题目ID */
    @ApiModelProperty(value = "题目ID",dataType="Long")
    @Excel(name = "题目ID")
    private Long problemId;

    /** 答题人姓名 */
    @ApiModelProperty(value = "答题人姓名",dataType="String")
    @Excel(name = "答题人姓名")
    private String answerName;

    /** 回答的时间 */
    @ApiModelProperty(value = "回答时间",dataType="datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    /** 答案的状态（0为普通答案，1为优质答案） */
    @ApiModelProperty(value = "答案的状态（0为普通答案，1为优质答案）",dataType="Integer")
    @Excel(name = "答案的状态", readConverterExp = "0=为普通答案，1为优质答案")
    private Integer answerStatu;

    /** 答案文本 */
    @ApiModelProperty(value = "答案文本",dataType="String")
    @Excel(name = "答案文本")
    private String answerContent;

    public String getAnswerName() {
        return answerName;
    }

    public void setAnswerName(String answerName) {
        this.answerName = answerName;
    }

    public void setAnswerId(Long answerId)
    {
        this.answerId = answerId;
    }

    public Long getAnswerId()
    {
        return answerId;
    }
    public void setSysUserId(Long sysUserId)
    {
        this.sysUserId = sysUserId;
    }

    public Long getSysUserId()
    {
        return sysUserId;
    }
    public void setProblemId(Long problemId)
    {
        this.problemId = problemId;
    }

    public Long getProblemId()
    {
        return problemId;
    }
    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    public Date getCreationTime()
    {
        return creationTime;
    }
    public void setAnswerStatu(Integer answerStatu)
    {
        this.answerStatu = answerStatu;
    }

    public Integer getAnswerStatu()
    {
        return answerStatu;
    }
    public void setAnswerContent(String answerContent)
    {
        this.answerContent = answerContent;
    }

    public String getAnswerContent()
    {
        return answerContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("answerId", getAnswerId())
            .append("sysUserId", getSysUserId())
            .append("problemId", getProblemId())
            .append("creationTime", getCreationTime())
            .append("answerStatu", getAnswerStatu())
            .append("answerContent", getAnswerContent())
            .toString();
    }
}
