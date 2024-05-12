package org.example.plantdisease.repository;



import org.example.plantdisease.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    List<Attachment> findAllByIdIn(List<UUID> attachmentIdList);
}
