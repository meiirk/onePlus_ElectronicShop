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
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String comment;
    private Date addedDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private ShopItem item;
    @ManyToOne(fetch = FetchType.EAGER)
    private Users author;


}
