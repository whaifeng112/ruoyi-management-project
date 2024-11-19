<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="回答人ID" prop="sysUserId">
        <el-input
          v-model="queryParams.sysUserId"
          placeholder="请输入回答人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提问时间" prop="creationTime">
        <el-date-picker clearable
          v-model="queryParams.creationTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择提问时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="答案的状态" prop="answerStatu">
        <el-input
          v-model="queryParams.answerStatu"
          placeholder="请输入答案的状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['problemSystem:answer:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['problemSystem:answer:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['problemSystem:answer:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['problemSystem:answer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <h1></h1>
    <el-table v-loading="loading" :data="answerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="答案主键" align="center" prop="answerId" />
      <el-table-column label="回答人ID" align="center" prop="sysUserId" />
      <el-table-column label="题目ID" align="center" prop="problemId" />
      <el-table-column label="提问时间" align="center" prop="creationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.creationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="答案的状态" align="center" prop="answerStatu">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.answer_statu" :value="scope.row.answerStatu"/>
        </template>
      </el-table-column>
      <el-table-column label="答案文本" align="center" prop="answerContent" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['problemSystem:answer:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['problemSystem:answer:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改回答对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="回答人ID" prop="sysUserId">
          <el-input v-model="form.sysUserId" placeholder="请输入回答人ID" />
        </el-form-item>
        <el-form-item label="题目ID" prop="problemId">
          <el-input v-model="form.problemId" placeholder="请输入题目ID" />
        </el-form-item>
        <el-form-item label="答案文本">
          <editor v-model="form.answerContent" :min-height="192"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAnswer, getAnswer, delAnswer, addAnswer, updateAnswer } from "@/api/problemSystem/answer";

export default {
  name: "Answer",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 回答表格数据
      answerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sysUserId: null,
        problemId: null,
        creationTime: null,
        answerStatu: null,
        answerContent: null
      },
      problemParams: {
        pageNum: 1,
        pageSize: 10,
        sysUserId: null,
        problemId: null,
        creationTime: null,
        answerStatu: null,
        answerContent: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sysUserId: [
          { required: true, message: "回答人ID不能为空", trigger: "blur" }
        ],
        problemId: [
          { required: true, message: "题目ID不能为空", trigger: "blur" }
        ],
        creationTime: [
          { required: true, message: "提问时间不能为空", trigger: "blur" }
        ],
      },
        problemList : []
    };
  },
  created() {
    this.getProblem()
  },
  methods: {
    //查询题目
    getProblem(){
        this.loading = true;
        this.problemParams.problemId=1;
        listProblem_answer(this.queryParams).then(response => {
        this.problemList = response.rows;
        this.getList();
      });
    },
    /** 查询回答列表 */
    getList() {
      this.loading = true;
      listAnswer(this.queryParams).then(response => {
        this.answerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        answerId: null,
        sysUserId: null,
        problemId: null,
        creationTime: null,
        answerStatu: null,
        answerContent: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.answerId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加回答";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const answerId = row.answerId || this.ids
      getAnswer(answerId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改回答";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.answerId != null) {
            updateAnswer(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAnswer(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const answerIds = row.answerId || this.ids;
      this.$modal.confirm('是否确认删除回答编号为"' + answerIds + '"的数据项？').then(function() {
        return delAnswer(answerIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('problemSystem/answer/export', {
        ...this.queryParams
      }, `answer_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
