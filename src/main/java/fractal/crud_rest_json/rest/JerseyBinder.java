package fractal.crud_rest_json.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.google.gson.Gson;

public class JerseyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(Gson.class).to(Gson.class);
	}

}
