package com.gustavo.api.service;

import com.gustavo.api.domain.User;
import com.gustavo.api.domain.dto.UserDTO;

import java.util.List;

public interface UserService {

    User findById(Integer id);
    List<User> findAll();

    User create(UserDTO obj);
}
