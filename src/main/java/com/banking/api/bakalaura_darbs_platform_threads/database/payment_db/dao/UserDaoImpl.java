package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.User;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository.UserRepository;

public class UserDaoImpl implements CustomUserDao {

    private final UserRepository userRepository;
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
    }
}
