<template>
  <el-menu mode="horizontal" @select="handleSelect">
    <el-menu-item index="userList">用户列表</el-menu-item>
    <el-menu-item index="mailList">邮件列表</el-menu-item>
  </el-menu>
</template>

<script>
export default {
  methods: {
    handleSelect(key) {
      this.$router.push({ name: key });
    },
  },
};
</script>
