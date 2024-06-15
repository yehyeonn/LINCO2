//package com.lec.spring.service;
//
//import com.lec.spring.domain.Socializing;
//import com.lec.spring.repository.SocializingRepository;
//import com.lec.spring.repository.UserRepository;
//import org.apache.ibatis.session.SqlSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public class SocializingServiceImpl implements SocializingService {
//
//    private UserRepository userRepository;
//    private SocializingRepository socializingRepository;
//
//    @Autowired
//    public SocializingServiceImpl(SqlSession sqlSession){
//        userRepository = sqlSession.getMapper(UserRepository.class);
//        socializingRepository = sqlSession.getMapper(SocializingRepository.class);
//    }
//
//    // 여기도 첨부파일 필요
//    @Override
//    public int write(Socializing socializing) {
//        return socializingRepository.save(socializing);
//    }
//
//    // 세부사항
//    @Override
//    @Transactional
//    public Socializing detail(Long id) {
//        Socializing socializing = socializingRepository.findById(id);
//        return socializing;
//    }
//
//    @Override
//    public List<Socializing> list() {
//        return socializingRepository.findAll();
//    }
//
//    // 수정용
//    @Override
//    public Socializing selectById(Long id) {
//        Socializing socializing = socializingRepository.findById(id);
//
//        return socializing;
//    }
//
//    @Override
//    public int update(Socializing socializing) {
//        int result = 0;
//        result = socializingRepository.update(socializing);
//        return result;
//    }
//
//
//    @Override
//    public int deleteById(Long id) {
//        int result = 0;
//        Socializing socializing = socializingRepository.findById(id);
//        if(socializing != null){
//            result = socializingRepository.delete(socializing);
//        }
//        return result;
//    }
//}
