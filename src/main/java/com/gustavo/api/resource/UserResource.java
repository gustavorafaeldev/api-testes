package com.gustavo.api.resource;

import com.gustavo.api.domain.dto.UserDTO;
import com.gustavo.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserResource {

    public static final String ID = "{id}";

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping(ID)
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(mapper.map(service.findById(id), UserDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(service.findAll()
                .stream().map(user -> mapper.map(user, UserDTO.class)).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO obj) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ID).buildAndExpand(service.create(obj).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(ID)
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO obj, @PathVariable Integer id) {
        obj.setId(id);
        return ResponseEntity.ok(mapper.map(service.update(obj), UserDTO.class));
    }

    @DeleteMapping(ID)
    public ResponseEntity<UserDTO> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
