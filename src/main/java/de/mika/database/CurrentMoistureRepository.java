package de.mika.database;

import de.mika.database.model.CurrentMoisture;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

@ApplicationScoped
public class CurrentMoistureRepository implements PanacheRepository<CurrentMoisture> {

    public List<CurrentMoisture> getForLastNMinutes(int minutes) {
        return this.find("select cm from CurrentMoisture cm where created > :timestamp",
                Parameters.with("timestamp", LocalDateTime.now().minusMinutes(minutes))).list();
    }
}
