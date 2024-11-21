import { createRouter, createWebHistory } from "vue-router";
import UserList from "@/views/UserList.vue";
import MailList from "@/views/MailList.vue";

const routes = [
  { path: "/", redirect: "/userList" },
  { path: "/userList", name: "userList", component: UserList },
  { path: "/mailList", name: "mailList", component: MailList },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
