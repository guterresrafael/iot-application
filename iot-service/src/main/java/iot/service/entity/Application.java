package iot.service.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.jboss.resteasy.links.RESTServiceDiscovery;

/**
 *
 * @author Rafael Guterres
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Application {

    @XmlAttribute(name = "version")
    private static final String APP_VERSION = "1.0-SNAPSHOT";

    @XmlElementWrapper(name = "links")
    @XmlElement(name = "link")
    private RESTServiceDiscovery rest;

    public String getVersion() {
        return APP_VERSION;
    }
}
