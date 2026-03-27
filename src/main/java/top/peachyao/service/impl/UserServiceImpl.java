package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.peachyao.entity.User;
import top.peachyao.mapper.UserMapper;
import top.peachyao.service.UserService;

/**
 * @Description: 用户业务层接口实现类
 * @Author: PeaachYao
 * @Date: 2026-03-27
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw  new UsernameNotFoundException("用户不存在");
        }
        return user;
    }
}
