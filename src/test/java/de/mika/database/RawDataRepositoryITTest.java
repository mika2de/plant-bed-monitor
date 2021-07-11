package de.mika.database;

import de.mika.database.model.RawData;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@QuarkusTest
class RawDataRepositoryITTest {

    @Inject
    SensorRepository sensorRepository;

    @Inject
    RawDataRepository rawDataRepository;

    int datapointsPerSensor = 2;

    @BeforeEach
    @Transactional
    void initRawData(){
        List<RawData> rawDataList = new ArrayList<>();
        sensorRepository.listAll().forEach(sensor -> {
            LocalDateTime now = LocalDateTime.now();
            for (int i = 0; i < datapointsPerSensor; i++) {
                rawDataList.add(new RawData(sensor, now.plusMinutes(i*10),new Random().nextInt(101)));
            }
        });
        rawDataRepository.persist(rawDataList);
    }

    @AfterEach
    @Transactional
    void cleanupRawData(){
        rawDataRepository.deleteAll();
    }

    @Test
    void testGetEntriesSince() {
        List<RawData> rawDataList = rawDataRepository.getEntriesSince(LocalDateTime.now().minusYears(1));
        Assertions.assertEquals(10*datapointsPerSensor, rawDataList.size());
    }

    @Test
    void testGetEntriesOfSensorSince() {
        List<RawData> rawDataList = rawDataRepository.getEntriesOfSensorAfter(1L
                , LocalDateTime.now().minusYears(1));
        Assertions.assertEquals(datapointsPerSensor, rawDataList.size());
    }

    @Test
    void testGetEntriesOfTomorrow() {
        List<RawData> rawDataList = rawDataRepository.getEntriesSince(LocalDateTime.now().plusDays(1));
        Assertions.assertEquals(0, rawDataList.size());
    }
}