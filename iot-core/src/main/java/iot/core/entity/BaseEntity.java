package iot.core.entity;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author Rafael Guterres
 * @param <I>
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class BaseEntity<I> implements Serializable {

    private static final long serialVersionUID = 5285403367012918640L;

    public abstract I getId();

    public abstract void setId(I id);
}
