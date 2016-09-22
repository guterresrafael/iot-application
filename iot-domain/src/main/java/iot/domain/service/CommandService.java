package iot.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import iot.core.repository.BaseRepository;
import iot.core.service.BaseService;
import iot.domain.entity.Command;
import iot.domain.repository.CommandRepository;

/**
 *
 * @author Rafael Guterres
 */
@ApplicationScoped
public class CommandService extends BaseService<Command, Long> {

    @Inject
    private CommandRepository commandRepository;

    @Override
    public BaseRepository<Command, Long> getRepository() {
        return commandRepository;
    }
}
