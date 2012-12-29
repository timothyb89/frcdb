package net.frcdb.servlet.json;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author tim
 */
@Path("/test")
public class TestService {
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonResponse<TestBean> getTestBean(
			@FormParam("name") String name,
			@FormParam("number") int number) {
		TestBean bean = new TestBean(name, number);
		return JsonResponse.success(bean);
	}
	
	public class TestBean {
		
		private String name;
		private int number;

		public TestBean(String name, int number) {
			this.name = name;
			this.number = number;
		}

		public String getName() {
			return name;
		}

		public int getNumber() {
			return number;
		}
		
	}
	
}
