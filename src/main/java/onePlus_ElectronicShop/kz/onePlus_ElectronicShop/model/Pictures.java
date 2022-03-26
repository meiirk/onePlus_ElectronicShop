package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pictures {
    @Id
    @GeneratedValue
    private  Long id;
    private String url;
    private Date addedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopItem item;

}
