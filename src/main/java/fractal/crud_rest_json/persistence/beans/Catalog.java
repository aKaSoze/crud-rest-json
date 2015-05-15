package fractal.crud_rest_json.persistence.beans;

import java.util.LinkedHashSet;

import fractal.crud_rest_json.persistence.IdentifiableBean;

public class Catalog extends IdentifiableBean {

	private LinkedHashSet<Category>	categories;

	public Catalog(String name) {
		super(name);
	}

}
