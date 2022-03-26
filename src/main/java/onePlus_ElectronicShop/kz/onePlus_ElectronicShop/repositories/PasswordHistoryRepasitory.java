package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories;

import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.PasswordHistory;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PasswordHistoryRepasitory  extends JpaRepository<PasswordHistory,Long> {
    List<PasswordHistory> findAllByUser(Users user);
}
