package de.mika.database.model;

import de.mika.database.model.key.DataPointPk;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@IdClass(DataPointPk.class)
public abstract class AbstractDataPoint {
    @Id
    private Sensor sensor;
    @Id
    private LocalDateTime created;
    private int moisture;
}
