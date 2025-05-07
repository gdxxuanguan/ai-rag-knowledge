package com.xuanguan.dev.api.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.bind.annotation.ResponseStatus;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private String code;
    private String info;
    private T data;



}
