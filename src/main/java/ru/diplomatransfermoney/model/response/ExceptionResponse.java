package ru.diplomatransfermoney.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    private String errorMessage;
    private int id;
}
