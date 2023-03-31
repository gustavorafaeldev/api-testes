package com.gustavo.api.resource;

import com.gustavo.api.domain.User;
import com.gustavo.api.domain.dto.UserDTO;
import com.gustavo.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserResource {

    private final UserService service;

    private final ModelMapper mapper;

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(mapper.map(service.findById(id), UserDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(service.findAll()
                .stream().map(user -> mapper.map(user, UserDTO.class)).collect(Collectors.toList()));
    }
}
