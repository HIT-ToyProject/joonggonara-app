package com.hit.joonggonara.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;

public record ValidationResponse (
    int httpStatus,
    List<FieldError> fieldErrors
){

    public static ValidationResponse of(
            int httpStatus,
           BindingResult bindingResult
    ){
        return new ValidationResponse(httpStatus, FieldError.create(bindingResult));
    }
}


