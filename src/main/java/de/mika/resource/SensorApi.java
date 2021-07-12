package de.mika.resource;

import de.mika.database.*;
import de.mika.database.model.*;
import de.mika.service.LocalDateTimeService;
import de.mika.service.MoistureService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/moisture")
public class SensorApi {

    @Inject
    LocalDateTimeService localDateTimeService;

    @Inject
    RawDataRepository rawDataRepository;

    @Inject
    MoistureService moistureService;

    @Inject
    SensorRepository sensorRepository;

    @Inject
    CurrentMoistureRepository currentMoistureRepository;

    @Inject
    HourlyMoistureAvgRepository hourlyMoistureAvgRepository;

    @Inject
    DailyMoistureAvgRepository dailyMoistureAvgRepository;

    @GET
    @Path("/sensors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors() {
        return sensorRepository.listAll();
    }

    @POST
    @Path("/sensors/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Sensor renameSensor(@PathParam(value = "sensorId") int sensorId, String name){
        Sensor sensor = sensorRepository.findById(Long.valueOf(sensorId));
        sensor.setName(name);
        sensorRepository.persist(sensor);
        return sensor;
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
        return rawDataRepository.getEntriesSince(localDateTimeService.now().minusMinutes(minutes));
    }

    @GET
    @Path("/hourly")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HourlyMoistureAvg> getHourlyAvg(){
        LocalDateTime now = localDateTimeService.now();
        return hourlyMoistureAvgRepository.getEntriesFromToDate(now.minusHours(24).truncatedTo(ChronoUnit.HOURS), now);
    }

    @GET
    @Path("/daily")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DailyMoistureAvg> getDailyAvg(){
        return dailyMoistureAvgRepository.listAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CurrentMoisture storeData(MoistureApiModel moistureApiModel){
        return moistureService.storeData(moistureApiModel.getMac(), moistureApiModel.getMoisture());
    }
}