package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao.CustomUserDao;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserDao {
}
