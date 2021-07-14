package de.mika.resource.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultiChart {

    private String name;
    List<SingleChart> series;
}
