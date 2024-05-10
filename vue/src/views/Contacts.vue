<!-- 联系人管理表格 -->

<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search" v-model="name"></el-input>
            <el-input style="width: 200px" placeholder="请输入电话号码" suffix-icon="el-icon-position" class="ml-5" v-model="telephone"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
            <el-popconfirm
                    class="ml-5"
                    confirm-button-text='您确认批量删除?'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定删除吗？"
                    @confirm="delBatch">
                <el-button type="danger" slot="reference" class="ml-5">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>
          <el-button type="primary"  @click="addContact"   class="ml-5">添加联系人 <i class="el-icon-top"></i></el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"  @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="120">
            </el-table-column>
            <el-table-column prop="sex" label="性别" width="120">
            </el-table-column>
            <el-table-column prop="age" label="年龄" width="120">
            </el-table-column>
            <el-table-column prop="address" label="地址">
            </el-table-column>
            <el-table-column prop="telephone" label="电话" width="120">
            </el-table-column>=
            <el-table-column label="操作"  width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>

                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确认删除'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="handleDelete(scope.row.id)">
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[5, 10, 15, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="35%">
            <el-form  label-width="100px" size="small" :model="form" :rules="addFormRules" ref="ruleForm">
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="sex">
                    <el-radio-group v-model="form.sex">
                        <el-radio label="男" value="男"></el-radio>
                        <el-radio label="女" value="女"></el-radio>
                    </el-radio-group>
                </el-form-item>
                <!--年龄验证不为空-->
                <el-form-item label="年龄" prop="age">
                    <el-input v-model.number="form.age" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="地址" prop="address">
                    <el-input v-model="form.address" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="telephone">
                    <el-input v-model="form.telephone" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleSave">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import request from "../utils/request.js";

    export default {
        name: "Resident",
        data(){
            //验证年龄
            let checkAge = (rule, value, callback) => {
                if (!Number.isInteger(value)) {
                    callback(new Error('请输入数字值'));
                }else {
                    callback()
                }
            };
            //验证电话
            let checkPhone = (rule, value, callback) => {
                let reg = /^1[12345789]\d{9}$/
                if (!reg.test(value)) {
                    callback(new Error('请输入11位手机号'))
                } else {
                    callback()
                }
            }
            return{

                //用户数据
                tableData:[],
                total:0,
                pageNum:1,
                pageSize:5,
                name: "",
                telephone: "",
                address:"",
                headerBg: 'headerBg',
                //嵌套表单
                dialogFormVisible:false,
                form:{},
                //批量删除属性
                multipleSelection:[],

                addFormRules:{
                    name:[
                        {required:true,message:'请输入姓名',trigger:'blur'},
                        {min:2,max:10,message: '长度在2到10个字符',trigger: 'blur'}
                    ],
                    sex:[
                        {required:true,message:'请选择性别',trigger:'change'},
                    ],
                    age:[
                        {required:true,type:'number',validator:checkAge,trigger:'blur'},
                    ],
                    address:[
                        {required:true,message:'请输入地址',trigger:'blur'},
                        {min:3,max:50,message: '长度在3到50个字符',trigger: 'blur'}
                    ],
                    telephone:[
                        {required:true,type:'number',validator:checkPhone,trigger:'blur'}
                    ],
                    localPeople:[
                        {required:true,message:'请选择类别',trigger:'change'},

                    ]


                }
            }
        },
        created(){
          //请求数据
          this.load()
        },
        methods:{

            //批量删除按钮
          handleSelectionChange(val){
            this.multipleSelection = val
          },

          //添加联系人
          addContact(){
            this.dialogFormVisible=true
            this.form={}
          },

            //批量删除方法
            delBatch(){
                let ids =this.multipleSelection.map(v =>v.id) //由纯对象数组转为ids数组[1,2,3]
                request.post("/api/contacts/batchDelete",ids).then(res =>{
                    //判断是否保存成功
                    if(res) {
                        this.$message.success("批量删除成功!")
                        this.dialogFormVisible=false
                        this.load()
                    }else{
                        this.$message.error("批量删除失败!")
                    }
                })
            },


            //用户删除按钮
            handleDelete(id){
                request.delete("/api/contacts/"+ id).then(res =>{
                    //判断是否保存成功
                    if(res) {
                        this.$message.success("删除成功!")
                        this.dialogFormVisible=false
                        this.load()
                    }else{
                        this.$message.error("删除id失败!")
                    }
                })
            },

            //用户编辑按钮
            handleEdit(row){
                this.form=Object.assign({},row)//将行对象的数据赋予到弹窗中
                this.dialogFormVisible =  true
                console.log('当前行',row)
            },

            //用户新增按钮
            handleAdd(){
                this.dialogFormVisible=true,
                    this.form={}
              this.form.localPeople = '是'


            },

            //用户新增按钮-确定按钮
            handleSave(){
                //表单校验
                this.$refs['ruleForm'].validate(valid=>{
                    console.log('表单校验',valid)
                    if(valid){
                      if (this.form.id==undefined){
                        this.form.id=0
                      }
                      console.log('表单合法！',this.form.id)

                      let userId = JSON.parse(localStorage.getItem("user")).id
                        request.post("/api/contacts/addAndUpdate/"+userId,this.form).then(res =>{
                            //判断是否保存成功
                            console.log('post后端')
                            if(res) {
                                console.log('post后端返回值',res)
                                this.$message.success("保存成功!")
                                this.dialogFormVisible=false
                                this.load()
                            }else{
                                this.$message.error("保存失败!")
                            }
                        })
                    }else{
                        console.log("表单格式非法!")

                        this.$nextTick(()=>{
                            this.scrollToTop(this.$refs['ruleForm'.$el])
                        })
                    }
                })
            },

            //分页信息
            handleSizeChange(pageSize){
                this.pageSize=pageSize
                this.load()
            },
            handleCurrentChange(pageNum){
                this.pageNum=pageNum
                this.load()
            },

            //重置按钮
            reset(){
                this.username="",
                    this.name="",
                    this.address= "",
                    this.load()
            },

            //加载用户信息
            load(){
              //如果模糊查询框值有一个不为空，则只执行模糊查询
              if (this.name == ""&&this.address==""){
                console.log("==========")
                this.request.get("/api/contacts/selectByUserId",{
                  params:{
                    pageNum:this.pageNum,
                    pageSize:this.pageSize,
                    userId:JSON.parse(localStorage.getItem("user")).id
                  }
                }).then(res =>{
                  console.log('加载用户信息',this.pageNum,this.pageSize)
                  this.tableData=res.data;
                  this.total = res.total
                  console.log('res.records-local',this.tableData)

                })
              }else {
                //执行模糊查询
                this.request.get("/api/contacts/fuzzySearch",{
                      params:{
                        pageNum:this.pageNum,
                        pageSize:this.pageSize,
                        name:this.name,
                        telephone:this.telephone,
                        userId:JSON.parse(localStorage.getItem("user")).id
                      }
                    }
                ).then(res =>{
                  console.log("模糊查询！！！！！！！",res.data.length)
                  this.tableData=res.data;
                  //
                  this.total = res.total
                  // console.log('res.records-resident',res)

                })
              }
            },


        }
    }
</script>

