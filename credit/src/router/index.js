import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Login from '../views/login.vue'
import Teachermain from '../views/Teachermain.vue'
import CreateRecord from '../views/createrecord.vue'
import ViewRecord from '../views/viewrecord.vue'
import TeacherCenter from '../views/teacherCenter.vue'

import Studentmain from '../views/Studentmain.vue'
import MyRecord from '../views/myrecord.vue'
import ManagePermission from '../views/managepermission.vue'
import StudentCenter from '../views/studentcenter.vue'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/login',
    name: '登录',
    component: Login
  },
  {
    path: '/teacher',
	name:'系统管理界面',
	component:Teachermain,
	redirect:'/viewrecord',
   children:[
	// 	//子路由
	// 	{path:'/createrecord',component:CreateRecord},//新建档案
	// 	{path:'/applypermission',component:ApplyPermission},//申请授权
	// 	{path:'/viewrecord',component:ViewRecord},//查看档案
	// 	{path:'/teacherCenter',component:TeacherCenter}//个人中心
	{
	  path: '/createrecord',
	  name: '新建档案',
	  component: CreateRecord
	},
	{
	  path: '/viewrecord',
	  name: '查看档案',
	  component: ViewRecord
	},
	{
	  path: '/teacherCenter',
	  name: '个人中心',
	  component: TeacherCenter
	}
	]
  },
  {
    path: '/student',
  	name:'系统管理界面',
  	component:Studentmain,
  	redirect:'/myrecord',
   children:[
  	// 	//子路由
  	// 	{path:'/createrecord',component:CreateRecord},//新建档案
  	// 	{path:'/applypermission',component:ApplyPermission},//申请授权
  	// 	{path:'/viewrecord',component:ViewRecord},//查看档案
  	// 	{path:'/teacherCenter',component:TeacherCenter}//个人中心
  	{
  	  path: '/myrecord',
  	  name: '我的档案',
  	  component: MyRecord
  	},
  	{
  	  path: '/managerpermission',
  	  name: '授权管理',
  	  component: ManagePermission
  	},
  	{
  	  path: '/studentCenter',
  	  name: '个人中心',
  	  component: StudentCenter
  	}
  	]
  },
  
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
