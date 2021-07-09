package de.mika.service;

import de.mika.database.HourlyMoistureAvgRepository;
import de.mika.database.RawDataRepository;
import de.mika.database.SensorRepository;
import de.mika.database.model.HourlyMoistureAvg;
import de.mika.database.model.RawData;
import de.mika.database.model.Sensor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StagingServiceTest {

    @Mock
    LocalDateTimeService localDateTimeServiceMock;

    @Mock
    SensorRepository sensorRepositoryMock;

    @Mock
    RawDataRepository rawDataRepositoryMock;

    @Mock
    HourlyMoistureAvgRepository hourlyMoistureAvgRepositoryMock;

    @Test
    void updateAvgHour() {
        when(localDateTimeServiceMock.now()).thenReturn(LocalDateTime.of(2021, 07, 27, 12, 00, 00, 000000));
        LocalDateTime mockedNow = localDateTimeServiceMock.now();

        List<HourlyMoistureAvg> mockedHourlyMoistureAvgList = new ArrayList<>();

        Sensor mockedSensor = new Sensor(1L, String.format("mock-sensor"), "AA:BB:CC:DD:EE:FF");
        List<Sensor> mockedSensorList = new ArrayList<>();
        mockedSensorList.add(mockedSensor);

        List<RawData> mockedRawDataList = new ArrayList<>();
        List<RawData> mockedRawDataToBeStaged = new ArrayList<>();

        for (int r = 0; r < 10; r++) {
            RawData rawData = new RawData(mockedSensor, mockedNow.minusHours(1).plusMinutes(r * 10), r);
            mockedRawDataList.add(rawData);
            System.out.println(rawData);
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

        when(sensorRepositoryMock.listAll()).thenReturn(mockedSensorList);
        when(rawDataRepositoryMock.getEntriesOfSensorBefore(anyLong(), any(LocalDateTime.class))).thenReturn(mockedRawDataList);

        StagingService stagingService = new StagingService(localDateTimeServiceMock,
                sensorRepositoryMock,
                rawDataRepositoryMock,
                hourlyMoistureAvgRepositoryMock);
        stagingService.updateAvgHour();

        mockedHourlyMoistureAvgList.forEach(mockedHourlyMoistureAvg -> {
            verify(hourlyMoistureAvgRepositoryMock).persist(mockedHourlyMoistureAvg);
        });
        mockedRawDataToBeStaged.forEach(mockedRawData -> verify(rawDataRepositoryMock).delete(mockedRawData));
    }
}