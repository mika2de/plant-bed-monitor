package de.mika.database;

import de.mika.database.model.RawData;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class RawDataRepository implements PanacheRepository<RawData> {

    public List<RawData> getForLastNMinutes(int minutes) {
        return this.find("select cm from RawData cm where created > :timestamp",
                Parameters.with("timestamp", LocalDateTime.now().minusMinutes(minutes))).list();
    }
}
