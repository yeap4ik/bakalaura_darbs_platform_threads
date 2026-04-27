package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.User;

public interface CustomUserDao {
    User findUserById(Long id);
}
