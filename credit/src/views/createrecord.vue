<template>
	<div style="margin-left: 300px;">
		<div class="record">
			<el-col :span="12">
				<!-- 表单 -->
				<el-form :model="createForm" :rules="rules" ref="createForm" label-position="labelPositon">
					<el-form-item label="姓名" prop="name">
						<label slot="label">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</label>
						<el-input placeholder="请输入学生姓名" v-model="createForm.name"></el-input>
					</el-form-item>
					<el-form-item label="联系电话" prop="phone">
						<el-input placeholder="请输入联系电话" v-model="createForm.phone"></el-input>
					</el-form-item>
					<el-form-item label="学校" prop="school">
						<label slot="label">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;校</label>
						<el-input placeholder="请输入学生学校" v-model="createForm.school"></el-input>
					</el-form-item>
					<el-form-item label="学号" prop="stuid">
						<label slot="label">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</label>
						<el-input placeholder="请输入学生学号" v-model="createForm.stuid"></el-input>
					</el-form-item>
					<el-form-item label="成绩档案" prop="description">
						<el-input type="textarea" placeholder="请输入成绩档案" v-model="createForm.description"></el-input>
					</el-form-item>
					<el-form-item label="备注" prop="remark">
						<label slot="label">&nbsp;&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</label>
						<el-input type="textarea" placeholder="备注" v-model="createForm.remark"></el-input>
					</el-form-item>
				</el-form>
				
				<!-- 按钮 -->
				<div class="subbutton">
					<el-button type="primary" @click="create('createForm')">+新建档案</el-button>
					<el-button @click="resetForm('createForm')" style="margin-left: 40px">重置</el-button>
				</div>
			</el-col>
		</div>
		
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
		
		
		<!-- 弹窗 -->
		<el-dialog
		title="确认成绩档案"
		:visible.sync="dialogVisible"
		width="50%">
		<span>
			<el-form :model="createForm" :inline="true" label-width="100px" class="demo-createForm">
				<el-form-item label="授课学校:">
					{{teacher.school}}
				</el-form-item>
				<el-form-item label="课程名称:">
					{{teacher.course}}
				</el-form-item>
				<el-form-item label="授课老师:">
					{{teacher.name}}
				</el-form-item>
				<br />
				<div style="margin-top: -20px;"><hr /></div>
				<el-form-item label="姓名:">
					<label slot="label">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</label>
					{{createForm.name}}
				</el-form-item>
				<el-form-item label="联系电话:">
					{{createForm.phone}}
				</el-form-item>
				<el-form-item label="学校:">
					<label slot="label">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;校</label>
					{{createForm.school}}
				</el-form-item>
				<el-form-item label="学号:">
					<label slot="label">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</label>
					{{createForm.stuid}}
				</el-form-item>
				<br />
				<el-form-item label="成绩档案:">
					{{createForm.description}}
				</el-form-item>
				<br />
				<el-form-item label="备注:">
					<label slot="label">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</label>
					{{createForm.remark}}
				</el-form-item>
			</el-form>
		</span>
		<span slot="footer" class="dialog-footer">
			<el-button @click="dialogVisible=false">取消</el-button>
			<el-button type="primary" @click="submitForm('createForm')">确认</el-button>
		</span>
		</el-dialog>
		
	</div>
</template>



<script>
import Axios from 'axios';

	export default {
		// name: "CreateRecord"
		data() {
			return{
				teacher:'',
				labelPosition:'left',
				dialogVisible: false,
				createForm:{
					tschool:'',
					course:'',
					teacherid:'',
					name:'',
					phone:'',
					school:'',
					stuid:'',
					description:'',
					remark:'',
				},
				rules:{
					name:[
						{required: true,message: '请输入学生姓名',trigger: 'blur' },
					],
					phone:[
						{required: true,message: '请输入联系电话',trigger: 'blur' },
					],
					school:[
						{required: true,message: '请输入学校',trigger: 'blur' },
					],
					stuid:[
						{required: true,message: '请输入学号',trigger: 'blur' },
					],
					description:[
						{required: true,message: '请输入成绩档案',trigger: 'blur' },
					],
				},	
			};
		},
		created() {
			let teacher=JSON.parse(window.localStorage.getItem('teacher'))
			this.teacher=teacher
		},
			
		methods:{
			//点击新建档案
			create(formName){
				const _this=this
				this.$refs[formName].validate((valid) =>{
						
					if(valid){
						//打开弹窗
						this.dialogVisible=true
					}else{
						alert('请完善内容！');
						return false;
					}
				}
				);
			},
			//确认档案
			submitForm(formName){
				const _this =this
				_this.createForm.tschool = _this.teacher.school
				_this.createForm.course = _this.teacher.course
				_this.createForm.teacherid = _this.teacher.id
				console.log(_this.createForm)
					
				axios.post('http://localhost:8181/record/create',_this.createForm).then(function (resp){
					// 根据后端 code 值判断结果
					      if (resp.data.code === 0) {
					        _this.$message.success('申请已提交');
					        row.authStatus = 0; // 更新前端状态为"已申请"
					      } else {
					        _this.$message.error(`提交失败: ${resp.data.msg || '学生信息错误请核对'}`);
					      }
					    })
					    .catch(error => {
					      // 处理网络或服务器错误
					      const errMsg = error.response?.data?.msg || '请求异常';
					      // _this.$message.error(`提交失败: ${errMsg}`);
				})
			},
			
			//重置表单
			resetForm(formName){
				this.$refs[formName].resetFields();
			}
		}
		
		
	}
</script>

<style scoped>
/* 按钮容器样式优化 */
.subbutton {
  margin-top: 40px;
  display: flex;
  justify-content: center; /* 水平居中 */
  gap: 40px; /* 按钮间距 */
  flex-wrap: wrap; /* 小屏幕自动换行 */
}

/* 统一按钮样式 */
::v-deep .subbutton .el-button {
  width: 180px;
  height: 42px;
  font-size: 16px;
  letter-spacing: 2px;
  transition: all 0.3s;
}

/* 主要按钮样式 */
::v-deep .subbutton .el-button--primary {
  background: #409EFF;
  border-color: #409EFF;
}

/* 重置按钮样式 */
::v-deep .subbutton .el-button:not(.el-button--primary) {
  background: #ffffff;
  border-color: #dcdfe6;
  color: #606266;
}

/* 按钮悬停效果 */
::v-deep .subbutton .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 移动端适配 */
@media screen and (max-width: 768px) {
  .subbutton {
    gap: 20px;
    padding: 0 20px;
  }
  
  ::v-deep .subbutton .el-button {
    width: 100%;
    margin: 5px 0;
  }
}
</style>