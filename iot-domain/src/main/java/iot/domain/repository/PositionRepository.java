package iot.domain.repository;

import javax.enterprise.context.ApplicationScoped;
import iot.core.repository.BaseRepository;
import iot.domain.entity.Position;

/**
 *
 * @author Rafael Guterres
 */
@ApplicationScoped
public class PositionRepository extends BaseRepository<Position, Long> {

}
