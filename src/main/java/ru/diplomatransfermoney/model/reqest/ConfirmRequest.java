package ru.diplomatransfermoney.model.reqest;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class ConfirmRequest {
    @NotBlank(message = "operation id must not be null")
    @Pattern(regexp = "^[0-9]*$")
    private String operationId;

    @NotBlank(message = "code must not be null")
    private String code;
}
