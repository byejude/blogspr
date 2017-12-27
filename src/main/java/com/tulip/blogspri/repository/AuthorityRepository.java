package com.tulip.blogspri.repository;

import com.tulip.blogspri.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
