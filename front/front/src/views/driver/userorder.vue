<template>
  <div class="app-container">
    <el-table v-loading="loading" :data="CustomerOrderList" @selection-change="handleSelectionChange">
      <el-table-column label="车辆型号" align="center" prop="carType" />
      <el-table-column label="车系名称" align="center" prop="carSeries" />
      <el-table-column label="车辆数量" align="center" prop="quantity" />
      <el-table-column label="每辆车的单价" align="center" prop="unitPrice" />
      <el-table-column label="订单创建时间" align="center" prop="orderDate" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.orderDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus">
        <template slot-scope="scope">
          <span>{{ getOrderStatusText(scope.row.orderStatus) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >评论</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 评论弹框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="评论内容" prop="comment">
          <el-input
            type="textarea"
            v-model="form.comment"
            placeholder="请输入评论"
            rows="4"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitComment">提交评论</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCustomerOrder, getCustomerOrder, delCustomerOrder, addCustomerOrder, updateCustomerOrder, submitCommentToOrder } from "../../api/customerorder.js";

export default {
  name: "CustomerOrder",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      CustomerOrderList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        carType: null,
        carSeries: null,
        quantity: null,
        unitPrice: null,
        orderDate: null,
        orderStatus: null,
        customerId: null,
        orderDetailId: null
      },
      form: {
        orderDetailId: null,
        comment: ""
      },
      rules: {
        comment: [
          { required: true, message: "评论内容不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getOrderStatusText(status) {
      switch (status) {
        case '0':
          return '未支付';
        case '1':
          return '已支付';
        case '2':
          return '已取消';
        default:
          return '未知状态';
      }
    },

    getList() {
      this.loading = true;
      this.queryParams.customerId = this.$store.getters.id;
      listCustomerOrder(this.queryParams).then(response => {
        this.CustomerOrderList = response.data.rows;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    cancel() {
      this.open = false;
      this.reset();
    },

    reset() {
      this.form = {
        orderDetailId: null,
        comment: ""
      };
      this.resetForm("form");
    },

    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.customerId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单";
    },

    handleUpdate(row) {
      this.open = true;
      this.title = "添加评论";
      this.form.orderDetailId = row.orderDetailId;
      this.form.comment = row.comment || ""; // 如果已有评论，填充评论内容
    },

    submitComment() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 调用提交评论的API
          submitCommentToOrder(this.form).then(response => {
            this.$message.success("评论成功");
            this.open = false;
            this.getList(); // 更新列表
          }).catch(error => {
            this.$message.error(error);
          });
        }
      });
    },

    handleDelete(row) {
      const customerIds = row.customerId || this.ids;
      this.$modal.confirm('是否确认删除订单编号为"' + customerIds + '"的数据项？').then(function() {
        return delCustomerOrder(customerIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    handleExport() {
      this.download('system/CustomerOrder/export', {
        ...this.queryParams
      }, `CustomerOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
