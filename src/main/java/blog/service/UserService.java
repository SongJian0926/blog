package blog.service;


import blog.entity.User;

public interface UserService {
    User checkUser(String username, String password);
}
