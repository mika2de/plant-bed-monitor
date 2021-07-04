package de.mika.database.model.key;

import de.mika.database.model.Sensor;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Date;

@Data
@Embeddable
public class AvgDataPointPk implements Serializable {
    @ManyToOne
    @JoinColumn(name = "mac_id")
    private Sensor sensor;
    private Date date;
}
