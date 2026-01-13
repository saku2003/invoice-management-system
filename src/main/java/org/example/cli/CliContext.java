package org.example.cli;

import org.example.entity.company.CompanyDTO;
import org.example.entity.user.UserDTO;

import java.util.UUID;


public class CliContext {
    private UUID currentUserId;
    private UUID currentCompanyId;
    private UserDTO currentUser;
    private CompanyDTO currentCompany;

    public UUID getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(UUID currentUserId) {
        this.currentUserId = currentUserId;
    }

    public UUID getCurrentCompanyId() {
        return currentCompanyId;
    }

    public void setCurrentCompanyId(UUID currentCompanyId) {
        this.currentCompanyId = currentCompanyId;
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
        this.currentUserId = currentUser != null ? currentUser.id() : null;
    }

    public CompanyDTO getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(CompanyDTO currentCompany) {
        this.currentCompany = currentCompany;
        this.currentCompanyId = currentCompany != null ? currentCompany.id() : null;
    }

    public boolean isLoggedIn() {
        return currentUser != null && currentUserId != null;
    }

    public boolean hasCompanySelected() {
        return currentCompany != null && currentCompanyId != null;
    }

    public void clearSession() {
        this.currentUserId = null;
        this.currentCompanyId = null;
        this.currentUser = null;
        this.currentCompany = null;
    }

    public void clearCompany() {
        this.currentCompanyId = null;
        this.currentCompany = null;
    }
}


