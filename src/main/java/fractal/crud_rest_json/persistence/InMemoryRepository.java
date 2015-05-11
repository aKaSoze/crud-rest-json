package fractal.crud_rest_json.persistence;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryRepository<T extends Identifiable<K>, K> implements Repository<T, K> {

	private String id;
	
	private final Map<K, T>			dataStore	= new HashMap<>();
	private final KeyGenerator<K>	keyGenerator;

	public InMemoryRepository(KeyGenerator<K> keyGenerator) {
		this.keyGenerator = keyGenerator;
		id = UUID.randomUUID().toString();
	}

	public Optional<T> get(K id) {
		return Optional.ofNullable(dataStore.get(id));
	}

	public Set<T> getAll() {
		System.out.println(id);
		return new HashSet<>(dataStore.values());
	}

	public LinkedHashSet<T> getAll(Comparator<T> sorter) {
		return new LinkedHashSet<T>(dataStore.values().stream().sorted(sorter).collect(Collectors.toList()));
	}

	public Set<T> getAll(Collection<K> ids) {
		return ids.stream().filter(id -> dataStore.containsKey(id)).map(id -> dataStore.get(id)).collect(Collectors.toSet());
	}

	public void save(T t) {
		if (!t.getId().isPresent()) {
			t.setId(keyGenerator.generate());
		}
		dataStore.put(t.getId().get(), t);
		System.out.println(id);
	}

	public void saveAll(Collection<T> ts) {
		ts.stream().forEach(t -> save(t));
	}

	public void delete(K id) {
		dataStore.remove(id);
	}

	public void deleteAll(Collection<T> ts) {
		dataStore.values().removeAll(ts);
	}

	public void deleteAllById(Collection<K> ids) {
		dataStore.keySet().removeAll(ids);
	}

	public Set<K> toIds(Collection<T> ts) {
		return ts.stream().filter(t -> t.getId().isPresent()).map(t -> t.getId().get()).collect(Collectors.toSet());
	}
}
