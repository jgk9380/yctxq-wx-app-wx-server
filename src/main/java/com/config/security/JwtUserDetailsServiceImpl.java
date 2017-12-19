package com.config.security;

import com.dao.p.LoginUserDao;
import com.entity.p.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
@Autowired
LoginUserDao loginUserDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername:"+username);
        LoginUser loginUser=loginUserDao.findByName(username);

        if (loginUser==null) {
            throw new UsernameNotFoundException(String.format("找不到登录用户：'%s'.", username));
        } else if(loginUser.getIsValid()==false) {
            throw new UsernameNotFoundException(String.format("用户 '%s'状态错误", username));
        }else{
            return new JwtUser(loginUser);
        }

    }
}
