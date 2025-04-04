<template>
  <div class="container">
    <el-card class="box-card">
      <!-- 顶部查询栏 -->
      <div class="filter-container">
        <el-form ref="queryForm" :model="listQuery" :inline="true">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="listQuery.name" placeholder="请输入姓名" clearable />
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input v-model="listQuery.phone" placeholder="请输入电话" clearable />
          </el-form-item>
          <el-form-item label="课程" prop="course">
            <el-input v-model="listQuery.course" placeholder="请输入课程" clearable />
          </el-form-item>
          <el-form-item>
            <el-button @click="resetQuery">重置</el-button>
            <el-button type="primary" @click="fetchData">查询</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 表格栏 -->
      <el-table
        :data="tableData"
        border
        style="width: 100%"
      >
          <el-table-column prop="id" label="档案编号" align="center"sortable />
          <el-table-column prop="createtime" label="创建时间" align="center"sortable />
          <el-table-column prop="name" label="学生姓名" align="center" />
          <el-table-column prop="phone" label="学生电话" align="center" />
          <el-table-column prop="school" label="学生学校" align="center" />
          <el-table-column prop="stuid" label="学生学号" align="center" />
          <el-table-column prop="course" label="课程名称" align="center" />
          <el-table-column prop="tschool" label="教师学校" align="center" />
          <el-table-column label="操作" align="center" width="200">
           <template slot-scope="scope">
               <el-button
                   v-if="scope.row.state == -1"
                   type="primary"
                   @click="requestAuth(requestForm,scope.row)"
               >申请授权</el-button>
               
               <el-button
                   v-if="scope.row.state == 0"
                   type="warning"
                   disabled
               >已申请</el-button>
               
               <el-button
                   v-if="scope.row.state == 1"
                   type="success"
                   disabled
               >已授权</el-button>
               
               <el-button
                   type="info"
                   :disabled="scope.row.state !== 1"
                   @click="viewDetail(scope.row)"
               >查看</el-button>
           </template>
       </el-table-column>
      </el-table>
	  
	  
	  <!-- 输入私钥 第一层弹出栏 -->
	  <el-col :span="12">
	  	<el-dialog title="输入私钥" :visible.sync="open" width="50%">
	  		<el-form ref="privatekey" :model="privatekey" label-width="100px" :inline="true">
	  			<el-input v-model="privatekey.key" type="text" auto-complete="off" placeholder="请输入私钥"></el-input>
	  		</el-form>
	  		<el-button type="primary" @click="inputkey('privatekey')" style="margin-top: 20px;">确定</el-button>
	  	</el-dialog>
	  </el-col> 
	  
	  <!-- 查看第二层弹出栏 -->
	  <el-col :span="12">
	  	<el-dialog
	  	title="查看档案"
	  	:visible.sync="secondopen"
	  	width="50%"
	  	>
	  	 <el-form
	  	  ref="dialogForm"
	  	  :model="dialogForm"
	  	  label-width="100px"
	  	  :inline="true"
	  	  >
	  	   <el-form-item label="授课学校:">
	  		   {{dialogForm.tschool}}
	  	   </el-form-item>
	  	   <el-form-item label="课程名称:">
	  	   	   {{dialogForm.course}}
	  	   </el-form-item>
	  	   <el-form-item label="档案创建时间:" style="margin-left: 30px;">
	  	   	   {{dialogForm.createtime}}
	  	   </el-form-item>
	  	   <br />
	  	   <div style="margin-top: -20px;"><hr /></div>
	  	   <el-form-item label="姓名:">
	  		   <label slot="label">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</label>
	  		   {{dialogForm.name}}
	  	   </el-form-item>
	  	   <el-form-item label="联系电话:">
	  	   			   {{dialogForm.phone}}
	  	   </el-form-item>
	  	   <el-form-item label="学生院校:">
	  	   			   {{dialogForm.school}}
	  	   </el-form-item>
	  	   <el-form-item label="学生学号:">
	  	   			   {{dialogForm.stuid}}
	  	   </el-form-item>
	  	   <br />
	  	   <el-form-item label="成绩档案:">   			   
	  	   			   {{dialogForm.description}}
	  	   </el-form-item>
	  	   
	  	   <br />
	  	   <el-form-item label="备注:">
	  		   <label slot="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</label>
	  		   {{dialogForm.remark}}
	  	   </el-form-item>
	  	   
	  	   </el-form>
	  	</el-dialog>
	  	
	  </el-col>

      <!-- 分页栏 -->
      <el-pagination
        background
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="page"
      />
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
		open: false,          // 控制第一层弹窗
		secondopen: false,    // 控制第二层弹窗
      listQuery: {
        name: '',
        phone: '',
        course: ''
      },
      tableData: [],
     //当前页数
     currentPage: 1,
     //每页数据条数
     pageSize: 5,
     //数据总条数
     total: 0,
	 //默认
	requestForm:{
	 	recordid:'',
	 	teacherid:'',
	 	state: '',
	 },
	 // 私钥输入框
	 privatekey:{
	 	key:'',
	 },
	 // 后端解密所需数据
	 tdecryptForm:{
	 	privatekey:'',
	 	teacherid:'',
		recordid:'',
	 },
	 //弹出框数据数组
	 dialogForm: {
		 recordid:'',
	 	tschool: '',
	 	course: '',
	 	createtime: '',
	 	name: '',
	 	phone:'',
	 	school:'',
	 	stuid:'',
	 	description: '',
	 	remark: '',
	 	affirm: '',
	 
	 
	 },
	 
	 
    };
  },
  created() {
    const _this =this
	let teacher=JSON.parse(window.localStorage.getItem('teacher'))
	_this.teacher=teacher
	
	
	axios.get('http://localhost:8181/record/teacherlist/'+ _this.teacher.id+'/1/'+_this.pageSize).then(function(resp){
		console.log(resp)
		_this.tableData=resp.data.data.data
		_this.total=resp.data.data.total
		
		  // 初始化授权状态
	})
	
  },
  methods: {
	  //页面切换
	  page(currentPage){
	  	const _this =this
	  	axios.get('http://localhost:8181/record/teacherlist/' + _this.teacher.id+'/'+currentPage+'/'+_this.pageSize).then(function(resp){
	  		console.log(resp)
	  		_this.tableData=resp.data.data.data
	  		_this.total=resp.data.data.total
			
			 // 初始化授权状态
	  	})
	  	
	  },
    

    // 重置查询条件
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.currentPage = 1;
      this.fetchData();
    },

  

    // 申请授权

  requestAuth(formName,row) {
	  const _this =this
	 
	  _this.requestForm.teacherid = _this.teacher.id
	   _this.requestForm.recordid = row.id
	    _this.requestForm.state =0
	  console.log(_this.createForm)
	  	
	
    axios.post('http://localhost:8181/authorize/request', 
     _this.requestForm).then(function (resp){
					// 根据后端 code 值判断结果
					      if (resp.data.code === 0) {
					        _this.$message.success('申请已提交');
					        row.state = 0; // 更新前端状态为"已申请"
					      } else {
					        _this.$message.error(`提交失败: ${resp.data.msg || '未知错误'}`);
					      }
					    }).catch(() => {
      this.$message.error('提交失败');
    });
  },

    // 查看详情
    viewDetail(row) {
     console.log(row)
     this.currentRow = row;  // 保存当前行数据
     this.open = true;       // 打开第一层弹窗
     this.dialogForm.recordid=row.id
     this.dialogForm.tschool=row.tschool
     this.dialogForm.course=row.course
     this.dialogForm.createtime=row.createtime
     this.dialogForm.name=row.name
     this.dialogForm.phone=row.phone
     this.dialogForm.school=row.school
     this.dialogForm.stuid=row.stuid
     this.dialogForm.affirm=row.affirm
	
	 
	 
    },
	// 输入私钥验证
	    inputkey() {
	        const _this = this;
	        if (!_this.privatekey.key) {
	            this.$message.error('私钥不能为空');
	            return;
	        }
			_this.tdecryptForm.privatekey=_this.privatekey.key
			_this.tdecryptForm.teacherid=_this.teacher.id
			_this.tdecryptForm.recordid=_this.dialogForm.recordid
			console.log(_this.tdecryptForm)
	
	        // 调用解密接口
	        axios.post('http://localhost:8181/authorize/decrypt', _this.tdecryptForm).then(function (resp){
	            if (resp.data.code === 0) {
	                _this.open = false;          // 关闭第一层弹窗
	                _this.privatekey.key = '';    // 清空私钥输入
	                _this.secondopen = true;      // 打开第二层弹窗
	                // 填充解密后的数据
	                _this.dialogForm.description=resp.data.data.description
					_this.dialogForm.remark=resp.data.data.remark
	            } else {
	                this.$message.error('私钥错误，解密失败');
	            }
	        }).catch(() => {
	            this.$message.error('解密失败，请检查私钥');
	        });
	    },
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>