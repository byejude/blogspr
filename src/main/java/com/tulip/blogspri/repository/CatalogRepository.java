package com.tulip.blogspri.repository;

import com.tulip.blogspri.domain.Catalog;
import com.tulip.blogspri.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CatalogRepository extends CrudRepository<Catalog,Long>{

    List<Catalog> findByUser(User user);


    List<Catalog> findByUserAndName(User user,String name);
}
