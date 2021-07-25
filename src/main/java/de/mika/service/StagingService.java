package de.mika.service;

import de.mika.database.DailyMoistureAvgRepository;
import de.mika.database.HourlyMoistureAvgRepository;
import de.mika.database.RawDataRepository;
import de.mika.database.SensorRepository;
import de.mika.database.model.DailyMoistureAvg;
import de.mika.database.model.HourlyMoistureAvg;
import de.mika.database.model.RawData;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class StagingService {

    Logger logger = Logger.getLogger(StagingService.class);

    LocalDateTimeService localDateTimeService;
    SensorRepository sensorRepository;
    RawDataRepository rawDataRepository;
    HourlyMoistureAvgRepository hourlyMoistureAvgRepository;
    DailyMoistureAvgRepository dailyMoistureAvgRepository;

    @Inject
    public StagingService(LocalDateTimeService localDateTimeService,
                          SensorRepository sensorRepository,
                          RawDataRepository rawDataRepository,
                          HourlyMoistureAvgRepository hourlyMoistureAvgRepository,
                          DailyMoistureAvgRepository dailyMoistureAvgRepository) {
        this.localDateTimeService = localDateTimeService;
        this.sensorRepository = sensorRepository;
        this.rawDataRepository = rawDataRepository;
        this.hourlyMoistureAvgRepository = hourlyMoistureAvgRepository;
        this.dailyMoistureAvgRepository = dailyMoistureAvgRepository;
    }

    @Scheduled(cron = "0 0 * ? * *")
    @Transactional
    public void updateAvgHour() {
        logger.info("start updateAvgHour");
        LocalDateTime now = localDateTimeService.now();
        sensorRepository
                .listAll()
                .forEach(sensor -> {
                    List<RawData> unprocessedEntries = rawDataRepository.getBySensorIdBeforeTs(sensor.getId(), now.truncatedTo(ChronoUnit.HOURS));
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

    @Scheduled(cron = "0 5 1 ? * *")
    @Transactional
    public void updateAvgDay() {
        System.out.println("--- updateAvgDay ---");
        DailyMoistureAvg lastEntry = dailyMoistureAvgRepository.getLastEntry();
        LocalDateTime lastProcessedDayEntryDate = lastEntry != null ? lastEntry.getCreated() : LocalDateTime.of(2000, 1, 1, 0, 0);

        LocalDateTime nextDateToBeProcess = lastProcessedDayEntryDate.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime lastPossibleEntryYesterday = localDateTimeService.now().truncatedTo(ChronoUnit.DAYS)
                .minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);
        sensorRepository
                .listAll()
                .forEach(sensor -> {
                    List<HourlyMoistureAvg> unprocessedEntries = hourlyMoistureAvgRepository.getEntriesFromToDate(nextDateToBeProcess, lastPossibleEntryYesterday);
                    System.out.println("++++" + unprocessedEntries.size());
                    Map<LocalDateTime, List<HourlyMoistureAvg>> rawDataPerYearMonthDayHourInterval = unprocessedEntries
                            .stream()
                            .collect(Collectors.groupingBy(
                                    hourlyMoistureAvg -> hourlyMoistureAvg.getCreated().truncatedTo(ChronoUnit.DAYS),
                                    Collectors.toCollection(ArrayList::new)
                            ));

                    for (Map.Entry<LocalDateTime, List<HourlyMoistureAvg>> entry : rawDataPerYearMonthDayHourInterval.entrySet()) {
                        int avgMoisture = entry.getValue().stream().mapToInt(HourlyMoistureAvg::getAvgMoisture).sum() / entry.getValue().size();
                        DailyMoistureAvg dailyMoistureAvg = new DailyMoistureAvg(
                                sensor,
                                entry.getKey(),
                                avgMoisture);
                       dailyMoistureAvgRepository.persist(dailyMoistureAvg);
                    }
                });
    }
}
