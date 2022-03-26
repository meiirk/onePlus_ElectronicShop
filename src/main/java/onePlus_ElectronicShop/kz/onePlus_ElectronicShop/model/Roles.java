package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;
    private String description;


    @Override
    public String getAuthority() {
        return role ;
    }
}
