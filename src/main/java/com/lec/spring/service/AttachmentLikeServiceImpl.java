package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.AttachmentLike;
import com.lec.spring.domain.QryResult;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AttachmentLikeRepository;
import com.lec.spring.util.U;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttachmentLikeServiceImpl implements AttachmentLikeService {


    private AttachmentLikeRepository attachmentLikeRepository;

    @Autowired
    public AttachmentLikeServiceImpl(SqlSession sqlSession) {
        this.attachmentLikeRepository = sqlSession.getMapper(AttachmentLikeRepository.class);
    }

    @Transactional
    @Override
    public int addPostLikes(AttachmentLike like) {
        // 중복 체크
        AttachmentLike existingLike = attachmentLikeRepository.selectPostLikes(like.getUser(), like.getAttachment());
        if (existingLike == null) {
            return attachmentLikeRepository.addPostLikes(like);
        } else {
            throw new DuplicateKeyException("이미 좋아요를 눌렀습니다.");
        }
    }

    @Transactional
    @Override
    public int deletePostLikes(AttachmentLike like) {
        return attachmentLikeRepository.deletePostLikes(like);
    }

    @Override
    public AttachmentLike selectPostLikes(User user, Attachment attachment) {
        return attachmentLikeRepository.selectPostLikes(user, attachment);
    }

    @Override
    public List<AttachmentLike> getUserLikes() {
        User user = U.getLoggedUser();
        Long user_id = user.getId();
        return attachmentLikeRepository.findByUserId(user_id);
    }

}