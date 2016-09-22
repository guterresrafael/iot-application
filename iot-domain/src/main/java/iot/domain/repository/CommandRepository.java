package iot.domain.repository;

import javax.enterprise.context.ApplicationScoped;
import iot.core.repository.BaseRepository;
import iot.domain.entity.Command;

/**
 *
 * @author Rafael Guterres
 */
@ApplicationScoped
public class CommandRepository extends BaseRepository<Command, Long> {

}
