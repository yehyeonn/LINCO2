package com.lec.spring.service;

import com.lec.spring.domain.AttachmentLike;
import com.lec.spring.domain.QryResult;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AttachmentLikeRepository;
import com.lec.spring.util.U;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentLikeServiceImpl implements AttachmentLikeService {

    private AttachmentLikeRepository attachmentLikeRepository;

    @Autowired
    public AttachmentLikeServiceImpl(SqlSession sqlSession) {
        this.attachmentLikeRepository = sqlSession.getMapper(AttachmentLikeRepository.class);
    }
}
