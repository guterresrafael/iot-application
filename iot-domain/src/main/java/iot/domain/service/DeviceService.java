package iot.domain.service;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import iot.core.repository.BaseRepository;
import iot.core.service.BaseService;
import iot.domain.entity.Device;
import iot.domain.repository.DeviceRepository;

/**
 * @author Rafael Guterres
 */
@ApplicationScoped
public class DeviceService extends BaseService<Device, Long> {

    @Inject
    private DeviceRepository deviceRepository;

    @Override
    public BaseRepository<Device, Long> getRepository() {
        return deviceRepository;
    }

    public List<Device> findDevicesByUserId(Long userId) {
        return deviceRepository.findDevicesByUserId(userId);
    }
}
