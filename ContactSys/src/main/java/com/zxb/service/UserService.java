package com.zxb.service;


import com.zxb.pojo.User;
import com.zxb.utils.Result;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {

    void addUser(User user);
    User getOneUser(User user);

    User getUserByName(String username);
    User getLastUser();
    boolean updatePassword(Integer userId, String password);
    boolean uploadImage(Integer userId, String image);
}
