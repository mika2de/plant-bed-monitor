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
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class StagingService {

    LocalDateTimeService localDateTimeService;
    SensorRepository sensorRepository;
    RawDataRepository rawDataRepository;
    HourlyMoistureAvgRepository hourlyMoistureAvgRepository;

    @Inject
    public StagingService(LocalDateTimeService localDateTimeService,
                          SensorRepository sensorRepository,
                          RawDataRepository rawDataRepository,
                          HourlyMoistureAvgRepository hourlyMoistureAvgRepository) {
        this.localDateTimeService = localDateTimeService;
        this.sensorRepository = sensorRepository;
        this.rawDataRepository = rawDataRepository;
        this.hourlyMoistureAvgRepository = hourlyMoistureAvgRepository;
    }

    @Scheduled(cron = "0 0 * ? * *")
    @Transactional
    public void updateAvgHour() {
        System.out.println("+++ updateAvgHour +++");
        LocalDateTime now = localDateTimeService.now();
        int hour = now.minusHours(1).getHour();
        sensorRepository.listAll()
                .forEach(sensor -> {
                    List<RawData> unprocessedEntries = rawDataRepository.getEntriesOfSensorBefore(sensor.getId(), now.minusHours(1));
                    Map<LocalDateTime, List<RawData>> rawDataPerYearMonthDayHourInterval = unprocessedEntries
                            .stream()
                            .collect(Collectors.groupingBy(
                                    rawData -> rawData.getCreated().truncatedTo(ChronoUnit.HOURS),
                                    Collectors.toCollection(ArrayList::new)
                                    ));
                    for (Map.Entry<LocalDateTime, List<RawData>> entry : rawDataPerYearMonthDayHourInterval.entrySet()) {
                        int avgMoisture = entry.getValue().stream().mapToInt(RawData::getMoisture).sum() / entry.getValue().size();
                        HourlyMoistureAvg hourlyMoistureAvg = new HourlyMoistureAvg(
                                sensor,
                                entry.getKey(),
                                avgMoisture);

                        hourlyMoistureAvgRepository.persist(hourlyMoistureAvg);
                        entry.getValue().forEach(rawData -> rawDataRepository.delete(rawData));
                    }
                });
    }
}
