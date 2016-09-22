package iot.domain.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Position.class)
public abstract class Position_ {

	public static volatile SingularAttribute<Position, Boolean> valid;
	public static volatile SingularAttribute<Position, Double> altitude;
	public static volatile SingularAttribute<Position, String> other;
	public static volatile SingularAttribute<Position, String> address;
	public static volatile SingularAttribute<Position, Double> latitude;
	public static volatile SingularAttribute<Position, Double> course;
	public static volatile SingularAttribute<Position, Long> id;
	public static volatile SingularAttribute<Position, Double> power;
	public static volatile SingularAttribute<Position, Date> time;
	public static volatile SingularAttribute<Position, Long> deviceId;
	public static volatile SingularAttribute<Position, Double> speed;
	public static volatile SingularAttribute<Position, Double> longitude;

}

