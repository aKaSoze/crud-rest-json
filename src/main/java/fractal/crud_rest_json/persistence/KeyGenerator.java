package fractal.crud_rest_json.persistence;

@FunctionalInterface
public interface KeyGenerator<K> {

	K generate();

}
