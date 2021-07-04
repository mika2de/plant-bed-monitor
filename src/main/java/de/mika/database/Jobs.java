package de.mika.database;

import de.mika.database.model.RawData;
import io.quarkus.panache.common.Parameters;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class Jobs {

    @Inject
    RawDataRepository rawDataRepository;

    @Inject
    AvgHourRepository avgHourRepository;

    @Scheduled(every="3660s")
    void avgDay() {
        List<RawData> data = rawDataRepository.find("created < :now", Parameters.with("now", LocalDateTime.now())).list();
        for (RawData item : data) {
//            item.getCreated().get
        }
    }
}
