package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Account;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;


public class AccountDaoImpl implements CustomAccountDao {
    private final AccountRepository accountRepository;
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AccountDaoImpl.class);
    public AccountDaoImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account findAccountById(Long id) {
        log.info("Processing db findAccountById before payment, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account with ID " + id + " not found"));
    }
}


