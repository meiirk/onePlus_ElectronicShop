package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories;

import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepasitory extends JpaRepository<Orders, String> {

}
