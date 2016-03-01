package com.look.dao;

import com.look.model.Beneficial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: ivan
 * Date: 25.02.16
 * Time: 19:45
 */
public interface BeneficialDao {

    List<Beneficial> getCompanyBeneficials(@Param("company_id")int companyId);

    int addBeneficial(@Param("beneficial") Beneficial beneficial);

    int deleteBeneficialForCompany(@Param("company_id")int companyId);
}
