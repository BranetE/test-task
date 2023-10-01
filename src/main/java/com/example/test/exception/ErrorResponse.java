package com.example.test.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ErrorResponse {
    private Integer status;
    private String message;
}
