package de.mika.resource;

import de.mika.database.CurrentMoistureRepository;
import de.mika.database.RawDataRepository;
import de.mika.database.SensorRepository;
import de.mika.database.model.*;
import de.mika.resource.model.SensorData;
import de.mika.service.StoreDataService;
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
    StoreDataService storeDataService;

    @Inject
    SensorRepository sensorRepository;

    @Inject
    CurrentMoistureRepository currentMoistureRepository;

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
        return rawDataRepository.getEntriesSince(LocalDateTime.now().minusMinutes(minutes));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CurrentMoisture storeData(SensorData sensorData){
        return storeDataService.storeData(sensorData.getMac(), sensorData.getMoisture());
    }

    void updateAvgHour(int moisture, Sensor sensor){

    }
}