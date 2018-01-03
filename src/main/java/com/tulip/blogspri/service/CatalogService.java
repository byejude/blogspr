package com.tulip.blogspri.service;


import com.tulip.blogspri.domain.Catalog;
import com.tulip.blogspri.domain.User;

import java.util.List;

public interface CatalogService {

    Catalog saveCatalog(Catalog catalog) throws Exception;

    void removeCatalog(Long id);

    Catalog getCatalogById(Long id);

    List<Catalog> listCatalogs(User user);
}
