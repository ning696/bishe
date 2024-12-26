import { createRouter, createWebHistory } from "vue-router";
import { getToken } from "@/utils/auth";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path:"/",
      redirect:'/oj/cuser',
      name: "Layout",
      component: () => import("@/views/Layout.vue"),
      children: [
        {
          path: "oj/cuser",
          name: "Cuser",
          component: () => import("@/views/Cuser.vue"),
        },
        {
          path: "oj/question",
          name: "Question",
          component: () => import("@/views/Question.vue"),
        },
        {
          path: "oj/exam",
          name: "Exam",
          component: () => import("@/views/Exam.vue"),
        },
        {
          path: "oj/exam/recruit/updateExam",
          name: "UpdateExam",
          component: () => import("@/components/exam/UpdateExam.vue"),
        },
      ],
    },
    {
      path: "/oj/login",
      name: "Login",
      component: () => import("@/views/Login.vue"),
    },
  ],
});

router.beforeEach((to, from, next) => {
  if (getToken()) {
    /* has token*/
    if (to.path === '/oj/login') {
      next({ path: '/oj/cuser' })
    } else {
      next()
    }
  } else {
    if (to.path !== '/oj/login') {
      next({
        path:'/oj/login'
      })
    } else {
      next()
    }
  }
})

export default router;
