package de.mika.database.model;

import de.mika.database.model.key.AvgDataPointPk;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@IdClass(AvgDataPointPk.class)
public class AvgHour {

    @Id
    private Sensor sensor;
    @Id
    private Date date;
    private int hour;
    private int moisture;
}
