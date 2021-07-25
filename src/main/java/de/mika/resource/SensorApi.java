package de.mika.resource;

import de.mika.database.*;
import de.mika.database.model.*;
import de.mika.resource.model.DataPoint;
import de.mika.resource.model.MultiChart;
import de.mika.resource.model.SingleChart;
import de.mika.service.LocalDateTimeService;
import de.mika.service.MoistureService;
import io.smallrye.common.constraint.NotNull;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Path("/sensors")
public class SensorApi {
    Logger logger = Logger.getLogger(SensorApi.class);

    @Inject
    SensorRepository sensorRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors() {
        return sensorRepository.listAll().
                stream()
                .sorted(Comparator.comparingInt(m -> m.getId().intValue()))
                .collect(toList());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Sensor> storeSensors(List<Sensor> sensorList) {
        logger.infof("POST sensors %s", sensorList.size());
        List<Sensor> updatedSensorList = new ArrayList<>();
        List<Sensor> databaseSensorList = sensorRepository.listAll().
                stream()
                .sorted(Comparator.comparingInt(m -> m.getId().intValue()))
                .collect(toList());
        sensorList.forEach(sensor -> {
            Sensor updatedSensor = databaseSensorList.stream().filter(dbSensor -> dbSensor.getId() == sensor.getId()).findFirst().get();
            updatedSensor.setName(sensor.getName());
            sensorRepository.persist(updatedSensor);
            updatedSensorList.add(updatedSensor);
            logger.infof("updated sensor %s, new name = %s", updatedSensor.getId(), updatedSensor.getName());
        });
        return updatedSensorList;
    }

//    @POST
//    @Path("/{sensorId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Sensor renameSensor(@PathParam(value = "sensorId") int sensorId, @NotNull String name){
//        Sensor sensor = sensorRepository.findById(Long.valueOf(sensorId));
//        sensor.setName(name);
//        sensorRepository.persist(sensor);
//        return sensor;
//    }
}