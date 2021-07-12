package de.mika.service;

import de.mika.database.DailyMoistureAvgRepository;
import de.mika.database.HourlyMoistureAvgRepository;
import de.mika.database.RawDataRepository;
import de.mika.database.SensorRepository;
import de.mika.database.model.DailyMoistureAvg;
import de.mika.database.model.HourlyMoistureAvg;
import de.mika.database.model.RawData;
import de.mika.database.model.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StagingServiceTest {

    @Mock
    LocalDateTimeService localDateTimeService;

    @Mock
    SensorRepository sensorRepository;

    @Mock
    RawDataRepository rawDataRepository;

    @Mock
    HourlyMoistureAvgRepository hourlyMoistureAvgRepository;

    @Mock
    DailyMoistureAvgRepository dailyMoistureAvgRepository;

    Sensor mockedSensor;
    LocalDateTime mockedNow;

    StagingService stagingService;

    @BeforeEach
    void initSensorRepositoryMock() {

        mockedSensor = new Sensor(1L, String.format("mock-sensor"), "AA:BB:CC:DD:EE:FF");
        List<Sensor> mockedSensorList = new ArrayList<>();
        mockedSensorList.add(mockedSensor);

        when(sensorRepository.listAll()).thenReturn(mockedSensorList);
        when(localDateTimeService.now()).thenReturn(LocalDateTime.of(2021, 07, 27, 00, 00, 00, 000000));

        mockedNow = localDateTimeService.now();

        stagingService = new StagingService(
                localDateTimeService,
                sensorRepository,
                rawDataRepository,
                hourlyMoistureAvgRepository,
                dailyMoistureAvgRepository);
    }

    @Test
    void testUpdateAvgHour() {
        List<RawData> mockedRawDataList = new ArrayList<>();
        List<RawData> mockedRawDataToBeStaged = new ArrayList<>();
        List<HourlyMoistureAvg> mockedHourlyMoistureAvgList = new ArrayList<>();

        for (int r = 0; r < 10; r++) {
            RawData rawData = new RawData(mockedSensor, mockedNow.minusHours(1).plusMinutes(r * 10), r);
            mockedRawDataList.add(rawData);
            if (rawData.getCreated().getHour() == mockedNow.minusHours(1).getHour()){
                mockedRawDataToBeStaged.add(rawData);
            }
        }
        mockedHourlyMoistureAvgList.add(new HourlyMoistureAvg(
                mockedSensor,
                mockedNow.minusHours(1),
                mockedRawDataToBeStaged
                        .stream()
                        .mapToInt(RawData::getMoisture).sum() / mockedRawDataToBeStaged.size())
        );

        when(rawDataRepository.getEntriesOfSensorBefore(anyLong(), any(LocalDateTime.class))).thenReturn(mockedRawDataList);

        stagingService.updateAvgHour();

        mockedHourlyMoistureAvgList.forEach(mockedHourlyMoistureAvg -> {
            verify(hourlyMoistureAvgRepository).persist(mockedHourlyMoistureAvg);
        });
        mockedRawDataToBeStaged.forEach(mockedRawData -> verify(rawDataRepository).delete(mockedRawData));
    }

    @Test
    public void testUpdateAvgDay() {
        List<HourlyMoistureAvg> mockedHourlyMoistureAvgList = new ArrayList<>();
        int avgDay1 = 0;
        int avgDay2 = 0;
        for (int i=0; i<48; i++) {
            mockedHourlyMoistureAvgList.add(new HourlyMoistureAvg(mockedSensor, mockedNow.minusDays(2).plusHours(i), i));
            if (i<24){
                avgDay1 += i;
            } else {
                avgDay2 += i;
            }
        }
        avgDay1 = avgDay1 / 24;
        avgDay2 = avgDay2 / 24;

        when(dailyMoistureAvgRepository.getLastEntry()).thenReturn(null);
        when(hourlyMoistureAvgRepository.getEntriesFromToDate(any(), any())).thenReturn(mockedHourlyMoistureAvgList);

        stagingService.updateAvgDay();

        verify(dailyMoistureAvgRepository).persist(new DailyMoistureAvg(mockedSensor, mockedNow.minusDays(2), avgDay1));
        verify(dailyMoistureAvgRepository).persist(new DailyMoistureAvg(mockedSensor, mockedNow.minusDays(1), avgDay2));
    }
}