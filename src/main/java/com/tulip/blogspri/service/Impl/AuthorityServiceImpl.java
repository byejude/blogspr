package com.tulip.blogspri.service.Impl;

import com.tulip.blogspri.domain.Authority;
import com.tulip.blogspri.repository.AuthorityRepository;
import com.tulip.blogspri.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }
}
