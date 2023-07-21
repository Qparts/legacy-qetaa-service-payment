package qetaa.service.payment.restful;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import qetaa.service.payment.dao.DAO;
import qetaa.service.payment.filters.SecuredCustomer;
import qetaa.service.payment.filters.SecuredUser;
import qetaa.service.payment.model.Bank;

@Path("/banks")
public class BanksService {

	@EJB
	private DAO dao; 
	
	
	@SecuredUser
	@POST
	@Path("/bank")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPayment(Bank bank) {
		try {
			dao.persist(bank);
			return Response.status(200).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@PUT
	@Path("/bank")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateBank(Bank bank) {
		try {
			dao.update(bank);
			return Response.status(200).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@DELETE
	@Path("/bank")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteBank(Bank bank) {
		try {
			dao.delete(bank);
			return Response.status(200).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@GET
	@Path("/active-banks/user")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveBanks() {
		try {
			List<Bank> banks = dao.getCondition(Bank.class, "internalStatus", 'A'); 
			return Response.status(200).entity(banks).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@GET
	@Path("/all-banks/user")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBanks() {
		try {
			List<Bank> banks = dao.get(Bank.class); 
			return Response.status(200).entity(banks).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredCustomer
	@GET
	@Path("/active-banks/customer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveBanksCustomer() {
		try {
			List<Bank> banks = dao.getCondition(Bank.class, "customerStatus", 'A'); 
			return Response.status(200).entity(banks).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@GET
	@Path("/bank/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBank(@PathParam(value="param") int id) {
		try {
			Bank bank = dao.find(Bank.class, id); 
			return Response.status(200).entity(bank).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}

}
