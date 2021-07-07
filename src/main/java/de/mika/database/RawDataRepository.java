package de.mika.database;

import de.mika.database.model.RawData;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class RawDataRepository implements PanacheRepository<RawData> {

    @Override
    public void persist(RawData rawData) {
        this.persist(rawData);
    }

    public List<RawData> getEntriesSince(LocalDateTime timestamp) {
        return this.find("select cm from RawData cm where created > :timestamp",
                Parameters.with("timestamp", timestamp)).list();
    }
}
