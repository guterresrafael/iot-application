package iot.service;

import javax.annotation.PostConstruct;
import iot.service.path.ApplicationPath;

/**
 *
 * @author Rafael Guterres
 */
@javax.ws.rs.ApplicationPath(ApplicationPath.API)
public class Application extends javax.ws.rs.core.Application {

    @PostConstruct
    protected void init() {
        //TODO: implementar annotation para agendamento automatico
        //scheduler.addJob(new PositionJob());
        //scheduler.scheduleJobs();
    }
}
