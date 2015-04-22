package fractal.crud_rest_json.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfigurer extends ResourceConfig {

	public JerseyConfigurer() {
		register(new JerseyBinder());
		packages(true, "fractal.crud_rest_json.rest");
	}

}
