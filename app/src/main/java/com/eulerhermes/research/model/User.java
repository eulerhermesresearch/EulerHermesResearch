package com.eulerhermes.research.model;

import com.google.api.client.util.Key;
import org.apache.commons.lang3.StringUtils;

public class User {
    @Key("Company")
    private String company;
    @Key("Country")
    private String country;
    @Key("DateInserted")
    private String dateInserted;
    @Key("Department")
    private String department;
    @Key("EmployeesRange")
    private String employeesRange;
    @Key("ForeName")
    private String foreName;
    @Key("HasAcceptedInformation")
    private boolean hasAcceptedInformation;
    @Key("Id")
    private String id;
    @Key("IsCustomerOfEH")
    private boolean isCustomerOfEH;
    @Key("JobTitle")
    private String jobTitle;
    @Key("Name")
    private String name;
    @Key("Password")
    private String password;
    @Key("Phone")
    private String phone;
    @Key("PhonePrefix")
    private String phonePrefix;
    @Key("SalesRange")
    private String salesRange;
    @Key("Sector")
    private String sector;
    @Key("Username")
    private String username;

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateInserted() {
        return this.dateInserted;
    }

    public void setDateInserted(String dateInserted) {
        this.dateInserted = dateInserted;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmployeesRange() {
        return this.employeesRange;
    }

    public void setEmployeesRange(String employeesRange) {
        this.employeesRange = employeesRange;
    }

    public String getForeName() {
        return this.foreName;
    }

    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public boolean getHasAcceptedInformation() {
        return this.hasAcceptedInformation;
    }

    public void setHasAcceptedInformation(boolean hasAcceptedInformation) {
        this.hasAcceptedInformation = hasAcceptedInformation;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsCustomerOfEH() {
        return this.isCustomerOfEH;
    }

    public void setIsCustomerOfEH(boolean isCustomerOfEH) {
        this.isCustomerOfEH = isCustomerOfEH;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhonePrefix() {
        return this.phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public String getSalesRange() {
        return this.salesRange;
    }

    public void setSalesRange(String salesRange) {
        this.salesRange = salesRange;
    }

    public String getSector() {
        return this.sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFormattedName() {
        return new StringBuilder(String.valueOf(StringUtils.capitalize(this.foreName))).append(" ").append(this.name.toUpperCase()).toString();
    }
}
