package fractal.crud_rest_json.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("hello")
public class RestEntryPoint {

	@GET
    @Path("{name}")
    public String sayHello(@PathParam("name") String name){
        StringBuilder stringBuilder = new StringBuilder("Hello ");
        stringBuilder.append(name).append("!");
        return stringBuilder.toString();
    }
	
}
