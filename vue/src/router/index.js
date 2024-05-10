import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: ()=> import('../views/Manager.vue'),
    meta:{title:"首页",requireAuth: true},
    //重定向至 /person
    redirect:"/person",
    children:[
        //个人信息界面
      {path: 'person', name: 'Person', component: ()=> import('../views/Person.vue'), meta:{title:"用户信息",requireAuth: true}},

    ]
  },
,
  {
    path:'/admin',
    name:'Admin',
    component: ()=> import('../views/Manager.vue'),
    meta:{title:"管理",requireAuth: true},
    redirect: "admin/user",
    children:[
        //联系人管理界面
      {path: 'contacts', name: 'Contacts', component: ()=> import('../views/Contacts.vue'), meta:{title:"联系人列表",requireAuth: true}},
    ]
  },

  //登录页面
  {
    path:'/login',
    name:'Login',
    component: ()=> import('../views/Login.vue'),
    meta:{title: "登录页面"}
  },
  //注册页面
  {
    path:'/register',
    name:'Register',
    component: () => import('../views/Register.vue'),
    meta:{title:"注册页面"}
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//路由守卫
router.beforeEach((to, from, next) => {
  let token = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

  console.log('beforeEach',to,from)
  //单语言项目 实现动态标题
  document.title = to.meta.title

  if (to.meta.requireAuth) { // 判断跳转的路由是否需要登录

    if (Object.keys(token).length!==0) { // vuex.state判断token是否存在
      next() // 已登录
    } else {

      //使用vuex
      store.commit('setMessage', { message: '请先登录', type: 'error' });
      next({
        path: '/login',
        query: {redirect: to.fullPath} // 将跳转的路由path作为参数，登录成功后跳转到该路由

      })
    }
  } else {
    next()
  }

})


export default router
