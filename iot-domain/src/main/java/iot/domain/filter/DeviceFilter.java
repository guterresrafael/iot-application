package iot.domain.filter;

import iot.core.repository.Filter;
import iot.core.repository.CriteriaMethod;

/**
 *
 * @author Rafael Guterres
 */
public class DeviceFilter implements Filter {

    private static final long serialVersionUID = -2816539253666022760L;

    @CriteriaMethod
    private Long id;

    @CriteriaMethod
    private String name;

    @CriteriaMethod
    private String imei;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
