package com.zxb.controller;


import com.zxb.pojo.Contact;
import com.zxb.service.impl.ContactServiceImpl;
import com.zxb.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.xml.bind.JAXBException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactServiceImpl contactService;

    @Autowired
    public ContactController(ContactServiceImpl contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public String getAllContacts(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize) {
        List<Contact> contacts = contactService.getAllContacts(pageNum,pageSize);

        int totalContactsCount = contacts.size();
        String s = Result.okGetStringByDataT("查询成功!",contacts,totalContactsCount);
        return s;
    }

    @GetMapping("/selectByUserId")
    public String getAllContactsByUserId(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize,@RequestParam("userId") Integer userId) {
        List<Contact> contactList = contactService.getContactsByUserId(userId);
        //执行分页
        List<Contact> contacts = contactService.dividePage(contactList, pageNum, pageSize);

        //统计总数
        int total = contactList.size();
        String s = Result.okGetStringByDataT("查询成功!",contacts,total);
        return s;
    }

    @GetMapping("/{id}")
    public String getContactById(@PathVariable int id) {
        Contact contact = contactService.getContactById(id);
        if (contact != null) {
            return Result.okGetStringByData("获取成功!",contact);
        } else {
            return Result.okGetString("获取失败!");
        }
    }


    //模糊查询
    @GetMapping("/fuzzySearch")
    public String getContactByTel(@RequestParam("telephone") String telephone,@RequestParam("name") String name,
                                  @RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize,@RequestParam("userId") Integer userId) {
        List<Contact> contact = contactService.findByTelAndName(telephone,name,userId);
        List<Contact> contacts = contactService.dividePage(contact, pageNum, pageSize);
        int total = contact.size();
        if (contact != null) {
            return Result.okGetStringByDataT("获取成功!",contacts,total);
        } else {
            return Result.okGetString("没有此人!");
        }
    }

    //批量删除
    @PostMapping("/batchDelete")
    public boolean deleteInBatches(@RequestBody List<Integer> ids){
        boolean b = contactService.deleteContactsByIds(ids);
        return b;

    }

    //添加以及修改操作
    @PostMapping("/addAndUpdate/{userId}")
    public String addAndUpdateContact(@RequestBody Contact contact,@PathVariable Integer userId) {
        int id = contact.getId();
        if (id==0){
            Contact lastContact = contactService.getLastContact();
            contact.setId(lastContact.getId()+1);
            contact.setAffiliate(userId);
            contactService.addOneContact(contact);
            try {
                contactService.saveContacts();
            } catch (JAXBException e) {
                return Result.okGetString("服务器错误!");
            }
            return Result.okGetStringByData("添加成功!",contact);
        }else {
            contactService.updateOneContact(id, contact);
            try {
                contactService.saveContacts();
            } catch (JAXBException e) {
                return Result.okGetString("服务器错误!");
            }
            return Result.okGetStringByData("更新成功!",contact);
        }
    }

    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable int id) {
        contactService.delOneContact(id);
        try {
            contactService.saveContacts();
        } catch (JAXBException e) {
            return Result.okGetString("服务器错误!");
        }
        return Result.okGetString("删除成功!");
    }


}
