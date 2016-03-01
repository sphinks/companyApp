package com.look.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.look.model.Beneficial;
import com.look.model.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
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
        value = "tearDownAll.xml",
        type = DatabaseOperation.DELETE_ALL)
public class CompanyDaoTest {

    @Inject
    private CompanyDao companyDao;

    @Test
    @DatabaseSetup(
            value = "createCompanies.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    public void testGetCompanies() throws Exception {
        List<Company> companies = companyDao.getAllCompanies();

        assertThat(companies.size(), is(2));
        assertThat(companies.get(0).getId(), greaterThan(0));
        assertThat(companies.get(1).getId(), greaterThan(0));
        assertThat(companies.get(0).getName(), is("company1"));
        assertThat(companies.get(1).getName(), is("company2"));
        assertThat(companies.get(0).getAddress(), is("Nikolskaya str., 14"));
        assertThat(companies.get(1).getAddress(), is("Bishop's Gate str., 1"));
        assertThat(companies.get(0).getCity(), is("Moscow"));
        assertThat(companies.get(1).getCity(), is("London"));
        assertThat(companies.get(0).getCountry(), is("Russia"));
        assertThat(companies.get(1).getCountry(), is("England"));
        assertThat(companies.get(0).getEmail(), is("test@company1.com"));
        assertThat(companies.get(1).getEmail(), is("test@company2.com"));
        assertThat(companies.get(0).getPhoneNumber(), is("+7 495 111 11 11"));
        assertThat(companies.get(1).getPhoneNumber(), is("+44 11 222 11 11"));
        assertThat(companies.get(0).getBeneficials(), is(emptyCollectionOf(Beneficial.class)));
        assertThat(companies.get(1).getBeneficials(), is(emptyCollectionOf(Beneficial.class)));
    }

    @Test
    @DatabaseSetup(
            value = "createCompanies.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    public void testGetCompaniesInfo() throws Exception {

        //Use it just to get idea about company id
        int companyId = companyDao.getAllCompanies().get(0).getId();
        Company company = companyDao.getCompanyInfo(companyId);

        assertNotNull(company);
        assertThat(company.getId(), greaterThan(0));
        assertThat(company.getName(), is("company1"));
        assertThat(company.getAddress(), is("Nikolskaya str., 14"));
        assertThat(company.getCity(), is("Moscow"));
        assertThat(company.getCountry(), is("Russia"));
        assertThat(company.getEmail(), is("test@company1.com"));
        assertThat(company.getPhoneNumber(), is("+7 495 111 11 11"));
        assertThat(company.getBeneficials(), is(emptyCollectionOf(Beneficial.class)));
    }

    @Test
    @DatabaseSetup(
            value = "createBeneficialsWithCompany.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    public void testGetCompanyWithBenficials() throws Exception {
        List<Company> companies = companyDao.getAllCompanies();

        assertThat(companies.size(), is(1));
        assertThat(companies.get(0).getName(), is("company1"));
        assertThat(companies.get(0).getAddress(), is("Nikolskaya str., 14"));
        assertThat(companies.get(0).getCity(), is("Moscow"));
        assertThat(companies.get(0).getCountry(), is("Russia"));
        assertThat(companies.get(0).getEmail(), is("test@company1.com"));
        assertThat(companies.get(0).getPhoneNumber(), is("+7 495 111 11 11"));
        assertThat(companies.get(0).getBeneficials().size(), is(2));
    }

    @Test
    @DatabaseSetup(
            value = "tearDownAll.xml",
            type = DatabaseOperation.DELETE_ALL)
    @ExpectedDatabase(
            value = "createCompaniesExpected.xml",
            table = "company",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testAddCompanyInfo() throws Exception {

        Company company3 = new Company("company3", "Nikolskaya str., 15", "Moscow",
                "Russia", "test@company3.com", "+7 495 111 11 13");
        Company company4 = new Company("company4", "Bishop's Gate str., 5", "London", "England",
                "test@company4.com", "+44 11 222 11 14");

        int insertRes1 = companyDao.addCompanyInfo(company3);
        int insertRes2 = companyDao.addCompanyInfo(company4);

        assertThat(insertRes1, is(1));
        assertThat(insertRes2, is(1));
        assertThat(company3.getId(), greaterThan(0));
        assertThat(company4.getId(), greaterThan(0));
    }

    @Test
    @DatabaseSetup(
            value = "createCompanies.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(
            value = "createCompaniesExpected.xml",
            table = "company",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testUpdateCompanyInfo() throws Exception {

        List<Company> companies = companyDao.getAllCompanies();

        assertThat(companies.size(), is(2));

        for (Company company: companies) {
            if ("company1".equals(company.getName())) {
                company.setName("company3");
                company.setAddress("Nikolskaya str., 15");
                company.setCity("Moscow");
                company.setCountry("Russia");
                company.setEmail("test@company3.com");
                company.setPhoneNumber("+7 495 111 11 13");
            }
            if ("company2".equals(company.getName())) {
                company.setName("company4");
                company.setAddress("Bishop's Gate str., 5");
                company.setCity("London");
                company.setCountry("England");
                company.setEmail("test@company4.com");
                company.setPhoneNumber("+44 11 222 11 14");
            }
        }

        int updateRes1 = companyDao.updateCompanyInfo(companies.get(0));
        int updateRes2 = companyDao.updateCompanyInfo(companies.get(1));

        assertThat(updateRes1, is(1));
        assertThat(updateRes2, is(1));
    }
}