package fractal.crud_rest_json.persistence;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface Repository<T extends Identifiable<K>, K> {

	Optional<T> get(K id);

	Set<T> getAll();

	Set<T> filter(Predicate<T> filter);

	Optional<T> yield(Predicate<T> filter);

	LinkedHashSet<T> getAll(Comparator<T> sorter);

	Set<T> getAll(Collection<K> ids);

	void save(T t);

	void saveAll(Collection<T> ts);

	void delete(K id);

	void deleteAll(Collection<T> ts);

	void deleteAllById(Collection<K> ids);

	Set<K> toIds(Collection<T> ts);
}
