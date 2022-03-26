package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories;


import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepasitory extends JpaRepository<Users,Long> {

     Users findByEmail(String email);
}
