package com.tulip.blogspri.vo;

import com.tulip.blogspri.domain.Catalog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CatalogVo implements Serializable {

    private static final Long serialVersion = 1L;

    private String username;
    private Catalog catalog;

}
