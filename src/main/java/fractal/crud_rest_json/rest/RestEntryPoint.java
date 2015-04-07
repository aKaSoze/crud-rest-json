package fractal.crud_rest_json.rest;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("{subResources:.*}")
public class RestEntryPoint {

	@Context
	private UriInfo	uriInfo;

	@GET
	public JSONObject get() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		for (ObjectReference objectReference : getObjectPath()) {
			jsonObject.put(objectReference.objectClass.getName(), objectReference.key.orElse("All"));
		}
		return jsonObject;
	}

	@POST
	public String post(JSONObject jsonObject) {
		return uriInfo.getPath();
	}

	@PUT
	public String put(JSONObject jsonObject) {
		return uriInfo.getPath();
	}

	@DELETE
	public String delete() {
		return uriInfo.getPath();
	}

	private List<ObjectReference> getObjectPath() {
		List<ObjectReference> objectPath = new LinkedList<>();
		try {
			String[] tokens = uriInfo.getPath().split("/");
			for (int i = 1; i < tokens.length; i += 2) {
				if (i < tokens.length) {
					objectPath.add(new ObjectReference(getClassForPathToken(tokens[i - 1]), tokens[i]));
				} else {
					objectPath.add(new ObjectReference(Class.forName(tokens[i - 1].substring(0, tokens[i - 1].length() - 1))));
				}
			}
		} catch (ClassNotFoundException e) {
		}

		return objectPath;
	}
	
	private Class<?> getClassForPathToken(String token) throws ClassNotFoundException {
		String firstLetter = token.substring(0, 1).toUpperCase();
		String restOfTheName = token.substring(1).toLowerCase();
		return Class.forName(firstLetter + restOfTheName); 
	}

}
