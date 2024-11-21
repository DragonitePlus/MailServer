<template>
  <div class="footer">
    <div>已选择 {{ selectedUsersCount }} 名用户</div>
    <el-button type="primary" size="small">发送邮件</el-button>
  </div>
</template>

<script>
export default {
  props: {
    selectedUsersCount: {
      type: Number,
      default: 0,
    },
  },
};
</script>
