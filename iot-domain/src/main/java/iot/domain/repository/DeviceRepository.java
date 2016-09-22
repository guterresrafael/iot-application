package iot.domain.repository;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import iot.core.repository.BaseRepository;
import iot.domain.entity.Device;
import iot.domain.entity.User;
import iot.domain.entity.User_;

/**
 *
 * @author Rafael Guterres
 */
@ApplicationScoped
public class DeviceRepository extends BaseRepository<Device, Long> {

    public List<Device> findDevicesByUserId(Long userId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Device> criteriaQuery = criteriaBuilder.createQuery(Device.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(userRoot.get(User_.id), userId));
        ListJoin<User, Device> users = userRoot.join(User_.devices);
        CriteriaQuery<Device> cq = criteriaQuery.select(users);
        TypedQuery<Device> typedQuery = getEntityManager().createQuery(cq);
        return typedQuery.getResultList();
    }
}
