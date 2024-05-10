<template>
  <el-card style="width: 500px;">

    <el-form label-width="80px" size="small">

      <div style="text-align: center">
        <img v-if="imageUrl" :src="imageUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </div>


      <el-form-item label="用户名">
        <el-input disabled v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
        <el-form-item label="密码">
            <el-input disabled v-model="form.password" autocomplete="off" show-password></el-input>
          <el-button type="primary" style="margin-top: 20px" @click="updatePassword"   class="ml-5">修改密码 </el-button>
          <el-upload
              :on-change="handleChange"
              :on-success="handleSuccess"
              :action="uploadUrl"
              :headers="headers"
              :file-list="fileList">
            <el-button slot="trigger" size="small" type="primary">上传头像</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>


        </el-form-item>
      <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="35%">
        <el-form  label-width="100px" size="small" :model="updateForm" :rules="rules" ref="ruleForm">
          <el-form-item prop="password">
            <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="updateForm.password"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input placeholder="请确认密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="updateForm.confirmPassword"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSave">确 定</el-button>
        </div>
      </el-dialog>


    </el-form>
  </el-card>
</template>

<script>
import {serverIp} from "../../public/config";
import request from "../utils/request";

export default {
  name: "Person",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

      fileList: [],
      imageUrl: '',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('user')
      },

      serverIp: serverIp,
      form: {},
      updateForm:{
        userId:null,
        password:'',
      },
      dialogFormVisible:false,
      rules: {
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
  created() {
    //请求数据（后台接口）

      let userInfo = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
      this.id = userInfo.id
  },

  mounted(){
    this.form = this.user
    this.loadAvatar()

  },
  computed:{
    // 指定上传图片的URL
    uploadUrl(){
      return 'http://localhost:9090/api/user/uploadImage?userId='+this.user.id
    }
  },


  methods: {
    updatePassword(){
      this.dialogFormVisible = true;
      this.updateForm = {}

    },
    handleSave(){
      this.$refs['ruleForm'].validate(valid=>{
        if (valid){
          if (this.updateForm.password !== this.updateForm.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }

          this.updateForm.userId = this.user.id;
          console.log('=====',this.updateForm.userId)
          request.get('/api/user/updatePassword',{
            params:{
              password:this.updateForm.password,
              userId:JSON.parse(localStorage.getItem("user")).id
            }
          }).then(res=>{
            if (res.code==='200'){
              this.$message.success('修改成功,请重新登录')
              localStorage.removeItem('user')
              window.location.reload();

            }else {
              this.$message.error("参数错误,重新提交")
            }
          })


        }else {
          this.$nextTick(()=>{
            this.scrollToTop(this.$refs['ruleForm'.$el])
          })
        }
      })
    },

    handleChange(file, fileList) {
      this.fileList = fileList.slice(-1); // 只保留最新选择的文件
    },
    handleSuccess(res, file) {
      // 上传成功后处理图片显示

      console.log('======url',res)
      if (res.code==='200'){
        this.$message.success('上传成功');
        this.loadAvatar()
      }

      // this.imageUrl = URL.createObjectURL(file.raw); // 这里应该有图片的URL
    },

    loadAvatar(){
      request.get('/api/user/getImage/',{
        params:{
          userId:this.user.id
        }
      }).then(res=>{
        this.imageUrl = res.data
        console.log('========img',this.imageUrl)
      })
    }

  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  margin-bottom: 20px;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
