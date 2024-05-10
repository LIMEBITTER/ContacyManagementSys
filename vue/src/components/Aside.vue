<!--导航菜单-->
<template>
    <!--router 模式：转至子项路由-->
    <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
             background-color="rgb(48, 65, 86)"
             text-color="#fff"
             active-text-color="#ffd04b"
             :collapse-transition="false"
             :collapse="isCollapse"
             router
    >
        <div style="height: 60px; line-height: 60px; text-align: center">
            <b style="color: white" v-show="logoTextShow">联系人管理系统</b>
        </div>

        <el-submenu index="1">
            <template slot="title">
                <i class="el-icon-menu"></i>
                <span>用户</span>
            </template>
            <el-menu-item index="/person">
                <span slot="title">用户信息</span>
            </el-menu-item>
        </el-submenu>

        <el-submenu index="2">
            <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">人员管理</span>
            </template>
                <el-menu-item index="/admin/contacts">
                    <i class="el-icon-s-custom"></i>
                    <span slot="title">联系人列表</span>
                </el-menu-item>
        </el-submenu>



    </el-menu>
</template>

<script>
    export default {
        name: "Aside",
        data(){
            return{

            }
        },
        props: {
            isCollapse: Boolean,
            logoTextShow: Boolean
        }
    }
</script>

<style scoped>

</style>
