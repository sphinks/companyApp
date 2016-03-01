package com.look.webservices.resources;

import com.look.model.Beneficial;
import com.look.model.Company;
import com.look.services.CompanyService;
import com.look.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @Author: ivan
 * Date: 28.02.16
 * Time: 3:17
 */
@Path("companies")
public class CompanyResource {

    @Inject
    private CompanyService companyService;

    private final Logger LOGGER = LoggerFactory.getLogger(CompanyResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getCompaniesList() {
        return companyService.getAllCompanies();
    }

    @GET
    @Path("{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Company getCompanyById(@PathParam("companyId") String companyId) {
        return companyService.getCompanyInfo(companyId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addCompany(Company company) {

        ValidationUtils.validateCompanyObject(company);
        companyService.addCompany(company);

        return Response.ok()
            .entity("companyId=" + company.getId())
            .build();
    }

    @PUT
    @Path("{companyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateCompany(@PathParam("companyId") String companyId, Company newCompanyInfo) {

        ValidationUtils.validateCompanyObject(newCompanyInfo);
        Company company = companyService.getCompanyInfo(companyId);
        newCompanyInfo.setId(company.getId());

        companyService.updateCompanyInfo(newCompanyInfo);

        return Response.ok()
                .entity("companyId=" + newCompanyInfo.getId())
                .build();
    }

    @POST
    @Path("{companyId}/beneficials")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addBeneficialToCompany(@PathParam("companyId") String companyId, Beneficial beneficial) {

        ValidationUtils.validateBeneficialObject(beneficial);
        Company company = companyService.getCompanyInfo(companyId);

        beneficial.setCompanyId(company.getId());
        companyService.addBeneficialToCompany(beneficial);

        return Response.ok()
                .entity("beneficialId=" + beneficial.getId())
                .build();
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}
