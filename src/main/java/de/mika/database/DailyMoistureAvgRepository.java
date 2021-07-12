package de.mika.database;

import de.mika.database.model.DailyMoistureAvg;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DailyMoistureAvgRepository  implements PanacheRepository<DailyMoistureAvg> {

    public DailyMoistureAvg getLastEntry(){
        return findAll(Sort.descending("created")).firstResult();
    }
}
