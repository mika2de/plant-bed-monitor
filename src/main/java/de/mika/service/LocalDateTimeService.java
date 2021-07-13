package de.mika.service;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class LocalDateTimeService {

    public LocalDateTime now(){
        return LocalDateTime.now();
    }
}
