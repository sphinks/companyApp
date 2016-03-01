package com.look.services;

import com.look.dao.BeneficialDao;
import com.look.dao.CompanyDao;
import com.look.model.Beneficial;
import com.look.model.Company;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @Author: ivan
 * Date: 05.10.14
 * Time: 16:32
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {
        "/spring/applicationContext.xml"
})
public class CompanyServiceTest {

    @Mock
    BeneficialDao mockBeneficialDao;
    @Mock
    CompanyDao mockCompanyDao;

    CompanyService companyService;

    @Before
    public void setup() {
        companyService = new CompanyService();
        companyService.setBeneficialDao(mockBeneficialDao);
        companyService.setCompanyDao(mockCompanyDao);
    }

    @Test
    public void testAddCompany() {

        Company company = new Company("company3", "Nikolskaya str., 15", "Moscow",
                "Russia", "test@company3.com", "+7 495 111 11 13");

        Beneficial beneficial1 = new Beneficial();
        beneficial1.setName("Jack Sparrow");

        Beneficial beneficial2 = new Beneficial();
        beneficial2.setName("John Bull");

        company.setBeneficials(Arrays.asList(beneficial1, beneficial2));

        companyService.addCompany(company);

        assertThat(company.getBeneficials().get(0).getCompanyId(), is(company.getId()));
        assertThat(company.getBeneficials().get(1).getCompanyId(), is(company.getId()));

        verify(mockCompanyDao).addCompanyInfo(company);
        verify(mockBeneficialDao, times(2)).addBeneficial(Matchers.any(Beneficial.class));
    }

    @Test
    public void testUpdateCompany() {

        Company company = new Company(3, "company3", "Nikolskaya str., 15", "Moscow",
                "Russia", "test@company3.com", "+7 495 111 11 13");

        Beneficial beneficial1 = new Beneficial();
        beneficial1.setName("Jack Sparrow");

        Beneficial beneficial2 = new Beneficial();
        beneficial2.setName("John Bull");

        company.setBeneficials(Arrays.asList(beneficial1, beneficial2));

        companyService.updateCompanyInfo(company);

        assertThat(company.getBeneficials().get(0).getCompanyId(), is(company.getId()));
        assertThat(company.getBeneficials().get(1).getCompanyId(), is(company.getId()));

        verify(mockCompanyDao).updateCompanyInfo(company);
        verify(mockBeneficialDao).deleteBeneficialForCompany(company.getId());
        verify(mockBeneficialDao, times(2)).addBeneficial(Matchers.any(Beneficial.class));
    }

}
