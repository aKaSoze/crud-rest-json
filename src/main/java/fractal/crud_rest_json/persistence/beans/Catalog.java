package fractal.crud_rest_json.persistence.beans;

import java.util.LinkedHashSet;

import fractal.crud_rest_json.persistence.TrackedIdentifiable;

public class Catalog extends TrackedIdentifiable {

	private LinkedHashSet<Category>	categories;

	public Catalog(String name) {
		super(name);
	}

}
