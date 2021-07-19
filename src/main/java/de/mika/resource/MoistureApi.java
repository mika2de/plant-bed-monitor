package de.mika.resource;

import de.mika.database.CurrentMoistureRepository;
import de.mika.database.DailyMoistureAvgRepository;
import de.mika.database.HourlyMoistureAvgRepository;
import de.mika.database.RawDataRepository;
import de.mika.database.model.CurrentMoisture;
import de.mika.database.model.DailyMoistureAvg;
import de.mika.database.model.RawData;
import de.mika.resource.model.DataPoint;
import de.mika.resource.model.MultiChart;
import de.mika.resource.model.SingleChart;
import de.mika.service.LocalDateTimeService;
import de.mika.service.MoistureService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/moisture")
public class MoistureApi {

    @Inject
    LocalDateTimeService localDateTimeService;

    @Inject
    RawDataRepository rawDataRepository;

    @Inject
    MoistureService moistureService;
    @Inject
    CurrentMoistureRepository currentMoistureRepository;

    @Inject
    HourlyMoistureAvgRepository hourlyMoistureAvgRepository;

    @Inject
    DailyMoistureAvgRepository dailyMoistureAvgRepository;

    @GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SingleChart> getCurrent() {
        return currentMoistureRepository.listAll()
                .stream()
                .sorted(Comparator.comparingInt(m -> m.getSensor().getId().intValue()))
                .map(currentMoisture -> new SingleChart(
                        currentMoisture.getSensor().getName(),
                        currentMoisture.getMoisture()))
                .collect(toList());
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
    @Path("/24h")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MultiChart> getPastHourAvgMoistures(){
        LocalDateTime now = localDateTimeService.now();
        List<MultiChart> multiCharts = new ArrayList<>();
        hourlyMoistureAvgRepository.getEntriesFromToDate(now.minusHours(24).truncatedTo(ChronoUnit.HOURS), now)
                .stream()
                .sorted(Comparator.comparingLong(m -> m.getCreated().toEpochSecond(ZoneOffset.UTC)))
                .forEach(hourlyMoistureAvg -> {
                    MultiChart multiChart = multiCharts
                            .stream()
                            .filter(m -> m.getName() == hourlyMoistureAvg.getSensor().getName())
                            .findFirst()
                            .orElse(new MultiChart(hourlyMoistureAvg.getSensor().getName(), new ArrayList<>()));
                    multiChart.getSeries().add(new SingleChart(
                            String.valueOf(hourlyMoistureAvg.getCreated().getHour()),
                            hourlyMoistureAvg.getAvgMoisture()));
                    multiCharts.add(multiChart);
                });
        return multiCharts;
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
    public CurrentMoisture storeData(@Valid DataPoint dataPoint){
        return moistureService.storeData(dataPoint.getMac(), dataPoint.getValue());
    }
}
