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
import java.util.List;
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
                    List<RawData> pastHourEntries = rawDataRepository.getEntriesOfSensorSince(sensor.getId(), now.minusMinutes(120))
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
