package iot.domain.repository;

import java.util.Collection;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import iot.core.repository.BaseRepository;
import iot.domain.entity.Device;
import iot.domain.entity.Device_;
import iot.domain.entity.User;

/**
 *
 * @author Rafael Guterres
 */
@ApplicationScoped
public class UserRepository extends BaseRepository<User, Long> {

    public Collection<User> findUsersByDeviceId(Long deviceId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<Device> deviceRoot = criteriaQuery.from(Device.class);
        criteriaQuery.where(criteriaBuilder.equal(deviceRoot.get(Device_.id), deviceId));
        ListJoin<Device, User> users = deviceRoot.join(Device_.users);
        CriteriaQuery<User> cq = criteriaQuery.select(users);
        TypedQuery<User> typedQuery = getEntityManager().createQuery(cq);
        return typedQuery.getResultList();
    }
}
