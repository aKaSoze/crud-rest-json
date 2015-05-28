package fractal.crud_rest_json.persistence;

import java.util.Objects;

public class Validator<T extends Identifiable<K>, K> {

	private final Repository<T, K>	repo;

	public Validator(Repository<T, K> repo) {
		this.repo = Objects.requireNonNull(repo);
	}

	public void validate(T t) throws ValidationException {
		if (repo.exists(other -> t.getName().equalsIgnoreCase(other.getName()))) {
			throw new ValidationException();
		}
	}

}
