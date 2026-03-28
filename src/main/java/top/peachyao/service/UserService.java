package top.peachyao.service;

import top.peachyao.entity.User;

public interface UserService {
    User findUserByUsernameAndPassword(String username, String password);
}
