package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories;

import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Pictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PictureRepasitory extends JpaRepository<Pictures,Long> {

    List<Pictures> findByItem_Id(Long id);
}
