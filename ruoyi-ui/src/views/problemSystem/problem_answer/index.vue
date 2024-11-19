<template>
  <div class="app-container">
    <div v-show="openmain">
      <el-form
        :model="queryParams"
        ref="queryForm"
        size="small"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
      >
        <el-form-item label="提问人" prop="sysUserId">
          <el-input
            v-model="queryParams.userName"
            placeholder="请输入提问人"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="内容" prop="sysUserId">
          <el-input
            v-model="queryParams.problemContent"
            placeholder="请输入内容"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="开发状态" prop="openStatu">
          <el-select v-model="queryParams.openStatu" placeholder="请选择开发状态" clearable>
            <el-option
              v-for="dict in dict.type.problem_statu"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="提问时间" prop="creationTime">
          <el-date-picker
            clearable
            v-model="queryParams.creationTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择提问时间"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['problemSystem:problem_answer:edit']"
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
            v-hasPermi="['problemSystem:problem_answer:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['problemSystem:problem_answer:export']"
          >导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table
        v-loading="loading"
        :data="problem_answerList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="提问人" align="center" prop="userName" width="100" />
        <el-table-column label="提问时间" align="center" prop="creationTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.creationTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开放状态" align="center" width="100">
          <template slot-scope="scope">{{scope.row.openStatu==0?'开放':'关闭'}}</template>
        </el-table-column>
        <el-table-column class="clearfix" label="问题文本" align="center">
          <template slot-scope="scope">
            <div
              id="custom-div"
              :class="urlClass"
              @click="handleChick(scope.row)"
              v-html="scope.row.problemContent"
            ></div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-if="userId==scope.row.sysUserId"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-if="userId==scope.row.sysUserId"
            >删除</el-button>
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleChick(scope.row)"
            >查看</el-button>-->
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
    </div>

    <!-- 添加或修改提问于回答对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="问题文本">
          <editor v-model="form.problemContent" :min-height="190" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看问题 -->
    <div v-show="openshow">
      <label>问题：</label>
      <h1 class="clearfix"> <span id="custom-div" v-html="problem.problemContent"></span></h1>
      <label style="padding-right: 80px;">提问人：{{problem.userName}}</label>
      <label style="padding-right: 80px;">提问时间：{{problem.creationTime}}</label>
      <el-button
        v-if="userId==problem.sysUserId"
        @click="updateOpenStatu(problem.openStatu)"
      >{{problem.openStatu==0?'关闭开放状态':'开启开放状态'}}</el-button>
      <h4>答案：</h4>
      <ul style="list-style-type: none;">
        <li v-for="answer in  answerList" :key="answer">
          <el-main>
            <el-row :gutter="10">
              <el-col :span="10">
                <span style="font-weight: bold;color: pink;">{{answer.answerName}} :</span>
                <el-tag v-show="answer.answerStatu==1" size="mini">(优质回答)</el-tag>
              </el-col>
            </el-row>
            <el-row>
              <label id="custom-div" v-html="answer.answerContent"></label>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="5" style="align-items: center;">
                <label style="color: gray;">{{answer.creationTime}}</label>
              </el-col>
              <el-col :span="10">
                <el-button
                  v-if="userId==answer.sysUserId && userId!=problem.sysUserId"
                  @click="handleUpdateAnswer(answer)"
                >修改答案</el-button>
                <el-button
                  v-if="userId==answer.sysUserId && userId!=problem.sysUserId"
                  @click="handleDeleteAnswer(answer)"
                >删除答案</el-button>
                <el-button
                  v-if="userId==problem.sysUserId"
                  @click="handleUpdateStatu(answer)"
                >{{answer.answerStatu==1?'移除':'添加'}}优质答案</el-button>
              </el-col>
            </el-row>
          </el-main>
          <hr />
        </li>
      </ul>
      <pagination
        v-show="answerTotal>0"
        :total="answerTotal"
        :page.sync="answerQueryParams.pageNum"
        :limit.sync="answerQueryParams.pageSize"
        @pagination="handleChick"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="showCancel">返 回</el-button>
      </div>
      <div v-if="userId!=problem.sysUserId">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item class="clearfix">
            <h3>回答：</h3>
            <editor v-model="form.answerContent" :min-height="192" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFormAnswer">发 布</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  listProblem_answer,
  getProblem_answer,
  delProblem_answer,
  addProblem_answer,
  updateProblem_answer
} from "@/api/problemSystem/problem_answer";
import {
  listAnswer,
  getAnswer,
  delAnswer,
  addAnswer,
  updateAnswer
} from "@/api/problemSystem/answer";
export default {
  name: "Problem_answer",
  dicts: ["problem_statu"],
  data() {
    return {
      // 遮罩层
      loading: true,
      //用户id
      userId: this.$store.state.user.id,
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
      //答案总条数
      answerTotal: 0,
      // 提问于回答表格数据
      problem_answerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openmain: true,
      openshow: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sysUserId: null,
        userName: null,
        creationTime: null,
        problemStatu: null,
        problemContent: null
      },
      //查询答案的参数
      answerQueryParams: {
        pageNum: 1,
        pageSize: 10,
        sysUserId: null,
        userName: "",
        creationTime: null,
        answerContent: null,
        problemId: null
      },
      // 问题表单参数
      form: {},
      // 答案的表单参数，
      answerForm: {},
      // 表单校验
      rules: {
        problemContent: [
          { required: true, message: "提问内容不能为空", trigger: "blur" }
        ],
        openStatu: [
          { required: true, message: "开放状态不能为空", trigger: "blur" }
        ]
      },
      problem: "",
      answerList: "",
      urlClass: "url-text"
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询提问于回答列表 */
    getList() {
      console.log(this.$store.state.user);
      this.loading = true;
      listProblem_answer(this.queryParams).then(response => {
        this.problem_answerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 查看返回
    showCancel() {
      this.openshow = false;
      this.openmain = true;
      this.problem = "";
      this.getList();
    },
    // 表单重置
    reset() {
      this.form = {
        problemId: null,
        sysUserId: null,
        creationTime: null,
        problemStatu: null,
        problemContent: null,
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
      this.ids = selection.map(item => item.problemId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加提问于回答";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const problemId = row.problemId || this.ids;
      getProblem_answer(problemId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改提问于回答";
      });
    },
    /** 修改按钮操作 */
    handleUpdateAnswer(row) {
      console.log(row);
      const answerId = row.answerId || this.ids;
      getAnswer(answerId).then(response => {
        this.form = response.data;
        this.title = "修改回答";
        console.log(row);
      });
      this.handleChick();
    },
    handleUpdateStatu(row) {
      if (row.answerStatu == 1) {
        row.answerStatu = 0;
      } else {
        row.answerStatu = 1;
      }
      updateAnswer({
        answerStatu: row.answerStatu,
        answerId: row.answerId
      }).then(response => {
        this.$modal.msgSuccess("修改成功");
      });
      this.handleChick();
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        this.form.sysUserId = this.$store.state.user.id;
        if (valid) {
          if (this.form.problemId != null) {
            updateProblem_answer(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProblem_answer(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 提交按钮 */
    submitFormAnswer() {
      this.form.sysUserId = this.userId;
      this.form.problemId = this.problem.problemId;
      listAnswer({
        sysUserId: this.userId,
        problemId: this.problem.problemId
      }).then(response => {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.problem.openStatu == 1) {
              alert("该问题已经关闭不可以再修改回答了");
              return;
            }
            if (this.form.answerId != null) {
              updateAnswer(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                falg = false;
              });
            } else {
              if (response.total >= 1) {
                alert("你已经发布过答案了，不能再发表了");
                return;
              }
              if (this.problem.openStatu == 1) {
                alert("该问题已经关闭不可以再发布评论了");
                return;
              }
              addAnswer(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
              });
            }
            this.reset();
            this.handleChick();
          }
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      if (this.problem.openStatu == 1) {
        alert("该问题已经关闭不可以再删除回答了");
        return;
      }
      const problemIds = row.problemId || this.ids;
      this.$modal
        .confirm('是否确认删除提问于回答编号为"' + problemIds + '"的数据项？')
        .then(function() {
          return delProblem_answer(problemIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** Answer删除按钮操作 */
    handleDeleteAnswer(row) {
      const answerIds = row.answerId;
      this.$modal
        .confirm("是否确认删除？")
        .then(function() {
          return delAnswer(answerIds);
        })
        .then(() => {
          this.handleChick();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 查询按钮操作 */
    handleChick(row) {
      console.log(row);
      this.form.answerContent = null;
      if (this.problem == "") {
        this.problem = row;
      }

      this.loading = true;
      this.answerQueryParams.problemId = this.problem.problemId;
      listAnswer(this.answerQueryParams).then(response => {
        this.answerList = response.rows;
        this.answerTotal = response.total;
      });
      console.log(this.userId);
      this.openmain = false;
      this.openshow = true;
    },
    // 切换promble的开放状态
    updateOpenStatu(openStatu) {
      updateProblem_answer({
        problemId: this.problem.problemId,
        openStatu: openStatu == 0 ? 1 : 0
      }).then(response => {
        this.$modal.msgSuccess("切换成功成功");
        this.getList();
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "problemSystem/problem_answer/export",
        {
          ...this.queryParams
        },
        `problem_answer_${new Date().getTime()}.xlsx`
      );
    }
  }
};
</script>
<style>
.url-style {
  padding: 8px;
  background-color: #f0f0f0;
  border-radius: 4px;
}
#custom-div img {
  /* width: 300px; 设置图片宽度为 300 像素 */
  height: 200px; /* 设置图片高度为 200 像素 */
  float: left; /* 将图片设置为左浮动 */
  margin-right: 10px; /* 可选：设置右边距以增加图片与文本之间的间隔 */
}
.clearfix::after {
      content: "";
      display: table;
      clear: both;
    }
.url-text {
  color: blue;
  text-decoration: underline;
}
.url-end {
  color: rgb(226, 23, 135);
  text-decoration: underline;
}

/* 布局样式 */
.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}
.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: left;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>