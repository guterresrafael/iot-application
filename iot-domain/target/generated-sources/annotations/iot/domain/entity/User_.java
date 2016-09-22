package iot.domain.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> password;
	public static volatile ListAttribute<User, UserMeta> metadata;
	public static volatile SingularAttribute<User, String> phone;
	public static volatile ListAttribute<User, Device> devices;
	public static volatile ListAttribute<User, Role> roles;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> login;
	public static volatile SingularAttribute<User, String> email;

}

