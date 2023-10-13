package ru.diplomatransfermoney.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class TransferAndConfirmResponse {
    @NotBlank(message = "operation id must not be null")
    @Pattern(regexp = "^[0-9]*$")
    private String operationId;
}
