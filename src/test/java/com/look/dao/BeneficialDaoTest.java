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
        value = "tearDownAll.xml",
        type = DatabaseOperation.DELETE_ALL)
public class BeneficialDaoTest {

    @Inject
    private BeneficialDao beneficialDao;
    @Inject
    private CompanyDao companyDao;

    @Test
    @DatabaseSetup(
            value = "createBeneficialsWithCompany.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    public void testGetBeneficialByCompanyId() throws Exception {
        List<Beneficial> beneficials = beneficialDao.getCompanyBeneficials(1);

        assertThat(beneficials.size(), is(2));

    }

    @Test
    @DatabaseSetup(
            value = "createBeneficialsWithCompany.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(
            value = "tearDownAll.xml",
            table = "beneficial",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testDeleteBeneficialByCompanyId() throws Exception {

        int deletedRows = beneficialDao.deleteBeneficialForCompany(1);

        assertThat(deletedRows, is(2));

    }

    @Test
    @DatabaseSetup(
            value = "createCompanies.xml",
            type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(
            value = "addBeneficialsExpected.xml",
            table = "beneficial",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testAddBeneficials() throws Exception {

        List<Company> companies = companyDao.getAllCompanies();

        assertThat(companies.size(), is(2));

        Beneficial beneficialForFirstCompany = new Beneficial();
        beneficialForFirstCompany.setName("Jack Sparrow");
        beneficialForFirstCompany.setCompanyId(companies.get(0).getId());

        int addResult = beneficialDao.addBeneficial(beneficialForFirstCompany);
        assertThat(addResult, is(1));

        Beneficial beneficialForSecondCompany = new Beneficial();
        beneficialForSecondCompany.setName("John Bull");
        beneficialForSecondCompany.setCompanyId(companies.get(1).getId());
        addResult = beneficialDao.addBeneficial(beneficialForSecondCompany);
        assertThat(addResult, is(1));

        List<Beneficial> beneficialsRead = beneficialDao.getCompanyBeneficials(companies.get(0).getId());
        assertThat(beneficialsRead.size(), is(1));
        assertThat(beneficialsRead.get(0).getCompanyId(), is(beneficialForFirstCompany.getCompanyId()));

        beneficialsRead = beneficialDao.getCompanyBeneficials(companies.get(1).getId());
        assertThat(beneficialsRead.size(), is(1));
        assertThat(beneficialsRead.get(0).getCompanyId(), is(beneficialForSecondCompany.getCompanyId()));
    }

}