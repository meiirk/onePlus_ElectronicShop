package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories;

import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;

@Repository
@Transactional
public interface RolesRepasitory extends JpaRepository<Roles,Long> {

   Roles findByRole(String role);
}
