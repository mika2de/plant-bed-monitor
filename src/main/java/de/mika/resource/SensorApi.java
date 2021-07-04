package de.mika.resource;

import de.mika.database.CurrentMoistureRepository;
import de.mika.database.RawDataRepository;
import de.mika.database.SensorRepository;
import de.mika.database.model.*;
import de.mika.resource.model.SensorData;
import io.quarkus.panache.common.Parameters;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;

@Path("/moisture")
public class SensorApi {
    @Inject
    RawDataRepository rawDataRepository;

    @Inject
    CurrentMoistureRepository currentMoistureRepository;

    @Inject
    SensorRepository sensorRepository;

    @GET
    @Path("/sensors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors() {
        return sensorRepository.listAll();
    }

    @GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CurrentMoisture> getCurrent() {
        return currentMoistureRepository.listAll();
    }

    @GET
    @Path("/raw")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawData> getRawDataForLastNMinutes(@QueryParam("minutes")  int minutes){
        if (minutes==0)
            return rawDataRepository.listAll();
        return rawDataRepository.getForLastNMinutes(minutes);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public RawData saveData(SensorData sensorData){
        LocalDateTime now = LocalDateTime.now();
        Sensor sensor = sensorRepository
                .find("mac = :mac", Parameters.with("mac", sensorData.getMac()))
                .firstResult();
        RawData rawData = new RawData();
        rawData.setSensor(sensor);
        rawData.setCreated(now);
        rawData.setMoisture(sensorData.getMoisture());
        rawDataRepository.persist(rawData);

        CurrentMoisture currentMoisture = currentMoistureRepository
                .find("sensor", sensor)
                .firstResult();
        currentMoisture.setMoisture(sensorData.getMoisture());
        currentMoisture.setUpdated(now);
        currentMoistureRepository.persist(currentMoisture);

        return rawData;
    }
}