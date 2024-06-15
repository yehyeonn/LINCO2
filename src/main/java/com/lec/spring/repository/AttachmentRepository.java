package com.lec.spring.repository;

import com.lec.spring.domain.Attachment;

import java.util.List;
import java.util.Map;

public interface AttachmentRepository {

    int insert(List<Map<String, Object>> list, Long boardId);

    int save(Attachment file);

    List<Attachment> findByBoard(Long boardId);

    Attachment findById(Long id);

    List<Attachment> findByIds(Long [] ids);

    int deleteByIds(Long [] ids);

    int delete(Attachment file);
}
