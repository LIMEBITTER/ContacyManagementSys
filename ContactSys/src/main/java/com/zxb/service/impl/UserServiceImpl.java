package com.zxb.service.impl;

import com.zxb.pojo.Contact;
import com.zxb.pojo.User;
import com.zxb.pojo.Users;
import com.zxb.service.UserService;
import com.zxb.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final String CONTACTS_FILE = "F:/Data/IDEAProject/XMLFinal/ContactSys/src/main/resources/User.xml";
    private Users users;

    public UserServiceImpl() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        try {
            users = (Users) unmarshaller.unmarshal(new File(CONTACTS_FILE));

        } catch (Exception e) {
            users = new Users();
        }
    }

    public void saveUsers() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(users, new File(CONTACTS_FILE));
    }

    @Override
    public void addUser(User user) {
        users.getUserList().add(user);
    }


    @Override
    public User getOneUser(User loginUser) {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        return users.getUserList().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);


    }

    @Override
    public User getUserByName(String username) {
        return users.getUserList().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User getLastUser() {
        User lastUser = null;
        List<User> userList = users.getUserList();
        lastUser = userList.get(userList.size() - 1);
        return lastUser;
    }

    public User getUserById(int id) {
        return users.getUserList().stream()
                .filter(contact -> contact.getId() == id)
                .findFirst()
                .orElse(null);
    }


    @Override
    public boolean updatePassword(Integer userId, String password) {

        User updatedUser = getUserById(userId);
        if (updatedUser!=null){
            updatedUser.setPassword(password);
            try {
                saveUsers();
            }catch (JAXBException e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean uploadImage(Integer userId, String imagePath) {
        User updatedUser = getUserById(userId);
        if (updatedUser!=null){
            updatedUser.setImage(imagePath);
            try {
                saveUsers();
            }catch (JAXBException e){
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }


}
