package de.mika.database.model;

import de.mika.database.model.key.AvgDataPointPk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="avg_day")
@IdClass(AvgDataPointPk.class)
public class DailyMoistureAvg {

    @Id
    private Sensor sensor;
    @Id
    private Date date;
    private int hour;
    private int moisture;
}
