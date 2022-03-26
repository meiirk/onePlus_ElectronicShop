package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories;

import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ItemRepasitories extends JpaRepository<ShopItem,Long> {
    List<ShopItem> findAllByNameLikeOrderByPriceAsc(String name);
    List<ShopItem> findAllByNameLikeOrderByPriceDesc(String name);
    List<ShopItem> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, int price1, int price2);
    List<ShopItem> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, int price1, int price2);
    List<ShopItem> findAllByPriceBetweenOrderByPriceAsc(int price1,int price2);
    List<ShopItem> findAllByPriceBetweenOrderByPriceDesc(int price1,int price2);
    List<ShopItem> findAllByPriceIsGreaterThanOrderByPriceDesc(int price1);
    List<ShopItem> findAllByPriceIsGreaterThanOrderByPriceAsc(int price1);
    List<ShopItem> findAllByPriceIsLessThanOrderByPriceAsc(int price2);
    List<ShopItem> findAllByPriceIsLessThanOrderByPriceDesc(int price2);
    List<ShopItem> findAllByNameStartingWith(String name);
    List<ShopItem> findAllByNameContaining(String name);
    List<ShopItem> findAllByNameContainingIgnoreCase(String name);
}
