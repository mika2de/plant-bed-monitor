package de.mika.database.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Sensor {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String mac;

}
