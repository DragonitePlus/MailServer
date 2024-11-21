<template>
  <el-table :data="users" style="width: 100%" @selection-change="handleSelectionChange" border>
    <!-- 选择框列 -->
    <el-table-column type="selection" width="50"></el-table-column>

    <!-- ID 列 -->
    <el-table-column prop="userId" label="ID" width="80" align="center"></el-table-column>

    <!-- 用户名列 -->
    <el-table-column prop="username" label="用户名" min-width="120" align="left"></el-table-column>

    <!-- 邮箱列 -->
    <el-table-column prop="email" label="邮箱" min-width="180" align="left"></el-table-column>

    <!-- 状态列 -->
    <el-table-column prop="status" label="状态" width="100" align="center">
      <template #default="scope">
        <el-tag :type="scope.row.status === '启用' ? 'success' : 'warning'">
          {{ scope.row.status }}
        </el-tag>
      </template>
    </el-table-column>

    <!-- 操作列 -->
    <el-table-column label="操作" width="200" align="center">
      <template #default="scope">
        <el-button
          v-if="scope.row.status === '启用'"
          type="warning"
          size="small"
          @click="toggleStatus(scope.row)"
        >
          封禁
        </el-button>
        <el-button
          v-else
          type="success"
          size="small"
          @click="toggleStatus(scope.row)"
        >
          启用
        </el-button>
        <el-button
          type="danger"
          size="small"
          @click="deleteUser(scope.row)"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  props: {
    users: {
      type: Array,
      default: () => [],
    },
  },
  methods: {
    handleSelectionChange(selected) {
      this.$emit("selection-change", selected);
    },
    toggleStatus(user) {
      this.$emit("update-status", user);
    },
    deleteUser(user) {
      this.$emit("delete", user);
    },
  },
};
</script>

