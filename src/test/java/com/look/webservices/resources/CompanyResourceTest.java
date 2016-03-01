package com.look.webservices.resources;

import com.look.model.Company;
import com.look.services.CompanyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @Author: ivan
 * Date: 13.09.14
 * Time: 1:36
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {
        "/spring/applicationContext.xml"
})
public class CompanyResourceTest {

    CompanyResource companyResource;
    @Mock
    CompanyService mockCompanyService;

    @Before
    public void setup() {
        companyResource = new CompanyResource();
        companyResource.setCompanyService(mockCompanyService);
    }

    @Test
    public void testGetAllCompanies() {

        Company company1 = new Company();
        List<Company> companies = new ArrayList<>();
        companies.add(company1);

        when(mockCompanyService.getAllCompanies()).thenReturn(companies);

        List<Company> result = companyResource.getCompaniesList();

        assertThat(result.size(), is(1));
        verify(mockCompanyService).getAllCompanies();
    }

}
