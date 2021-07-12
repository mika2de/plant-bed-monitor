package de.mika.service;

import de.mika.database.*;
import de.mika.database.model.CurrentMoisture;
import de.mika.database.model.RawData;
import de.mika.database.model.Sensor;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class MoistureService {

    @Inject
    LocalDateTimeService localDateTimeService;

    @Inject
    SensorRepository sensorRepository;

    @Inject
    CurrentMoistureRepository currentMoistureRepository;

    @Inject
    RawDataRepository rawDataRepository;

    public CurrentMoisture storeData(String macAddress, int moisture){
        LocalDateTime now = localDateTimeService.now();
        Sensor sensor = sensorRepository
                .find("mac = :mac", Parameters.with("mac", macAddress))
                .firstResult();
        RawData rawData = new RawData(sensor, now, moisture);
        rawDataRepository.persist(rawData);

        CurrentMoisture currentMoisture = currentMoistureRepository
                .find("sensor", sensor)
                .firstResult();

        currentMoisture.setMoisture(moisture);
        currentMoisture.setUpdated(now);
        currentMoistureRepository.persist(currentMoisture);

        return currentMoisture;
    }
}
