package com.gustavo.api.service;

import com.gustavo.api.domain.User;

import java.util.List;

public interface UserService {

    User findById(Integer id);
    List<User> findAll();
}
