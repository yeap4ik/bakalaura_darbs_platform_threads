package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Account;

public interface CustomAccountDao {
    Account findAccountById(Long id);
}
