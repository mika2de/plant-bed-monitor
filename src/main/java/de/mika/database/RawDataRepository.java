package de.mika.database;

import de.mika.database.model.RawData;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class RawDataRepository implements PanacheRepository<RawData> {

    public List<RawData> getEntriesSince(LocalDateTime timestamp) {
        return find("created > :timestamp",
                Parameters.with("timestamp", timestamp))
                .list();
    }

    public List<RawData> getBySensorIdBeforeTs(long sensorId, LocalDateTime timestamp) {
        return this.find("sensor.id = :sensorId and created < :timestamp",
                Parameters.with("sensorId", sensorId).and("timestamp", timestamp))
                .list();
    }

    public List<RawData> getEntriesOfSensorAfter(long sensorId, LocalDateTime timestamp) {
        return this.find("sensor.id = :sensorId and created > :timestamp",
                Parameters.with("sensorId", sensorId).and("timestamp", timestamp))
                .list();
    }
}
