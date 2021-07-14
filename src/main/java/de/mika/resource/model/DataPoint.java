package de.mika.resource.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class DataPoint {

    @NotNull
    private String mac;
    @NotNull
    private int value;
}
