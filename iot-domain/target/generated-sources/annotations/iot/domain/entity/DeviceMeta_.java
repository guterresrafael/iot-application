package iot.domain.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DeviceMeta.class)
public abstract class DeviceMeta_ {

	public static volatile SingularAttribute<DeviceMeta, Long> id;
	public static volatile SingularAttribute<DeviceMeta, Device> device;
	public static volatile SingularAttribute<DeviceMeta, String> value;
	public static volatile SingularAttribute<DeviceMeta, String> key;

}

