package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.api;

import lombok.RequiredArgsConstructor;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.dto.CommentDto;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Comments;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.ShopItem;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Users;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.services.ShopItemService;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.util.AuthUserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
@RequiredArgsConstructor
public class MainRestController extends AuthUserUtil {

    private final ShopItemService shopItemService;

    @GetMapping(value = "/allitems")
    public ResponseEntity<List<ShopItem>> getItmems() {
        List<ShopItem> items = shopItemService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    @GetMapping(value = "/allComments")
    public ResponseEntity<List<CommentDto>> getComments(@RequestParam(name = "itemId") Long itemId) {
        return new ResponseEntity<>(shopItemService.getCommentsDto(itemId), HttpStatus.OK);
    }


    @PostMapping(value = "/addComment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addComment(@RequestParam(name = "comment") String comment,
                                             @RequestParam(name = "itemId") Long itemId) {

        ShopItem shopItem = shopItemService.getShopItem(itemId);
        if (shopItem != null) {
            Comments comments = new Comments();
            comments.setComment(comment);
            comments.setItem(shopItem);
            comments.setAddedDate(new Date());
            comments.setAuthor(getCurrentUser());
            Comments newComment = shopItemService.addComment(comments);
            if (newComment != null) {
                new ResponseEntity<>("Added", HttpStatus.OK);
            }

        }
        return new ResponseEntity<>("COMMENT NOT FOUND", HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/deleteComment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteComment(@RequestParam(name = "id") Long id) {
        Users currentUser = getCurrentUser();
        Comments comment = shopItemService.getComment(id);
        if (comment != null &&comment.getAuthor().getId() == currentUser.getId() ){
            shopItemService.deleteComment(id);

            return ResponseEntity.ok("Deleted");
        }
        return new ResponseEntity<>("Not fount", HttpStatus.NOT_FOUND);
    }
}
