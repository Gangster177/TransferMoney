package ru.diplomatransfermoney.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data // (@Getter @Setter)генерирует весь шаблонный код, вовлеченный в работу с объектами POJO
@AllArgsConstructor // будет сгенерирован конструктор с одним параметром для каждого поля вашего класса
public class Amount {
    @Min(0)
    private int value;

    @NotBlank(message = "currency can't be null")
    private String currency;
}
