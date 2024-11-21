<template>
  <div class="search-bar">
    <el-input v-model="searchValue" placeholder="请输入ID或用户名" />
    <el-button type="primary" @click="search">搜索</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchValue: "",
    };
  },
  methods: {
    search() {
      const query = isNaN(this.searchValue)
        ? { username: this.searchValue }
        : { userId: this.searchValue };
      this.$emit("search", query);
    },
  },
};
</script>
