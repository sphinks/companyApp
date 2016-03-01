package com.look.services;

import com.look.dao.BeneficialDao;
import com.look.dao.CompanyDao;
import com.look.model.Beneficial;
import com.look.model.Company;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

/**
 * @Author: ivan
 * Date: 28.02.16
 * Time: 2:46
 */
@Service
public class CompanyService {

    @Inject
    BeneficialDao beneficialDao;
    @Inject
    CompanyDao companyDao;

    public void addCompany(Company company) {

        companyDao.addCompanyInfo(company);
        for (Beneficial beneficial: company.getBeneficials()) {
            beneficial.setCompanyId(company.getId());
            beneficialDao.addBeneficial(beneficial);
        }
    }

    public void updateCompanyInfo(Company company) {

        beneficialDao.deleteBeneficialForCompany(company.getId());
        companyDao.updateCompanyInfo(company);
        for (Beneficial beneficial: company.getBeneficials()) {
            beneficial.setCompanyId(company.getId());
            beneficialDao.addBeneficial(beneficial);
        }
    }

    public void addBeneficialToCompany(Beneficial beneficial) {
        beneficialDao.addBeneficial(beneficial);
    }

    public Company getCompanyInfo(String companyId) {
        Company company = companyDao.getCompanyInfo(Integer.parseInt(companyId));
        if (company == null) {
            throw new NotFoundException("No company with id: " + companyId);
        }
        return company;
    }

    public List<Company> getAllCompanies() {
        return companyDao.getAllCompanies();
    }

    public BeneficialDao getBeneficialDao() {
        return beneficialDao;
    }

    public void setBeneficialDao(BeneficialDao beneficialDao) {
        this.beneficialDao = beneficialDao;
    }

    public CompanyDao getCompanyDao() {
        return companyDao;
    }

    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }
}
