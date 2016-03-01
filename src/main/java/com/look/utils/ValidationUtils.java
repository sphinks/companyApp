package com.look.utils;

import com.look.model.Beneficial;
import com.look.model.Company;

import javax.validation.ConstraintViolation;
import javax.ws.rs.WebApplicationException;
import java.util.Iterator;
import java.util.Set;

/**
 * Class contains the constants required for validation testing.
 * 
 */
public class ValidationUtils {

    public static boolean checkValidationResult(Set<? extends ConstraintViolation> violations, String violationPath) {
        boolean result = false;
        Iterator<? extends ConstraintViolation> it = violations.iterator();
        while (it.hasNext()) {
            if (violationPath.equals(it.next().getPropertyPath().toString())) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static void validateCompanyObject(Company company) {

        String resultMessage = "";
        if (company.getName() == null || company.getName().isEmpty()) {
            resultMessage += "Field 'Name' in company can not be empty\n\r";
        }
        if (company.getAddress() == null || company.getAddress().isEmpty()) {
            resultMessage += "Field 'Address' in company can not be empty\n\r";
        }
        if (company.getCity() == null || company.getCity().isEmpty()) {
            resultMessage += "Field 'City' in company can not be empty\n\r";
        }
        if (company.getCountry() == null || company.getCountry().isEmpty()) {
            resultMessage += "Field 'City' in company can not be empty\n\r";
        }
        if (company.getBeneficials() == null || company.getBeneficials().isEmpty()) {
            resultMessage += "Field 'Beneficial' in company can not be empty\n\r";
        }
        if (!resultMessage.isEmpty()) {
            throw new WebApplicationException(resultMessage);
        }
    }

    public static void validateBeneficialObject(Beneficial beneficial) {

        String resultMessage = "";
        if (beneficial.getName() == null || beneficial.getName().isEmpty()) {
            resultMessage += "Field 'Name' in beneficial can not be empty\n\r";
        }
        if (!resultMessage.isEmpty()) {
            throw new WebApplicationException(resultMessage);
        }
    }
}
