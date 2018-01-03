package com.tulip.blogspri.service.Impl;

import com.tulip.blogspri.domain.Catalog;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.repository.CatalogRepository;
import com.tulip.blogspri.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService{

    @Autowired
    private CatalogRepository catalogRepository;


    @Override
    public Catalog saveCatalog(Catalog catalog) throws Exception{
        //filter Distinct
        List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(),catalog.getName());
        if(list!=null&&list.size()>0){
            throw  new IllegalAccessException("该分类已存在");
        }
        return catalogRepository.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
        catalogRepository.delete(id);
    }

    @Override
    public Catalog getCatalogById(Long id) {

        return catalogRepository.findOne(id);
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }
}
