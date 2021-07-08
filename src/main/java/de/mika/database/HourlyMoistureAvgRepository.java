package de.mika.database;

import de.mika.database.model.HourlyMoistureAvg;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HourlyMoistureAvgRepository implements PanacheRepository<HourlyMoistureAvg> {
}
