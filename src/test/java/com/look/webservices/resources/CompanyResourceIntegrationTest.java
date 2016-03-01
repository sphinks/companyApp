package com.look.webservices.resources;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.look.model.Company;
import com.look.services.CompanyService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@ContextConfiguration(locations = {
        "/spring/applicationContext.xml"
})
@DatabaseTearDown(
        type = DatabaseOperation.DELETE_ALL,
        value = "classpath:/com/look/dao/tearDownAll.xml")
public class CompanyResourceIntegrationTest extends AbstractIntegrationTest {

    @Inject
    CompanyService companyService;

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testGetAllCompanies() {
        String companiesJson = requestWithAppJson("/companies").get(String.class);

        assertThat(companiesJson, Matchers.containsString("{\"address\":\"Nikolskaya str., 14\",\"beneficials\":[],\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"test@company1.com\",\"id\":"));
        assertThat(companiesJson, Matchers.containsString("\"name\":\"company1\",\"phoneNumber\":\"+7 495 111 11 11\"}"));
        assertThat(companiesJson, Matchers.containsString("{\"address\":\"Bishop's Gate str., 1\",\"beneficials\":[],\"city\":\"London\",\"country\":\"England\",\"email\":\"test@company2.com\",\"id\":"));
        assertThat(companiesJson, Matchers.containsString("\"name\":\"company2\",\"phoneNumber\":\"+44 11 222 11 11\"}"));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testGetCompanyInfo() {

        Company company = companyService.getAllCompanies().get(0);

        String companiesJson = requestWithAppJson("/companies/" + company.getId()).get(String.class);

        assertThat(companiesJson, Matchers.containsString("{\"address\":\"Nikolskaya str., 14\",\"beneficials\":[]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"test@company1.com\",\"id\":" + company.getId() +
                ",\"name\":\"company1\",\"phoneNumber\":\"+7 495 111 11 11\"}"));

    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testGetCompanyInfoWithWrongNumericId() {

        Response response = target("/companies/0").request().get();
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "classpath:/com/look/dao/tearDownAll.xml")
    @ExpectedDatabase(
            value = "classpath:/com/look/dao/createCompanies.xml",
            table = "company",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testAddCompanyInfo() {

        Response response = target("/companies").request().post(Entity.json("{\"address\":\"Nikolskaya str., 14\",\"beneficials\":[{\"name\":\"Hey\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"test@company1.com\"" +
                ",\"name\":\"company1\",\"phoneNumber\":\"+7 495 111 11 11\"}"));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        String result = response.readEntity(String.class);
        String companyId = result.split("=")[1];

        assertThat(Integer.parseInt(companyId), greaterThan(0));

        response = target("/companies").request().post(Entity.json("{\"address\":\"Bishop's Gate str., 1\",\"beneficials\":[{\"name\":\"BeneficialName\"}]," +
                "\"city\":\"London\",\"country\":\"England\",\"email\":\"test@company2.com\"" +
                ",\"name\":\"company2\",\"phoneNumber\":\"+44 11 222 11 11\"}"));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        result = response.readEntity(String.class);
        companyId = result.split("=")[1];

        assertThat(Integer.parseInt(companyId), greaterThan(0));

    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    @ExpectedDatabase(
            value = "classpath:/com/look/dao/createCompaniesExpected.xml",
            table = "company",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testUpdateCompanyInfo() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId()).request().put(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"test@company3.com\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"+7 495 111 11 13\"}"));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        response = target("/companies/"  + companies.get(1).getId()).request().put(Entity.json("{\"address\":\"Bishop's Gate str., 5\",\"beneficials\":[{\"name\":\"BeneficialName\"}]," +
                "\"city\":\"London\",\"country\":\"England\",\"email\":\"test@company4.com\"" +
                ",\"name\":\"company4\",\"phoneNumber\":\"+44 11 222 11 14\"}"));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testUpdateNonExistingCompanyInfo() {

        Response response = target("/companies/0").request().put(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"test@company3.com\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"+7 495 111 11 13\"}"));

        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testUpdateCompanyInfoWithEmptyEmailAndPhone() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId()).request().put(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testUpdateCompanyInfoWithEmptyName() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId()).request().put(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testUpdateCompanyInfoWithEmptyAddress() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId()).request().put(Entity.json("{\"address\":\"\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"comany3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testUpdateCompanyInfoWithEmptyCity() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId()).request().put(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testUpdateCompanyInfoWithEmptyCountry() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId()).request().put(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"\",\"email\":\"\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testUpdateCompanyInfoWithEmptyBeneficials() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId()).request().put(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testAddCompanyInfoWithEmptyEmailAndPhone() {

        Response response = target("/companies/").request().post(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "classpath:/com/look/dao/tearDownAll.xml")
    public void testAddCompanyInfoWithEmptyName() {

        Response response = target("/companies").request().post(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "classpath:/com/look/dao/tearDownAll.xml")
    public void testAddCompanyInfoWithEmptyAddress() {

        Response response = target("/companies").request().post(Entity.json("{\"address\":\"\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"comany3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "classpath:/com/look/dao/tearDownAll.xml")
    public void testAddCompanyInfoWithEmptyCity() {

        Response response = target("/companies").request().post(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "classpath:/com/look/dao/tearDownAll.xml")
    public void testAddCompanyInfoWithEmptyCountry() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies").request().post(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[{\"name\":\"New Name\"}]," +
                "\"city\":\"Moscow\",\"country\":\"\",\"email\":\"\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "classpath:/com/look/dao/tearDownAll.xml")
    public void testAddCompanyInfoWithEmptyBeneficials() {

        Response response = target("/companies").request().post(Entity.json("{\"address\":\"Nikolskaya str., 15\",\"beneficials\":[]," +
                "\"city\":\"Moscow\",\"country\":\"Russia\",\"email\":\"\"" +
                ",\"name\":\"company3\",\"phoneNumber\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @DatabaseSetup(
            value = "classpath:/com/look/dao/createCompanies.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(
            value = "classpath:/com/look/dao/addBeneficialsExpected.xml",
            table = "beneficial",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testAddBeneficialToCompany() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId() + "/beneficials").request().post(
                Entity.json("{\"name\":\"Jack Sparrow\"}"));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        String result = response.readEntity(String.class);
        String beneficialId = result.split("=")[1];

        assertThat(Integer.parseInt(beneficialId), greaterThan(0));

        response = target("/companies/" + companies.get(1).getId() + "/beneficials").request().post(
                Entity.json("{\"name\":\"John Bull\"}"));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        result = response.readEntity(String.class);
        beneficialId = result.split("=")[1];

        assertThat(Integer.parseInt(beneficialId), greaterThan(0));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testAddBeneficialToNonExistingCompany() {

        Response response = target("/companies/0/beneficials").request().post(
                Entity.json("{\"name\":\"Jack Sparrow\"}"));

        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "classpath:/com/look/dao/createCompanies.xml")
    public void testAddBeneficialWithEmptyNameToExistingCompany() {

        List<Company> companies = companyService.getAllCompanies();

        Response response = target("/companies/" + companies.get(0).getId() + "/beneficials").request().post(
                Entity.json("{\"name\":\"\"}"));

        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }



}