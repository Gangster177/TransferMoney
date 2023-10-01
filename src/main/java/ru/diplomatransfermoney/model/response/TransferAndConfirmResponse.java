package ru.diplomatransfermoney.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferAndConfirmResponse {

    private String operationId;

//    @NotBlank(message = "operation id must not be null")
//    @Pattern(regexp = "^[0-9]*$")
//    private String operationId;
}
