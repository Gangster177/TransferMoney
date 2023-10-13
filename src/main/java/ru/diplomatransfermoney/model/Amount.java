package ru.diplomatransfermoney.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Amount {
    @Min(0)
    private int value;

    @NotBlank(message = "currency can't be null")
    private String currency;
}
