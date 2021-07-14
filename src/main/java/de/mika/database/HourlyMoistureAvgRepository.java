package de.mika.database;

import de.mika.database.model.HourlyMoistureAvg;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import net.bytebuddy.asm.Advice;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class HourlyMoistureAvgRepository implements PanacheRepository<HourlyMoistureAvg> {

    public List<HourlyMoistureAvg> getEntriesForDay(int year, int month, int day){
        LocalDateTime lowerBound = LocalDateTime.of(year, month, day, 0, 0);
        LocalDateTime upperBound = LocalDateTime.of(year, month, day, 23, 59);
        return getEntriesFromToDate(lowerBound, upperBound);
    }

    public List<HourlyMoistureAvg> getEntriesFromToDate(LocalDateTime fromDate, LocalDateTime toDate){
        System.out.println(fromDate);
        System.out.println(toDate);
        return find("created >= :lowerBound and created <= :upperBound",
                Parameters.with("lowerBound", fromDate).and("upperBound", toDate))
                .list();
    }
}
