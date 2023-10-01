package ru.diplomatransfermoney.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmRequest {

    private String operationId;
    private String code;

//    @NotBlank(message = "operation id must not be null")
//    @Pattern(regexp = "^[0-9]*$")
//    private String operationId;
//
//    @NotBlank(message = "code must not be null")
//    private String code;
}
