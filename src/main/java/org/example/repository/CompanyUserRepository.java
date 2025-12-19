package org.example.repository;

import jakarta.persistence.EntityManagerFactory;
import org.example.entity.CompanyUser;
import org.example.entity.CompanyUserId;

import java.util.List;
import java.util.UUID;


public class CompanyUserRepository extends BaseRepository<CompanyUser, CompanyUserId> {
    public CompanyUserRepository(EntityManagerFactory emf) {
        super(emf, CompanyUser.class);
    }

    public List<CompanyUser> findByCompanyId(UUID companyId) {
        return executeRead(em ->
            em.createQuery("SELECT cu FROM CompanyUser cu WHERE cu.company.id = :companyId", CompanyUser.class)
                .setParameter("companyId", companyId)
                .getResultList()
        );
    }
}
