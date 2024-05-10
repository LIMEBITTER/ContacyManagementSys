package com.zxb.service;

import com.zxb.pojo.Contact;

import java.util.List;

public interface ContactService {

    void addOneContact(Contact contact);
    void delOneContact(Integer id);
    void updateOneContact(Integer id,Contact updatedContact);

    List<Contact> getAllContacts(int pageNum,int pageSize);

    List<Contact> findByTelAndName(String telephone,String name,Integer userId);

    boolean deleteContactsByIds(List<Integer> ids);

    List<Contact> getContactsByUserId(Integer id);



}
