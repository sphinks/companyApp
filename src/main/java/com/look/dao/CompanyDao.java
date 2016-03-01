package com.look.dao;

import com.look.model.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: ivan
 * Date: 25.02.16
 * Time: 19:24
 */
public interface CompanyDao {

    int addCompanyInfo(@Param("company") Company company);

    int updateCompanyInfo(@Param("company")Company company);

    Company getCompanyInfo(int companyId);

    List<Company> getAllCompanies();

}
