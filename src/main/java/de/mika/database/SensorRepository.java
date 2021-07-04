package de.mika.database;

import de.mika.database.model.Sensor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SensorRepository implements PanacheRepository<Sensor> {

}
