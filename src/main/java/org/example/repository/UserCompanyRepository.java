package org.example.repository;

import jakarta.persistence.EntityManagerFactory;
import org.example.entity.UserCompany;
import org.example.entity.UserCompanyId;

import java.util.List;
import java.util.UUID;


public class UserCompanyRepository extends BaseRepository<UserCompany, UserCompanyId> {
    public UserCompanyRepository(EntityManagerFactory emf) {
        super(emf, UserCompany.class);
    }

    public List<UserCompany> findByCompanyId(UUID companyId) {
        return executeRead(em ->
            em.createQuery("SELECT uc FROM UserCompany uc WHERE uc.company.id = :companyId", UserCompany.class)
                .setParameter("companyId", companyId)
                .getResultList()
        );
    }
}
