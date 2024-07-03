package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.AttachmentLike;
import com.lec.spring.domain.QryResult;
import com.lec.spring.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttachmentLikeService {

    int addPostLikes(AttachmentLike like);
    int deletePostLikes(AttachmentLike like);

    AttachmentLike selectPostLikes(User user, Attachment attachment);

    List<AttachmentLike> getUserLikes();
}
