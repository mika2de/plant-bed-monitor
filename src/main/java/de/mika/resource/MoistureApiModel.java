package de.mika.resource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class MoistureApiModel {

    private String name;
    @NotNull
    private String mac;
    @NotNull
    private int value;
}
