<template>
  <div class="container">
    <el-card class="box-card">
		
	<!-- 顶部查询框 -->
	<div class="filter-container">
	        <el-form ref="form" :model="listQuery" :inline="true">
	          <el-form-item>
	           <el-select v-model="listQuery.school" placeholder="请选择">
	           				<el-option :value="1" label="华中科技大学" />
	           				<el-option :value="2" label="武汉大学" />
	           				<el-option :value="3" label="华中师范大学" />
	           			</el-select>

	          </el-form-item>
	
	         <el-form-item>
	         			<el-button @click="resetForm('listQuery')" style="margin-left: 30px;">重置</el-button>
	         		</el-form-item>
	         		<el-form-item>
	         			<el-button type="primary" @click="onSubmit('listQuery')" style="margin-left: 10px;">查询</el-button>
	         		</el-form-item>
	         		

	        </el-form>
	      </div>
	
	<!-- 表格栏 -->
	<el-table
	ref="multipleTable"
	:data="tableData"
	tooltip-effect="dark"
	style="width: 100%"
	size="medium"
	>
	<el-table-column prop="id" label="档案编号" align="center"sortable />
	<el-table-column prop="createtime" label="创建时间" align="center"sortable />
	<el-table-column prop="tschool" label="授课学校" align="center" />
	<el-table-column prop="course" label="课程名称" align="center" />
	<el-table-column label="操作" align="center" width="200">
		<template slot-scope='scope'>
			<el-button size="mini"@click='view(scope.row)'>查看</el-button>
		</template>
	</el-table-column>
	<el-table-column label="状态" align="center" width="200">
		<template slot-scope='scope'>
			<p v-if="scope.row.affirm==0">
				<el-button size="mini" type="danger" @click='affirm(scope.row)'>确认</el-button>
			</p>
			<p v-if="scope.row.affirm==1">
				<el-button size="mini" type="success" disabled>已确认</el-button>
			</p>
		</template>
	</el-table-column>
	
		
		
	</el-table>
	
	
	
	
	<!-- 分页栏 -->
	<el-pagination
	background
	 layout="prev, pager, next" 
	:page-size="pageSize"
	:total="total"
	:current-page.sync="currentPage"
	@current-change="page"
	style="text-align: right;margin-top: 20px;"
	></el-pagination>
	
	
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
	
</el-card>
<div v-if="loading" class="loading-overlay">
	<div class="loading-spinner"></div>
	<div style="color: white;">&nbsp;&nbsp;档案内容正在上链</div>
</div>
	
	</div>
</template>

<script>
	import axios from 'axios';
	// import Pagination from '../components/Pagination'
	export default {
		name: 'Table',
		// components: { Pagination },
		data(){
			return {
				open: false,          // 控制第一层弹窗
			    secondopen: false,    // 控制第二层弹窗
			    currentRow: null ,      // 新增：保存当前点击的行数据
				//注入student对象
				student:'',
				//查询列表参数对象
				listQuery: {
					id:'',
					name:'',
					phone:'',
					course:'',
					page:'',
					size: 10,
					school: '',// 初始化school字段
				},
				// 私钥输入框
				privatekey:{
					key:'',
				},
				// 后端解密所需数据
				decryptForm:{
					privatekey:'',
					description:'',
					remark:'',
				},
				//当前页数
				currentPage: 1,
				//每页数据条数
				pageSize: 5,
				//数据总条数
				total: 0,
				//表格数据数组
				tableData: [],
				//弹出框数据数组
				dialogForm: {
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
				
				//加载提示
				loading: false,
				
				
				
			}
		},
		
		
		created() {
			const _this =this
			let student =JSON.parse(window.localStorage.getItem('student'))
			_this.student =student
			axios.get('http://localhost:8181/record/studentlist/' + _this.student.id+'/1/'+_this.pageSize).then(function(resp){
				console.log(resp)
				_this.tableData=resp.data.data.data
				_this.total=resp.data.data.total
			})
			
		},
		methods:{
			//页面切换	
			page(currentPage){
				const _this =this
				axios.get('http://localhost:8181/record/studentlist/' + _this.student.id+'/'+currentPage+'/'+_this.pageSize).then(function(resp){
					console.log(resp)
					_this.tableData=resp.data.data.data
					_this.total=resp.data.data.total
				})
				
			},
				//查询数据
					onSubmit(formName){
						
					},
					//重置查询
					resetForm(formName){
						this.$refs[formName].resetFields();
					},
						
					view(row){
						console.log(row)
						this.currentRow = row;  // 保存当前行数据
						this.open = true;       // 打开第一层弹窗
						
						this.dialogForm.tschool=row.tschool
						this.dialogForm.course=row.course
						this.dialogForm.createtime=row.createtime
						this.dialogForm.name=row.name
						this.dialogForm.phone=row.phone
						this.dialogForm.school=row.school
						this.dialogForm.stuid=row.stuid
						this.dialogForm.description=row.description
						this.dialogForm.remark=row.remark
						this.dialogForm.affirm=row.affirm
					},
					// 输入私钥验证
					    inputkey() {
					        const _this = this;
					        if (!_this.privatekey.key) {
					            this.$message.error('私钥不能为空');
					            return;
					        }
							_this.decryptForm.privatekey=_this.privatekey.key
							_this.decryptForm.description=_this.dialogForm.description
							_this.decryptForm.remark=_this.dialogForm.remark
					
					        // 调用解密接口（假设需要后端解密）
					        axios.post('http://localhost:8181/record/decrypt', _this.decryptForm).then(function (resp){
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
					
					// 确认档案并上传到区块链
					// Vue methods
					affirm(row) {
					    const _this = this;
					    this.$confirm('确认并上链？', '提示', {
					        confirmButtonText: '确定',
					        cancelButtonText: '取消',
					        type: 'warning'
					    }).then(() => {
					        axios.post(`http://localhost:8181/record/affirm`, {
					            id: row.id,
					            description: row.description,
					            remark: row.remark
					        }).then(resp => {
					            if (resp.data.code === 0) {
					                _this.$message.success('确认成功且数据已上链');
					                row.affirm = 1;
					            } else {
					                _this.$message.error(resp.data.msg);
					            }
					        }).catch(err => {
					            _this.$message.error('请求失败: ' + err.message);
					        });
					    }).catch(() => {
					        this.$message.info('已取消');
					    });
					}

			
			
		},
		
		
		
		
	}
</script>

<style scoped>
.container {
  padding: 20px;
  background: #f0f2f5;
  min-height: 100vh;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

.filter-container {
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}

.filter-item {
  margin-right: 15px;
}

.main-table {
  margin-top: 20px;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0,0,0,.1);
}

::v-deep .el-table__header th {
  background: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

::v-deep .el-table__body tr:hover > td {
  background-color: #f5f7fa !important;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409EFF;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media screen and (max-width: 768px) {
  .filter-container {
    padding: 15px;
  }
  
  .filter-item {
    margin-right: 10px;
    margin-bottom: 10px;
  }
  
  ::v-deep .el-form-item {
    margin-right: 0 !important;
  }
}
</style>