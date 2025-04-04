<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">高校课程互修学分系统登录</h2>
      <el-form 
        :rules="rules" 
        ref="form" 
        :model="form" 
        label-width="100px"
        class="login-form"
      >
        <el-form-item label="手机号" prop="phone">
          <el-input 
            v-model="form.phone" 
            placeholder="请输入手机号"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码"
            show-password
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item label="用户类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="teacher" border>教师用户</el-radio>
            <el-radio label="student" border>学生用户</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item class="action-buttons">
          <el-button 
            type="primary" 
            @click="handleLogin" 
            :loading="loading"
            size="medium"
            round
          >
            立即登录
          </el-button>
          <el-button 
            @click="resetForm" 
            size="medium"
            round
          >
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
  export default {
    data() {
      return {
		  creating:false,
        form: {
          phone: '',
		  password:'',
          type: '',
        },
		rules: {
		         phone: [
		           { required: true, message: '请输入账号', trigger: 'blur' },
		           
		         ],
				 password: [
				   { required: true, message: '请输入密码', trigger: 'blur' },
				   //{ min: 6, max: 11, message: '长度在 6 到 11 个字符', trigger: 'blur' }
				 ],
				 type: [
				     { required: true, message: '请选择用户类型', trigger: 'change' }
				   ]
					  },
		loading:false,
      }
    },

    methods: {
          handleLogin() {
            //console.log(this.form);
    		//console.log(this.form.type)
    		 this.$refs.form.validate((valid) => {
    		          if (valid) {
						  this.loading=true
    		            let _this=this
    					if(_this.form.type =='teacher'){
    						axios.get('http://localhost:8181/teacher/login',{params:_this.form}).then(function(resp){
    							//console.log(resp)
									_this.loading=false
								if(resp.data.code==-1){
									_this.$alert('用户不存在','提示',{
										confirmButtenText:'确认'
									})
								}
								if(resp.data.code==-2){
									_this.$alert('密码错误','提示',{
										confirmButtenText:'确认'
									})
								}
								if(resp.data.code==0){
									// _this.$alert('登录成功','提示',{
									// 	confirmButtenText:'确认'
									// })
									//存储当前登录用户的信息
									localStorage.setItem('teacher',JSON.stringify(resp.data.data));
									_this.$router.replace({path:'/teacher'})
								}
								
    						})
    					}
						if(_this.form.type =='student'){
							axios.get('http://localhost:8181/student/login',{params:_this.form}).then(function(resp){
								// console.log(resp)
									_this.loading=false
								if(resp.data.code==-1){
									_this.$alert('用户不存在','提示',{
										confirmButtenText:'确认'
									})
								}
								if(resp.data.code==-2){
									_this.$alert('密码错误','提示',{
										confirmButtenText:'确认'
									})
								}
								if(resp.data.code==0){
									// _this.$alert('登录成功','提示',{
									// 	confirmButtenText:'确认'
									// })
									// _this.$router.replace({path:'/student'})
									localStorage.setItem('student',JSON.stringify(resp.data.data));
									_this.$router.replace({path:'/student'})
								}
								
							})
						}
    		          }
    		        });
      },
	  resetForm() {
	      this.$refs.form.resetFields();
	    }
    }
  }
</script>
<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.login-box {
  background: #ffffff;
  padding: 40px 50px;
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  width: 480px;
  transition: all 0.3s ease;
}

.login-title {
  text-align: center;
  color: #303133;
  margin-bottom: 30px;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
}

.login-form {
  margin-top: 20px;
}

::v-deep .el-form-item__label {
  color: #606266;
  font-weight: 500;
}

.action-buttons {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 20px;
}

::v-deep .el-radio.is-bordered {
  padding: 12px 20px;
  border-radius: 8px;
  margin-right: 15px;
}

::v-deep .el-input__inner {
  border-radius: 6px;
  padding: 12px 15px;
}
</style>