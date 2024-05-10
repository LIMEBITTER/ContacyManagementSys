package com.zxb.controller;

import com.zxb.pojo.User;
import com.zxb.service.impl.UserServiceImpl;
import com.zxb.utils.Constants;
import com.zxb.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userService;

    @Value("${file.upload.path}")
    private String fileUploadPath;


    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result registerUser(@RequestBody User user) {
        //这边需要在注册时先查询是否存在已有的！！！！！！！（未完成----2024/5/4）
        User oneUser = userService.getUserByName(user.getUsername());
        //用户存在
        if (oneUser!=null){
            return Result.error(Constants.CODE_668,"用户已存在");
        }else {
            Integer lastId = (userService.getLastUser().getId())+1;
            user.setId(lastId);
            userService.addUser(user);
        }
        try {
            //保存
            userService.saveUsers();
        } catch (JAXBException e) {
            return Result.error(Constants.CODE_500,"服务器错误!");
        }
        return Result.success();
    }

    @PostMapping("/login")
    public Result getContactById(@RequestBody User user) {
        User oneUser = userService.getOneUser(user);
        if (oneUser != null) {
            //输入的user并没有id等属性，所以将查到的对象复制到源目标
            BeanUtils.copyProperties(oneUser,user);
            return Result.success(user);
        } else {
            return Result.error(Constants.CODE_400,"用户名或密码错误!");
        }
    }

    @GetMapping("/updatePassword")
    public Result updatePassword(@RequestParam("userId") Integer userId,@RequestParam("password") String password){
        System.out.println("userId"+userId);
        if (userId!=null){
            boolean b = userService.updatePassword(userId, password);
            if (b){
                return Result.success();
            }
            return Result.error(Constants.CODE_400,"参数异常");
        }
        return Result.error(Constants.CODE_400,"参数为空!");

    }

    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam("file") MultipartFile image,@RequestParam("userId") Integer userId){
        String parentFile = fileUploadPath;//服务器保存图片的路径
        File parent = new File(parentFile);        //如果父文件夹不存在，就创建
        if (!parent.getParentFile().exists()) {
            parent.mkdirs();
        }
        if(image == null){
            return Result.error(Constants.CODE_400,"文件为空");
        }
        String filename = image.getOriginalFilename();  //获取上传图片的文件名，包含后缀
        String nowName = userId+"_"+filename;
        File file = new File(parent,nowName);
        try {
            image.transferTo(file);
            userService.uploadImage(userId, nowName);
            return Result.success();
        }catch (IOException e){
            e.printStackTrace();
        }
        return Result.error(Constants.CODE_400,"FAILURE");
    }

    @GetMapping("/getImage")
    public Result getImage(@RequestParam("userId") Integer userId){
        User currentUser = userService.getUserById(userId);
        return Result.success("http://localhost:9090/user_file/"+currentUser.getImage());
    }


}
