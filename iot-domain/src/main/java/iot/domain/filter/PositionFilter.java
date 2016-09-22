package iot.domain.filter;

import iot.core.repository.Filter;
import iot.core.repository.CriteriaMethod;

/**
 *
 * @author Rafael Guterres
 */
public class PositionFilter implements Filter {

    private static final long serialVersionUID = 2541133153834424892L;

    @CriteriaMethod
    private Long deviceId;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}
