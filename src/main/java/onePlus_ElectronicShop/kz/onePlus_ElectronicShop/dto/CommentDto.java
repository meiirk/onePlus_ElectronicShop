package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {
     private Long id;
     private String commentText;
     private  Long shopItemId;
     private String fullName;
     private Long user_id;
     private Date addedDate;
}
