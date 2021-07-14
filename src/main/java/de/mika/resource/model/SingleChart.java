package de.mika.resource.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SingleChart {

    @NotNull
    private String name;
    @NotNull
    private int value;
}
