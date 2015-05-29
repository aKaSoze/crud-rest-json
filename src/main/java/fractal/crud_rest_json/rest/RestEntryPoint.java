package fractal.crud_rest_json.rest;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fractal.crud_rest_json.persistence.Decorator;
import fractal.crud_rest_json.persistence.Identifiable;
import fractal.crud_rest_json.persistence.IdentifiableBean;
import fractal.crud_rest_json.persistence.IdentifiableJson;
import fractal.crud_rest_json.persistence.Repository;
import fractal.crud_rest_json.persistence.RepositoryLocator;
import fractal.crud_rest_json.persistence.ValidationException;
import fractal.crud_rest_json.persistence.ValidatorLocator;

@Path("{subResources:.*}")
public class RestEntryPoint {

	private static final Set<String>					beanPackages	= new HashSet<>();

	private final UriInfo								uriInfo;

	private final Gson									gson;

	private final RepositoryLocator						repositoryLocator;

	private final ValidatorLocator						validatorLocator;

	private final Decorator<Identifiable<UUID>, UUID>	decorator;

	static {
		beanPackages.add("fractal.crud_rest_json.persistence.beans");
	}

	@Inject
	public RestEntryPoint(UriInfo uriInfo, Gson gson, RepositoryLocator repositoryLocator, ValidatorLocator validatorLocator, Decorator<Identifiable<UUID>, UUID> decorator) {
		this.uriInfo = Objects.requireNonNull(uriInfo);
		this.gson = Objects.requireNonNull(gson);
		this.repositoryLocator = Objects.requireNonNull(repositoryLocator);
		this.validatorLocator = Objects.requireNonNull(validatorLocator);
		this.decorator = Objects.requireNonNull(decorator);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@Context ObjectReference lastObjectFromPath) {
		return lastObjectFromPath.key.map(key -> toResponse(getIdentifiable(lastObjectFromPath.entityType, key))).orElse(
				Response.ok(gson.toJson(getRepo(lastObjectFromPath.entityType).getAll())).build());
	}

	@POST
	public Response post(@Context ObjectReference lastObjectFromPath, String jsonString) {
		JsonObject jsonObject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();

		try {
			Identifiable<?> identifiable = lastObjectFromPath.entityType.unify(clazz -> save(clazz, jsonObject), typeName -> save(typeName, jsonObject));
			return Response.created(uriInfo.getAbsolutePathBuilder().path(identifiable.getName()).build()).build();
		} catch (ValidationException v) {
			return Response.status(Status.BAD_REQUEST).entity(v.getMessage()).build();
		}
	}

	@PUT
	public String put(String jsonObject) {
		return uriInfo.getPath();
	}

	@DELETE
	public void delete(@Context ObjectReference lastObjectFromPath) {
		lastObjectFromPath.key.ifPresent(key -> getRepo(lastObjectFromPath.entityType).delete(UUID.fromString(key)));
	}
	
	@SuppressWarnings("unchecked")
	private Repository<? extends Identifiable<UUID>, UUID> getRepo(Either<Class<? extends IdentifiableBean>, String> entityType) {
		return repositoryLocator.locate((Either<Class<? extends Identifiable<UUID>>, String>) (Object) entityType);
	}

	private Optional<? extends Identifiable<UUID>> getIdentifiable(Either<Class<? extends IdentifiableBean>, String> type, String key) {
		@SuppressWarnings("unchecked")
		Repository<? extends Identifiable<UUID>, UUID> repo = repositoryLocator.locate((Either<Class<? extends Identifiable<UUID>>, String>) (Object) type);
		try {
			return repo.get(UUID.fromString(key));
		} catch (IllegalArgumentException e) {
			return repo.yield(entity -> entity.getName().equals(key));
		}
	}

	private Response toResponse(Optional<?> entity) {
		return entity.map(content -> Response.ok(gson.toJson(content)).build()).orElse(Response.status(Status.NOT_FOUND).build());
	}

	private <T extends IdentifiableBean> IdentifiableBean save(Class<T> clazz, JsonObject jsonObject) {
		T bean = gson.fromJson(jsonObject, clazz);
		decorator.decorate(bean);
		validatorLocator.locate(clazz).validate(bean);
		repositoryLocator.locate(clazz).save(bean);

		return bean;
	}

	private IdentifiableJson save(String typeName, JsonObject jsonObject) {
		IdentifiableJson identifiableJson = new IdentifiableJson(jsonObject);
		decorator.decorate(identifiableJson);
		Repository<IdentifiableJson, UUID> jsonRepo = repositoryLocator.locate(typeName);
		jsonRepo.save(identifiableJson);
		return identifiableJson;
	}

	public static void main(String[] args) {
		RestEntryPoint entryPoint = new RestEntryPoint(null, new Gson(), null, null, new Decorator<>(() -> null));
		System.out.println(entryPoint.get(null));
	}

}
