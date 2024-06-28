package com.lec.spring.service;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.ClubUserList;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserSocializing;
import com.lec.spring.repository.AuthorityRepository;
import com.lec.spring.repository.ClubUserListRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.repository.UserSocializingRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private ClubUserListRepository clubUserListRepository;
    private UserSocializingRepository userSocializingRepository;


    @Autowired
    public UserServiceImpl(SqlSession sqlSession){
        userRepository = sqlSession.getMapper(UserRepository.class);
        authorityRepository = sqlSession.getMapper(AuthorityRepository.class);
        clubUserListRepository = sqlSession.getMapper(ClubUserListRepository.class);
        userSocializingRepository = sqlSession.getMapper(UserSocializingRepository.class);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username.toUpperCase());
    }


    @Override
    public User findById(Long id){
        return userRepository.findById(id);
    }


    @Override
    public User findByUserId(Long userId) {
        return userRepository.findById(userId);
    }


    @Override
    public boolean isExist(String username) {
        User user = findByUsername(username.toUpperCase());
        return user != null;
    }

    @Override
    public int register(User user) {
        user.setTel(user.getTel());
        user.setUsername(user.getUsername().toUpperCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // password 는 암호화 하여 저장
        user.setName(user.getName());
        user.setAddress(user.getAddress());
        user.setGender(user.getGender());
        user.setBirthday(user.getBirthday());
        user.setProfile_picture(user.getProfile_picture());
        userRepository.save(user);

        Authority userAuth = authorityRepository.findByName("MEMBER");
        authorityRepository.addAuthority(user.getId(), userAuth.getId());

        return 1;
    }

    @Override
    public List<Authority> selectAuthoritiesById(Long id) {
        User user = userRepository.findById(id);
        return authorityRepository.findByUser(user);
    }

    // 유저가 가입한 클럽 리스트
    @Override
    public List<ClubUserList> getUserClubs(Long user_id) {
        return clubUserListRepository.findByUserId(user_id);
    }

    // 유저가 참여한 소셜라이징 리스트
    @Override
    public List<UserSocializing> getUserSocializings(Long user_id) {
        return userSocializingRepository.findBySocializingId(user_id);
    }

    // 유저 정보저장
    @Override
    public int save(User user) {
        userRepository.save(user);
        return 1;
    }

    @Override
    public int update(User user) {
        return userRepository.update(user);
    }
}
