package iot.domain.service;

import java.util.Collection;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import iot.core.repository.BaseRepository;
import iot.core.service.BaseService;
import iot.domain.entity.User;
import iot.domain.filter.UserFilter;
import iot.domain.repository.UserRepository;

/**
 *
 * @author Rafael Guterres
 */
@ApplicationScoped
public class UserService extends BaseService<User, Long> {

    @Inject
    private UserRepository userRepository;

    @Override
    public BaseRepository<User, Long> getRepository() {
        return userRepository;
    }

    public User findByUsername(String username) {
        UserFilter userFilter = new UserFilter();
        userFilter.setLogin(username);
        Collection<User> users = userRepository.find(userFilter);
        return users.iterator().next();
    }

    public Collection<User> findUsersByDevicesId(Long deviceId) {
        return userRepository.findUsersByDeviceId(deviceId);
    }
}
