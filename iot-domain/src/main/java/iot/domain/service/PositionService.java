package iot.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import iot.core.repository.BaseRepository;
import iot.core.service.BaseService;
import iot.domain.entity.Position;
import iot.domain.repository.PositionRepository;

/**
 *
 * @author Rafael Guterres
 */
@ApplicationScoped
public class PositionService extends BaseService<Position, Long> {

    @Inject
    private PositionRepository positionRepository;

    @Override
    public BaseRepository<Position, Long> getRepository() {
        return positionRepository;
    }

    public Position getPositionToImport() {
        return null;
    }
}
