package fractal.crud_rest_json.rest;

import java.util.UUID;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.google.gson.Gson;

import fractal.crud_rest_json.persistence.Decorator;
import fractal.crud_rest_json.persistence.Identifiable;
import fractal.crud_rest_json.persistence.IdentifiableJson;
import fractal.crud_rest_json.persistence.InMemoryRepository;
import fractal.crud_rest_json.persistence.Repository;
import fractal.crud_rest_json.persistence.RepositoryLocator;

public class JerseyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new Gson()).to(Gson.class);
		TypeLiteral<Repository<IdentifiableJson, String>> jsonRepoType = new TypeLiteral<Repository<IdentifiableJson, String>>() {
		};
		bind(new InMemoryRepository<IdentifiableJson, String>()).to(jsonRepoType);
		bind(new Decorator<Identifiable<String>, String>(() -> UUID.randomUUID().toString())).to(new TypeLiteral<Decorator<Identifiable<String>, String>>() {});
		bind(new RepositoryLocator()).to(RepositoryLocator.class);
	}

}
