package com.lec.spring.repository;

import com.lec.spring.domain.AttachmentLike;

public interface AttachmentLikeRepository {
    void save(Long user_id, Long attachment_id);

    int findByAttachmentId(Long attachment_id);
}
