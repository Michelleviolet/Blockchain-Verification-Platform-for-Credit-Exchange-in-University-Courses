<template>
  <div class="student-center-container">
    <el-card class="profile-card" shadow="never">
      <div class="profile-header">
        <h1 class="main-title">
          <i class="el-icon-user-solid title-icon"></i>
          学生信息中心
        </h1>
        <el-divider class="custom-divider"></el-divider>
      </div>

      <el-descriptions 
        :column="1" 
        border
        class="info-panel"
        label-class-name="info-label"
        content-class-name="info-content"
      >
        <!-- 用户ID -->
        <el-descriptions-item>
          <template slot="label">
            <span class="label-wrapper">
              <i class="el-icon-s-custom"></i>
              用户ID
            </span>
          </template>
          <el-tag type="info" class="id-tag">{{ student.id || '--' }}</el-tag>
        </el-descriptions-item>

        <!-- 姓名 -->
        <el-descriptions-item>
          <template slot="label">
            <span class="label-wrapper">
              <i class="el-icon-user"></i>
              姓名
            </span>
          </template>
          <div class="id-tag">{{ student.name || '未填写' }}</div>
        </el-descriptions-item>

        <!-- 联系电话 -->
        <el-descriptions-item>
          <template slot="label">
            <span class="label-wrapper">
              <i class="el-icon-mobile-phone"></i>
              联系电话
            </span>
          </template>
          <div class="id-tag">
            <el-link :href="`tel:${student.phone}`" type="primary">
              {{ student.phone || '未填写' }}
            </el-link>
          </div>
        </el-descriptions-item>

        <!-- 所属院校 -->
        <el-descriptions-item>
          <template slot="label">
            <span class="label-wrapper">
              <i class="el-icon-office-building"></i>
              所属院校
            </span>
          </template>
          <div class="id-tag">
            
              {{ student.school || '未设置' }}
            
          </div>
        </el-descriptions-item>

        <!-- 学生学号 -->
        <el-descriptions-item>
          <template slot="label">
            <span class="label-wrapper">
              <i class="el-icon-tickets"></i>
              学生学号
            </span>
          </template>
          <div class="id-tag">
           {{ student.stuId || '未设置' }}
          </div>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button 
          type="primary" 
          icon="el-icon-edit" 
          class="custom-button"
          @click="handleEdit"
        >
          编辑信息
        </el-button>
        <el-button 
          icon="el-icon-refresh-right" 
          class="custom-button"
          @click="refreshData"
        >
          刷新数据
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "StudentCenter",
  data() {
    return {
      student: {
        id: '',
        name: '',
        phone: '',
        school: '',
        stuId: ''
      }
    }
  },
  created() {
    this.loadStudentInfo();
  },
  methods: {
    loadStudentInfo() {
      const storedData = window.localStorage.getItem('student');
      if (storedData) {
        try {
          this.student = JSON.parse(storedData);
        } catch (e) {
          console.error('学生信息解析失败:', e);
          this.$message.error('个人信息加载失败');
        }
      } else {
        this.$message.warning('未找到学生信息');
      }
    },
    refreshData() {
      this.loadStudentInfo();
      this.$message.success('数据已刷新');
    },
    handleEdit() {
      // 编辑逻辑实现
    }
  }
}
</script>



<style scoped>
.student-center-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 0 1rem;
}

.profile-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.profile-card .el-card__body {
  padding: 2rem;
}

.main-title {
  color: #2c3e50;
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
  display: flex;
  align-items: center;
}

.title-icon {
  margin-right: 0.8rem;
  font-size: 2rem;
  color: #409EFF;
}

.custom-divider {
  margin: 1.5rem 0;
  background-color: #ebeef5;
}

.info-panel {
  margin: 2rem 0;
}

.info-panel .el-descriptions-item__cell {
  padding: 1.2rem;
}

.label-wrapper {
  display: flex;
  align-items: center;
}

.label-wrapper i {
  margin-right: 0.8rem;
  font-size: 1.2rem;
  color: #909399;
}

.highlight-text {
  font-size: 1.1rem;
  color: #303133;
  font-weight: 500;
}

.id-tag {
  font-size: 0.9rem;
  letter-spacing: 1px;
}

.contact-info .el-link {
  font-size: 1.1rem;
}

.school-info .el-tag {
  font-size: 1rem;
  padding: 0.6rem 1rem;
}

.stuid-wrapper .el-tag {
  font-family: monospace;
  font-size: 1.1rem;
  padding: 0.6rem 1.2rem;
}

.empty-tip {
  color: #f56c6c;
  font-size: 0.9rem;
}

.action-buttons {
  margin-top: 2.5rem;
  text-align: center;
}

.custom-button {
  padding: 0.8rem 2rem;
  font-size: 1rem;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.custom-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .profile-card .el-card__body {
    padding: 1.5rem;
  }
  
  .main-title {
    font-size: 1.5rem;
  }
  
  .title-icon {
    font-size: 1.8rem;
  }
  
  .info-panel .el-descriptions-item__cell {
    padding: 1rem;
  }
  
  .action-buttons .custom-button {
    width: 100%;
    margin-bottom: 1rem;
  }
}
</style>