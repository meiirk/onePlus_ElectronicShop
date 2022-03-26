package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopItem {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int price;
    private int amount;
    private int stars;
    private String smallPicURL;
    private String largePicURL;
    private Date addedDate;
    private boolean inTopPage;

 @ManyToOne(fetch = FetchType.LAZY)
 private Brands brand;

 @ManyToMany(fetch =FetchType.LAZY )
 private List<Categories> categories;







}
