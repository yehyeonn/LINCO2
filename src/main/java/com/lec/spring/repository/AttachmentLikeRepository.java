package com.lec.spring.repository;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.AttachmentLike;
import com.lec.spring.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AttachmentLikeRepository {
    int addPostLikes(AttachmentLike like);
    int deletePostLikes(AttachmentLike like);
    AttachmentLike selectPostLikes(User user, Attachment attachment);

    List<AttachmentLike> findByUserId(Long user_id);
}
