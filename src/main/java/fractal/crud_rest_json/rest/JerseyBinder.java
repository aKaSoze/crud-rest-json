package fractal.crud_rest_json.rest;

import java.util.UUID;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.google.gson.Gson;

import fractal.crud_rest_json.persistence.IdentifiableJsonObject;
import fractal.crud_rest_json.persistence.InMemoryRepository;
import fractal.crud_rest_json.persistence.Repository;

public class JerseyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(Gson.class).to(Gson.class);
		TypeLiteral<Repository<IdentifiableJsonObject, String>> jsonRepoType = new TypeLiteral<Repository<IdentifiableJsonObject, String>>() {};
		bind(new InMemoryRepository<IdentifiableJsonObject, String>(() -> UUID.randomUUID().toString())).to(jsonRepoType);
	}

}
