package de.mika.database.model;

import de.mika.database.model.key.AvgDataPointPk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="avg_hour")
@IdClass(AvgDataPointPk.class)
public class HourlyMoistureAvg {

    @Id
    private Sensor sensor;
    @Id
    private LocalDateTime created;
    @Column(name="avg_moisture")
    private int avgMoisture;
}
