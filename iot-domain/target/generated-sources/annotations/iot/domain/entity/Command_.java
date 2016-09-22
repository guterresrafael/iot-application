package iot.domain.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Command.class)
public abstract class Command_ {

	public static volatile SingularAttribute<Command, Date> queued;
	public static volatile SingularAttribute<Command, Date> executed;
	public static volatile SingularAttribute<Command, Long> id;
	public static volatile SingularAttribute<Command, Device> device;
	public static volatile SingularAttribute<Command, String> commands;

}

