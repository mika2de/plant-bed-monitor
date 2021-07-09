package de.mika.database;

import de.mika.database.model.CurrentMoisture;
import de.mika.service.LocalDateTimeService;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CurrentMoistureRepository implements PanacheRepository<CurrentMoisture> {

    @Inject
    LocalDateTimeService localDateTimeService;


    public List<CurrentMoisture> getForLastNMinutes(int minutes) {
        return this.find("select cm from CurrentMoisture cm where created > :timestamp",
                Parameters.with("timestamp", localDateTimeService.now().minusMinutes(minutes))).list();
    }
}
