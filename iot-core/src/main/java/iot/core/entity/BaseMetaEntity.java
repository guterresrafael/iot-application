package iot.core.entity;

import java.io.Serializable;

/**
 *
 * @author Rafael Guterres
 */
public abstract class BaseMetaEntity implements Serializable {

    private static final long serialVersionUID = 8731467211883576183L;

    public abstract String getKey();

    public abstract void setKey(String key);

    public abstract String getValue();

    public abstract void setValue(String value);
}
