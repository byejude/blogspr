package com.tulip.blogspri.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Response {

    private boolean success;
    private String message;
    private Object body;

    public Response(boolean success,String message){
        this.success = success;
        this.message = message;
    }
}
