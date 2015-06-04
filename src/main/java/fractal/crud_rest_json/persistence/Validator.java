package fractal.crud_rest_json.persistence;

import java.util.Objects;

public class Validator<T extends Identifiable<K>, K> {

	private final Repository<T, K>	repo;

	public Validator(Repository<T, K> repo) {
		this.repo = Objects.requireNonNull(repo);
	}

	public void validate(T t) throws ValidationException {

		ValidationException exception = new ValidationException();

		if (t.getName() == null) {
			exception.addIssue("name", "Must not be null.");
		} else if (repo.exists(other -> t.getName().equalsIgnoreCase(other.getName()))) {
			exception.addIssue("name", "Must be unique.");
		}

		if (exception.hasIssues()) {
			throw exception;
		}
	}

}
