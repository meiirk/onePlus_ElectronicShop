package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories;

import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Comments;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepasitory extends JpaRepository<Comments,Long> {

    List<Comments> findAllByItemOrderByAddedDateDesc(ShopItem item);
}
