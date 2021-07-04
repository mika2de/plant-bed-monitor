package de.mika.database.model;

import de.mika.database.model.key.DataPointPk;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="raw_data")
@IdClass(DataPointPk.class)
public class RawData extends AbstractDataPoint {
    @Id
    private Sensor sensor;
    @Id
    private LocalDateTime created;
    private int moisture;
}
