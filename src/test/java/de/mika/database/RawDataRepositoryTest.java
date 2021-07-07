package de.mika.database;

import de.mika.database.model.RawData;
import de.mika.database.model.Sensor;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RawDataRepositoryTest {

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
    void testGetEntriesSinceLastYear() {
        List<RawData> rawDataList = rawDataRepository.getEntriesSince(LocalDateTime.now().minusYears(1));
        Assertions.assertEquals(10*datapointsPerSensor, rawDataList.size());
    }

    @Test
    void testGetEntriesOfTomorrow() {
        List<RawData> rawDataList = rawDataRepository.getEntriesSince(LocalDateTime.now().plusDays(1));
        Assertions.assertEquals(0, rawDataList.size());
    }
}