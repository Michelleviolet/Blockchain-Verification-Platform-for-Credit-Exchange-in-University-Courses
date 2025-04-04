<template>
  <div class="manage-permission">
    <!-- 数据表格 -->
    <el-table
      :data="authorizeList"
      border
      style="width: 100%; margin-top: 20px"
    >
      <el-table-column prop="createtime" label="申请时间" align="center"sortable />
      <el-table-column prop="teacherid" label="老师ID" align="center" /> <!-- 新增 -->
      <el-table-column prop="tschool" label="老师所属学校" align="center" /> <!-- 修改 prop -->
      <el-table-column prop="course" label="课程名称" align="center" />
      <el-table-column prop="recordid" label="申请档案编号" align="center" /> <!-- 修改 prop -->
      
      <!-- 状态列 -->
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.state === 0 ? 'warning' : 'success'">
            {{ scope.row.state === 0 ? '未授权' : '已授权' }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作" align="center" width="120">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.state === 0"
            type="primary"
            size="mini"
            @click="showAuthDialog(scope.row)"
          >授权</el-button>
          <el-button
            v-else
            type="info"
            size="mini"
            disabled
          >已授权</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页栏 --> <!-- 新增分页组件 -->
    <el-pagination
      background
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="page"
    />
  <!-- 授权弹窗 -->
    <el-dialog
      title="授权操作"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <el-form>
        <el-form-item label="请输入私钥">
          <el-input
            v-model="privateKey"
            type="password"
            placeholder="请输入授权私钥"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <!-- 修改此处：移除 scope.row -->
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAuthorization">确认授权</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "ManagePermission",
  data() {
    return {
      authorizeList: [],     // 授权列表数据
      dialogVisible: false,  // 弹窗显示状态
      privateKey: '',        // 输入的私钥
      currentRecord: null    ,// 当前操作的记录
	  
	  //当前页数
	  currentPage: 1,
	  //每页数据条数
	  pageSize: 5,
	  //数据总条数
	  total: 0,
	student: null,
	// 后端修改授权表所需数据
	manageForm:{
		privatekey:'',
		authorizeid:'',
		recordid:'',
		teacherid:'',
	},
	
    };
  },
  created() {
   const _this =this
   let student =JSON.parse(window.localStorage.getItem('student'))
   _this.student =student
   this.fetchData(1); // 初始化加载第一页
   
  },
  methods: {
	   // 统一数据加载方法
	      fetchData(page) {
	        const url = `http://localhost:8181/authorize/managerlist/${this.student.id}/${page}/${this.pageSize}`;
	        axios.get(url)
	          .then(res => {
	            this.authorizeList = res.data.data.data; // 直接赋值给authorizeList
	            this.total = res.data.data.total;
	            this.currentPage = page;
	          })
	          .catch(err => {
	            this.$message.error('数据加载失败');
	          });
	      },
	  
	      // 分页切换
	      page(currentPage) {
	        this.fetchData(currentPage);
	      },
	  
   
    // 显示授权弹窗
    showAuthDialog(record) {
      this.currentRecord = record;
      this.dialogVisible = true;
    },

    // 执行授权操作
    async handleAuthorization() {
          if (!this.privateKey) {
            this.$message.warning('请输入授权私钥');
            return;
          }

   try {
           // 填充 manageForm 数据
           this.manageForm = {
             privatekey: this.privateKey,
             authorizeid: this.currentRecord.authorizeid, // 假设字段名正确
             recordid: this.currentRecord.recordid,
             teacherid: this.currentRecord.teacherid
           };
       
           const res = await axios.post('http://localhost:8181/authorize/confirm', this.manageForm);
			console.log(res);
           if (res.data.code === 0) {
             this.$message.success('授权成功');
             this.dialogVisible = false;
             this.fetchData(this.currentPage); // 刷新当前页数据
           } else {
             this.$message.error(res.data.msg || '授权失败');
           }
         } catch (error) {
           this.$message.error('授权失败，请检查私钥');
         }
    }
  }
};
</script>

<style scoped>
.manage-permission {
  padding: 20px;
}

.el-table {
  margin-top: 20px;
}

.el-dialog__body {
  padding: 20px 30px;
}
</style>