package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class SearchElemetData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  name;
    private Long brandId;
    private double priceFrom;
    private double priceTo;
    private String accAndDesc;


}
