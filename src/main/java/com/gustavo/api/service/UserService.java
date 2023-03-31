package com.gustavo.api.service;

import com.gustavo.api.domain.User;

public interface UserService {

    User findById(Integer id);
}
