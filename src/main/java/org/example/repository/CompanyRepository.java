package org.example.repository;

import jakarta.persistence.EntityManagerFactory;
import org.example.entity.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompanyRepository extends BaseRepository<Company, UUID>{
    public CompanyRepository(EntityManagerFactory emf) {
        super(emf, Company.class);
    }

    public Optional<Company> findByOrgNum(String orgNum) {
        return executeRead(em ->
            em.createQuery("SELECT c FROM Company c WHERE c.orgNum = :orgNum", Company.class)
                .setParameter("orgNum", orgNum)
                .getResultStream()
                .findFirst()
        );
    }

    public Optional<Company> findByEmail(String email) {
        return executeRead(em ->
            em.createQuery("SELECT c FROM Company c WHERE c.email = :email", Company.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
        );
    }

    public List<Company> findByName(String name) {
        return executeRead(em ->
            em.createQuery("SELECT c FROM Company c WHERE c.name LIKE :name", Company.class)
                .setParameter("name", "%" + name.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%")
                .getResultList()
        );
    }
}
