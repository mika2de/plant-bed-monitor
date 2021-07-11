package de.mika.database.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="current")
public class CurrentMoisture implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "mac_id")
    private Sensor sensor;
    private LocalDateTime updated;
    private int moisture;
}
