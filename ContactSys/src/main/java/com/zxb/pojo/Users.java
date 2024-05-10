package com.zxb.pojo;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Setter
public class Users {

    List<User> userList;

    @XmlElement(name = "user")
    public List<User> getUserList() {
        return userList;
    }
}
