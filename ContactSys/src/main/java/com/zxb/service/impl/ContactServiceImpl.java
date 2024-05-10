package com.zxb.service.impl;

import com.zxb.pojo.Contact;
import com.zxb.pojo.Contacts;
import com.zxb.service.ContactService;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    //这边需要用绝对路径，相对路径会出现xml文件不更新的情况！！！！！！
    private static final String CONTACTS_FILE = "F:/Data/IDEAProject/XMLFinal/ContactSys/src/main/resources/Contact.xml";
    private Contacts contacts;

    public ContactServiceImpl() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Contacts.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        try {
            contacts = (Contacts) unmarshaller.unmarshal(new File(CONTACTS_FILE));

        } catch (Exception e) {
            contacts = new Contacts();
        }
    }

    public void saveContacts() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Contacts.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(contacts, new File(CONTACTS_FILE));
    }


    @Override
    public void addOneContact(Contact contact) {
        contacts.getContactList().add(contact);
    }

    @Override
    public void delOneContact(Integer id) {
        Contact contact = getContactById(id);
        if (contact != null) {
            contacts.getContactList().remove(contact);
        }
    }

    public Contact getContactById(int id) {
        return contacts.getContactList().stream()
                .filter(contact -> contact.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Contact getLastContact() {
        Contact lastContact = null;
        List<Contact> contactList = contacts.getContactList();
        lastContact = contactList.get(contactList.size() - 1);
            return lastContact;
    }

    @Override
    public void updateOneContact(Integer id,Contact updatedContact) {
        Contact contact = getContactById(id);
        //找到了对应的联系人
        if (contact != null) {
            int index = contacts.getContactList().indexOf(contact);
            contacts.getContactList().set(index, updatedContact);
        }

    }

    @Override
    public List<Contact> getAllContacts(int pageNum,int pageSize) {
        // 计算开始索引和结束索引
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = pageNum * pageSize;

        // 使用Stream API进行分页查询
        return contacts.getContactList().stream()
                .skip(startIndex)
                .limit(pageSize)
                .collect(Collectors.toList());
    }


    @Override
    public List<Contact> findByTelAndName(String telephone,String name,Integer userId) {

        // 使用Stream API进行模糊匹配
        return contacts.getContactList().stream()
                .filter(contact -> contact.getName().contains(name) && contact.getTelephone().contains(telephone) && contact.getAffiliate()==userId)
                .collect(Collectors.toList());

    }


    @Override
    public boolean deleteContactsByIds(List<Integer> ids) {
        boolean b = contacts.getContactList().removeIf(contact -> ids.contains(contact.getId()));
        try {
            saveContacts();
        }catch (JAXBException e){
            e.printStackTrace();
        }
        return b;

    }

    @Override
    public List<Contact> getContactsByUserId(Integer userId) {
        return contacts.getContactList().stream()
                .filter(contact -> contact.getAffiliate()==userId)
                .collect(Collectors.toList());
    }

    //独立的分页方法
    public List<Contact> dividePage(List<Contact> contacts,int pageNum,int pageSize){
        // 计算开始索引和结束索引
        int startIndex = (pageNum - 1) * pageSize;
        return contacts.stream()
                .skip(startIndex)
                .limit(pageSize)
                .collect(Collectors.toList());
    }


}
