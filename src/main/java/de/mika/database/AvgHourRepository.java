package de.mika.database;

import de.mika.database.model.AvgHour;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AvgHourRepository  implements PanacheRepository<AvgHour> {
}
