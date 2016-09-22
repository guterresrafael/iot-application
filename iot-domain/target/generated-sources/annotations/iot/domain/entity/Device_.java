package iot.domain.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Device.class)
public abstract class Device_ {

	public static volatile SingularAttribute<Device, String> password;
	public static volatile ListAttribute<Device, DeviceMeta> metadata;
	public static volatile SingularAttribute<Device, String> phone;
	public static volatile SingularAttribute<Device, String> name;
	public static volatile SingularAttribute<Device, String> imei;
	public static volatile SingularAttribute<Device, Long> id;
	public static volatile SingularAttribute<Device, Position> latestPosition;
	public static volatile ListAttribute<Device, User> users;

}

