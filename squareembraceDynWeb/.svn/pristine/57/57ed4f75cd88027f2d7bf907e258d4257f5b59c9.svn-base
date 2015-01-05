package com.belteshazzar.sqrm;

import java.security.Principal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.belteshazzar.sqrm.json.WikiJSON;
import com.belteshazzar.wiki.Wiki;
import com.belteshazzar.wiki.WikiUtils;

@Path("/page")
@Stateless
public class SqrmPage
{
	@PersistenceContext(unitName = "seJPA")
	private EntityManager em;

	@Context
	private UriInfo uriInfo;

	@EJB
	private WikiUtils wikiUtils;

	@GET
	@Path("/{title}")
	@Produces(MediaType.TEXT_HTML)
	public Response get(@Context SecurityContext securityContext,
			@PathParam("title") String title)
	{
		System.err.println("RestPage.get");

		if (securityContext == null) System.err
				.println("null security context");
		else
		{
			System.err.println(securityContext);
			System.err.println(securityContext.isUserInRole("ROLE1"));
			Principal p = securityContext.getUserPrincipal();
			if (p != null) System.err.println("Principal = " + p);
			else System.err.println("Principal = null");
		}

		StringBuilder str = new StringBuilder();

		str.append("<!DOCTYPE html>");
		str.append("<html>");
		str.append("<head>");
		str.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		str.append("<meta charset=\"utf-8\">");
		str.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
		str.append("<title>Square Embrace</title>");
		str.append("<script type=\"text/javascript\" src=\"/js/jquery-1.9.1.min.js\"></script>");
		str.append("<script type=\"text/javascript\" src=\"/js/jquery.history.js\"></script>");
		str.append("<script type=\"text/javascript\" src=\"/js/sqrm.js\"></script>");
		str.append("<link href=\"/css/normalize.css\" rel=\"stylesheet\" type=\"text/css\"/>");
		str.append("<link href=\"/css/sqrm.css\" rel=\"stylesheet\" type=\"text/css\"/>");
		str.append("<link rel=\"stylesheet\" href=\"/codemirror/codemirror.css\">");
		str.append("<link rel=\"stylesheet\" href=\"/codemirror/eclipse.css\">");
		str.append("<script type=\"text/javascript\" src=\"/codemirror/codemirror.js\"></script>");
		str.append("<script type=\"text/javascript\" src=\"/codemirror/clike.js\"></script>");
		str.append("</head>");
		str.append("<body>");
		str.append("<div id=\"sqrm-overlay\"></div>");

		long start = System.currentTimeMillis();
		Wiki w = wikiUtils.getByTitle(title);

		if (w != null)
		{
			WikiJSON wj = wikiUtils.execute(uriInfo, w);
			wj.generatedIn = System.currentTimeMillis() - start;
			str.append(wj.html);
		}
		else
		{
			str.append("<p>Not found: " + title + "</p>");
		}

		str.append("</body>");
		str.append("</html>");

		Response.ResponseBuilder response = Response.ok(str.toString());
		response.type(MediaType.TEXT_HTML);
		return response.build();
	}
}
