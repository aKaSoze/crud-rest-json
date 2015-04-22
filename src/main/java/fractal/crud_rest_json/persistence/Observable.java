package fractal.crud_rest_json.persistence;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

public class Observable<T> {

	public static void registerAfterAction(Runnable afterAction, Observable<?>... observables) {
		Sets.newHashSet(observables).forEach(o -> o.registerAfterAction(afterAction));
	}

	private T					value;

	private final Set<Runnable>	afterActions	= new HashSet<>();

	public Observable(T t) {
		this.value = t;
	}

	public T get() {
		return value;
	}

	public void set(T newValue) {
		if (!value.equals(Objects.requireNonNull(newValue))) {
			value = newValue;
			afterActions.forEach(r -> r.run());
		}
	}

	public void registerAfterAction(Runnable afterAction) {
		afterActions.add(afterAction);
	}
}
