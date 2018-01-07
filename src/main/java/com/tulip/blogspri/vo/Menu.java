package com.tulip.blogspri.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Menu implements Serializable {

    private static final long serialVersion = 1L;

    private String name;
    private String url;

}
