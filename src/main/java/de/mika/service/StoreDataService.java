package de.mika.service;

import de.mika.database.CurrentMoistureRepository;
import de.mika.database.RawDataRepository;
import de.mika.database.SensorRepository;
import de.mika.database.model.CurrentMoisture;
import de.mika.database.model.RawData;
import de.mika.database.model.Sensor;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class StoreDataService {

    @Inject
    SensorRepository sensorRepository;

    @Inject
    RawDataRepository rawDataRepository;

    @Inject
    CurrentMoistureRepository currentMoistureRepository;

    public CurrentMoisture storeData(String macAddress, int moisture){
        LocalDateTime now = LocalDateTime.now();
        Sensor sensor = sensorRepository
                .find("mac = :mac", Parameters.with("mac", macAddress))
                .firstResult();
        RawData rawData = new RawData(sensor, now, moisture);
        rawDataRepository.persist(rawData);

        CurrentMoisture currentMoisture = currentMoistureRepository
                .find("sensor", sensor)
                .firstResult();

        //wenn neuer Tag --> Tageszusammenfassung aus AvgHour, ab Tage-2 löschen
        //wenn neue Stunde --> Stundenzusammenfassung RawData, items löschen
//        if (now.getDayOfYear() > currentMoisture.getUpdated().getDayOfYear())
//        if (now.getHour() > currentMoisture.getUpdated().getHour())

        currentMoisture.setMoisture(moisture);
        currentMoisture.setUpdated(now);
        currentMoistureRepository.persist(currentMoisture);

        return currentMoisture;
    }
}
