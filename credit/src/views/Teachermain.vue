<template>
	<el-container class="home_container">
		<el-header class="home_header">
			<div class="home_title">学分银行管理系统</div>
			<div class="home_userinfoContainer">
				<el-dropdown>
					<span class="el-dropdown-link home_userinfo">
						{{teacher.name}}<i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
					</span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
			</div>
		</el-header>

  <el-container>
      <el-aside class="home_aside" width="200px">
        <el-menu router>
          <el-menu-item index="/viewrecord" :class="$route.path=='/viewrecord'?'is-active':''">
			 <!-- 地址栏访问时同样选中 -->
            <i class="el-icon-menu"></i>
            <span slot="title">档案检索</span>
          </el-menu-item>
          <el-menu-item index="/createrecord" :class="$route.path=='/createrecord'?'is-active':''">
            <i class="el-icon-document"></i>
            <span slot="title">新建档案</span>
          </el-menu-item>
          <el-menu-item index="/teacherCenter" :class="$route.path=='/teacherCenter'?'is-active':''">
            <i class="el-icon-setting"></i>
            <span slot="title">个人中心</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
	
	
	<el-container>
		<el-main>
			<el-breadcrumb separator-class="el-icon-arrow-right">
				<el-breadcrumb-item :to="{ path:'/' }">首页</el-breadcrumb-item>
				<el-breadcrumb-item v-text="this.$router.currentRoute.name"></el-breadcrumb-item>
			</el-breadcrumb>
			<router-view></router-view>
		        </el-main>
		        <el-footer class="home_footer">2025 ©CB</el-footer>
		      </el-container>
		    </el-container>
		  </el-container>
	
</template>

<script>
export default {
  name: "TeacherMain",
  data() {
    return {
      teacher:''
    }
  },
	  
  created(){
	  let teacher=JSON.parse(window.localStorage.getItem('teacher'))
	  this.teacher=teacher
  },
  methods: {
    logout() {
        this.$confirm('确定要注销登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => { // 确认操作
          localStorage.removeItem('teacher')
          this.$router.replace('/login')
        }).catch(() => { // 取消操作
          // 可在此处添加取消后的逻辑
          this.$message({
            type: 'info',
            message: '已取消操作'
          })
        })
      }
  }
}
</script>

<style scoped>
.home_container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 修改用户信息颜色 */
.home_userinfo {
  color: #409EFF; /* 修改用户名颜色 */
  font-size: 16px;
  cursor: pointer;
  transition: all .3s;
}

/* 修改下拉箭头颜色 */
.el-icon-arrow-down {
  color: #409EFF !important; /* 修改箭头颜色 */
}

/* 头部样式优化 */
.home_header {
  background: #304156;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  height: 60px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  z-index: 1;
}

.home_title {
  color: #ffffff;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 2px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
}

/* 侧边栏样式优化 */
.home_aside {
  background: #ffffff;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.1);
  height: calc(100vh - 60px);
}

.el-menu {
  border-right: none;
  padding-top: 20px;
}

.el-menu-item {
  height: 50px;
  line-height: 50px;
  margin: 4px 0;
  transition: all 0.3s;
}

.el-menu-item.is-active {
  background-color: #f0f7ff !important;
  color: #409EFF !important;
}

.el-menu-item:hover {
  background-color: #f5f7fa;
}

.el-menu-item [class^="el-icon-"] {
  font-size: 18px;
  vertical-align: middle;
  margin-right: 8px;
}

/* 主内容区样式优化 */
.el-main {
  padding: 20px;
  background: #f5f7fa;
  position: relative;
  min-height: calc(100vh - 100px);
}

.el-breadcrumb {
  margin-bottom: 20px;
  padding: 12px 20px;
  background: #ffffff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 页脚样式优化 */
.home_footer {
  height: 40px;
  line-height: 40px;
  text-align: center;
  color: #909399;
  background: #ffffff;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.05);
  font-size: 14px;
}

/* 用户信息下拉菜单优化 */
.home_userinfoContainer {
  position: relative;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 4px;
  transition: all 0.3s;
}

.el-dropdown-link:hover {
  background: rgba(255, 255, 255, 0.1);
}

/* 全局微调 */
::v-deep .el-menu-item.is-disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

::v-deep .el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #409EFF;
  font-weight: 500;
}
</style>