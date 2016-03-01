package com.look.model;

/**
 * @Author: ivan
 * Date: 25.02.16
 * Time: 19:42
 */
public class Beneficial {
    private int id;
    private int companyId;
    private String name;

    public Beneficial() {}

    public Beneficial(int id, int companyId, String name) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
    }

    public Beneficial(int companyId, String name) {
        this.companyId = companyId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
