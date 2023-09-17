package com.example.jwtpractice.dto;

import com.example.jwtpractice.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ErrorDto {

    private String code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fieldErrors;

    public ErrorDto(ErrorCode code){
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public ErrorDto(ErrorCode code, BindingResult bindingResult){
        this.code = code.getCode();
        this.message = code.getMessage();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        this.fieldErrors  = fieldErrors.stream().collect(Collectors.toMap(
                FieldError::getField,
                DefaultMessageSourceResolvable::getDefaultMessage
        ));
    }

}
