package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao.CustomAttachmentsDao;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentsRepository extends JpaRepository<Attachments, Long>, CustomAttachmentsDao {
}
