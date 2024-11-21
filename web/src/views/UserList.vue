<template>
  <div class="user-list-container">
    <!-- 顶部工具栏 -->
    <div class="user-list-header">
      <div class="title">用户列表 ({{ users.length }})</div>
      <el-button type="primary" size="small" @click="openAddUserModal">添加用户</el-button>
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="请输入ID或用户名"
          size="small"
          class="search-input"
        />
        <el-button type="primary" size="small" @click="handleSearch">
          搜索
        </el-button>
      </div>
    </div>

    <!-- 用户表格 -->
    <UserTable
      :users="filteredUsers"
      @selection-change="handleSelectionChange"
      @update-status="updateUserStatus"
      @delete="deleteUser"
    />

    <!-- 底部工具栏 -->
    <div class="footer">
      <div>已选择 {{ selectedUsers.length }} 名用户</div>
      <el-button type="primary" style="margin-right: 50px" size="small">发送邮件</el-button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import UserTable from "@/components/UserTable.vue";

export default {
  components: { UserTable },
  data() {
    return {
      users: [],
      filteredUsers: [],
      searchQuery: "",
      selectedUsers: [],
    };
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get("/user/selectAll");
        this.users = response.data || [];
        this.filteredUsers = this.users; // 初始化为全用户
      } catch (error) {
        console.error("获取用户数据失败", error);
      }
    },
    handleSearch() {
      if (!this.searchQuery.trim()) {
        this.filteredUsers = this.users; // 恢复所有用户
        return;
      }
      const query = this.searchQuery.trim().toLowerCase();
      this.filteredUsers = this.users.filter(
        (user) =>
          user.userId.toString() === query || user.username.toLowerCase().includes(query)
      );
    },
    handleSelectionChange(selected) {
      this.selectedUsers = selected;
    },
    updateUserStatus(user) {
      // 更新用户状态逻辑
    },
    deleteUser(user) {
      // 删除用户逻辑
    },
    openAddUserModal() {
      // 添加用户逻辑
    },
  },
  mounted() {
    this.fetchUsers();
  },
};
</script>

<style scoped>
/* 顶部工具栏样式 */
.user-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding: 0 15px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #ddd;
  padding: 10px;
}
.user-list-header .title {
  font-size: 16px;
  font-weight: bold;
}
.search-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* 搜索输入框样式 */
.search-input {
  width: 200px;
}

/* 表格容器样式 */
.user-list-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 底部工具栏样式 */
.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  background-color: #f5f5f5;
  border-top: 1px solid #ddd;
  position: fixed;
  bottom: 0;
  width: 100%;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
}
</style>
