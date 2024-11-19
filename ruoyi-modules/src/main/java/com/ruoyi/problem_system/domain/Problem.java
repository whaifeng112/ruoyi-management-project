package com.ruoyi.problem_system.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 提问于回答对象 problem
 *
 * @author ruoyi
 * @date 2023-11-27
 */
public class Problem extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 问题主键 */
    @ApiModelProperty(value = "问题主键",name = "problemId",dataType="Long")
    private Long problemId;

    /** 提问人ID */
    @ApiModelProperty(value = "提问人ID",dataType="Long")
    @Excel(name = "提问人ID")
    private Long sysUserId;

    /** 提问人ID */
    @ApiModelProperty(value = "提问人姓名",dataType="String")
    @Excel(name = "提问人姓名")
    private String userName;

    /** 提问时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提问时间",dataType="datetime")
    @Excel(name = "提问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    /** 删除状态（0未删除，1为已经删除） */
    @ApiModelProperty(value = "删除状态（0未删除，1为已经删除）",dataType="Integer")
    private Integer problemStatu;

    /** 开发回答状态（0开发，1关闭） */
    @ApiModelProperty(value = "开放回答状态（0开发，1关闭）",dataType="Integer")
    private Integer openStatu;

    /** 问题文本 */
    @ApiModelProperty(value = "问题文本",dataType="String")
    @Excel(name = "问题文本")

    private String problemContent;

    public String getUserName() {
        return userName;
    }

    public Integer getOpenStatu() {
        return openStatu;
    }

    public void setOpenStatu(Integer openStatu) {
        this.openStatu = openStatu;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProblemId(Long problemId)
    {
        this.problemId = problemId;
    }

    public Long getProblemId()
    {
        return problemId;
    }
    public void setSysUserId(Long sysUserId)
    {
        this.sysUserId = sysUserId;
    }

    public Long getSysUserId()
    {
        return sysUserId;
    }
    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    public Date getCreationTime()
    {
        return creationTime;
    }
    public void setProblemStatu(Integer problemStatu)
    {
        this.problemStatu = problemStatu;
    }

    public Integer getProblemStatu()
    {
        return problemStatu;
    }
    public void setProblemContent(String problemContent)
    {
        this.problemContent = problemContent;
    }

    public String getProblemContent()
    {
        return problemContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("problemId", getProblemId())
            .append("sysUserId", getSysUserId())
            .append("creationTime", getCreationTime())
            .append("problemStatu", getProblemStatu())
            .append("problemContent", getProblemContent())
            .toString();
    }
}
