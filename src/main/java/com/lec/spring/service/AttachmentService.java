package com.lec.spring.service;

import com.lec.spring.domain.Attachment;

import java.util.List;

public interface AttachmentService {

    Attachment findById(Long id);
    List<Attachment> findByAttachment(Long id);
}
