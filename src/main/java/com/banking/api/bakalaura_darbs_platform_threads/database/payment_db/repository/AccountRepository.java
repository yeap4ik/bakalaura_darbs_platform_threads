package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao.CustomAccountDao;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, CustomAccountDao {
}