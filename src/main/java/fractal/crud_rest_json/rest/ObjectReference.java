package fractal.crud_rest_json.rest;

import java.util.Optional;

import fractal.crud_rest_json.persistence.IdentifiableBean;
import fractal.crud_rest_json.rest.Either.Left;
import fractal.crud_rest_json.rest.Either.Right;

public class ObjectReference {

	public final Either<Class<? extends IdentifiableBean>, String>	entityType;
	public final Optional<String>									key;

	public ObjectReference(Either<Class<? extends IdentifiableBean>, String> entityType) {
		this.entityType = entityType;
		this.key = Optional.empty();
	}

	public ObjectReference(Either<Class<? extends IdentifiableBean>, String> entityType, String key) {
		this.entityType = entityType;
		this.key = Optional.of(key);
	}

	public ObjectReference(Class<? extends IdentifiableBean> objectClass) {
		this(new Left<>(objectClass));
	}

	public ObjectReference(Class<? extends IdentifiableBean> objectClass, String key) {
		this(new Left<>(objectClass), key);
	}

	public ObjectReference(String objectClassName) {
		this(new Right<>(objectClassName));
	}

	public ObjectReference(String objectClassName, String key) {
		this(new Right<>(objectClassName), key);
	}

	public String getName() {
		return entityType.unify(clazz -> clazz.getSimpleName(), className -> className);
	}

	@Override
	public String toString() {
		return "ObjectReference [entityType=" + entityType + ", key=" + key + "]";
	}

}
