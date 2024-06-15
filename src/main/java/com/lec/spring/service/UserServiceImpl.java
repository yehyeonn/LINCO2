//package com.lec.spring.service;
//
//import com.lec.spring.domain.User;
//import com.lec.spring.domain.UserAuthority;
//import com.lec.spring.repository.UserAuthorityRepository;
//import com.lec.spring.repository.UserRepository;
//import org.apache.ibatis.session.SqlSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    private UserRepository userRepository;
//    private UserAuthorityRepository userAuthorityRepository;
//
//    @Autowired
//    public UserServiceImpl(SqlSession sqlSession){
//        userRepository = sqlSession.getMapper(UserRepository.class);
//        userAuthorityRepository = sqlSession.getMapper(UserAuthorityRepository.class);
//    }
//
//    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username.toUpperCase());
//    }
//
//    @Override
//    public boolean isExist(String username) {
//        User user = userRepository.findByUsername(username.toUpperCase());
//        return user != null;
//    }
//
//    @Override
//    public int register(User user) {
//        user.setTel(user.getTel());
//        user.setUsername(user.getUsername().toUpperCase());
//        // 비밀번호 암호와 필요(Config)
//        user.setPassword(user.getPassword());
//        user.setName(user.getName());
//        user.setAddress(user.getAddress());
//        user.setGender(user.getGender());
//        user.setBirthday(user.getBirthday());
//        user.setProfile_picture(user.getProfile_picture());
//
//        userRepository.save(user);
//
//        UserAuthority userAuth = userAuthorityRepository.findByName("MEMBER");
//        userAuthorityRepository.addUserAuthority(user.getId(), userAuth.getId());
//
//        return 1;
//    }
//
//    @Override
//    public List<UserAuthority> selectAuthorityById(Long id) {
//        User user = userRepository.findById(id);
//        return userAuthorityRepository.findByUser(user);
//    }
//}
