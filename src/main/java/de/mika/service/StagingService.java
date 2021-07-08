package de.mika.service;

import de.mika.database.HourlyMoistureAvgRepository;
import de.mika.database.RawDataRepository;
import de.mika.database.SensorRepository;
import de.mika.database.model.HourlyMoistureAvg;
import de.mika.database.model.RawData;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StagingService {

    @Inject
    SensorRepository sensorRepository;

    @Inject
    RawDataRepository rawDataRepository;

    @Inject
    HourlyMoistureAvgRepository hourlyMoistureAvgRepository;

    @Scheduled(cron = "0 0 * ? * *")
    @Transactional
    public void updateAvgHour() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.minusHours(1).getHour();
        sensorRepository.listAll()
                .forEach(sensor -> {
                    List<RawData> pastHourEntries = rawDataRepository.getEntriesSince(now.minusMinutes(120))
                            .stream()
                            .filter(rawData -> rawData.getCreated().getHour() == hour)
                            .collect(Collectors.toList());

                    int avgMoisture = pastHourEntries
                            .stream()
                            .mapToInt(RawData::getMoisture).sum() / pastHourEntries.size();

                    HourlyMoistureAvg hourlyMoistureAvg = new HourlyMoistureAvg(
                            sensor,
                            Date.valueOf(now.toLocalDate()),
                            hour,
                            avgMoisture);

                    hourlyMoistureAvgRepository.persist(hourlyMoistureAvg);
                    pastHourEntries.forEach(rawData -> rawDataRepository.delete(rawData));
                });
    }
}
