package com.tulip.blogspri.service;

import com.tulip.blogspri.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User saveUser(User user);

    void removeUser(Long id);

    void removeUsersInBatch(List<User> users);

    User updateUser(User user);

    User getUserById(Long id);

    List<User> listUsers();

    Page<User> listUsersByNameLike(String name, Pageable pageable);

    List<User> listUsersByUsernames(Collection<String> username);
}
