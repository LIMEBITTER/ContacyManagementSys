<template>
    <div class="wrapper">
        <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
            <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
            <el-form :model="user" :rules="rules" ref="userForm">
                <el-form-item prop="username">
                    <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
                </el-form-item>
                <el-form-item prop="confirmPassword">
                    <el-input placeholder="请确认密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
                </el-form-item>
                <el-form-item style="margin: 10px 0; text-align: right">
                    <el-button type="primary" size="small"  autocomplete="off" @click="login">登录</el-button>
                    <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/register')">注册</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import request from "../utils/request";
    import {mapGetters} from "vuex";
    export default {
        name: "Login",
        computed:{
          ...mapGetters(['getMessage']),
        },

        mounted() {
          const message = this.$store.getters.getMessage;
          if (message){
            this.userStateMessage(message);

          }

        },
      data() {
            return {
                //绑定的user对象
                user: {},
                confirmPassword:"",
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
                    ],
                    confirmPassword: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
                    ],
                }
            }
        },
        methods: {
          login() {
                this.$refs['userForm'].validate((valid) => {
                    if (valid) {  // 表单校验合法
                        if (this.user.password !== this.user.confirmPassword) {
                            this.$message.error("两次输入的密码不一致")
                            return false
                        }
                        request.post("/api/user/login", JSON.stringify(this.user)).then(res => {
                            console.log('user:',JSON.stringify(this.user))
                            if(res.code === '200') {
                                console.log('user:'+JSON.stringify(res.data))

                                localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器

                                this.$router.push("/")
                                this.$message.success("登录成功")
                            } else {
                                this.$message.error(res.msg)
                            }
                        })
                    }
                });
            },

          //使用vuex进行数据的转发
          userStateMessage(message){
              this.$message({
                message:message.message,
                type:message.type
              });
              this.$store.commit('clearMessage')
          }
        }
    }
</script>

<style>
    .wrapper {
        height: 100vh;
        background-image: url("../assets/background.jpeg");
        background-repeat: no-repeat;
        background-size: 100% 100%;
        overflow: hidden;
    }
</style>
