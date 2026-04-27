package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Account;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;


public class AccountDaoImpl implements CustomAccountDao {
    private final AccountRepository accountRepository;
    public AccountDaoImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Account findAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account with ID " + id + " not found"));
    }
}


