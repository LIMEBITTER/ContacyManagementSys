package com.zxb.pojo;



import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

//代表当前对应xml文件里面的根元素contacts
@XmlRootElement
@Setter
public class Contacts {

    List<Contact> contactList;

    //一个个子元素的映射注解自动和Contact类对应属性映射的注解
    //contact类的属性名称必须与xml文件下子属性名称相同
    //name  根元素下面对应的一级子元素的名称
    @XmlElement(name = "contact")
    public List<Contact> getContactList() {
        return contactList;
    }
}
