package com.belteshazzar.sqrm;

import java.security.Principal;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.belteshazzar.sqrm.json.LoginRequestJSON;

@Path("/rest/user")
@Stateless
public class SqrmRestUser
{
	@Context
	private SecurityContext securityContext;

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	@PersistenceContext(unitName = "seJPA")
	private EntityManager em;

	@POST
	@Path("/signup")
	public boolean signup(@FormParam("username") String username,
			@FormParam("password") String password)
	{
		return false;
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public void login(LoginRequestJSON lr)
	{
		Principal p = securityContext.getUserPrincipal();
		if (p != null)
		{
			System.err.println("already logged in, Principal = " + p);
			throw new SqrmException(SqrmError.ALREADY_LOGGED_IN);
		}

		try
		{
			System.err.println("username: " + lr.username);
			System.err.println("password: " + lr.password);
			request.login(lr.username, lr.password);
		}
		catch (ServletException e)
		{
			throw new SqrmException(SqrmError.INVALID_USERNAME_PASSWORD);
		}
	}

	@POST
	@Path("/logout")
	public void logout()
	{
		try
		{
			request.logout();
		}
		catch (Exception e)
		{
			throw new SqrmException(SqrmError.INVALID_USERNAME_PASSWORD);
		}
	}
}
