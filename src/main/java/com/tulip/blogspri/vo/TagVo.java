package com.tulip.blogspri.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class TagVo implements Serializable{

    private static final long serialVersionUID = 1l;

    private String name;

    private Long count;


}
