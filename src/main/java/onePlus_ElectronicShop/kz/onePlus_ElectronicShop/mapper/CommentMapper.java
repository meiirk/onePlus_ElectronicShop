package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.mapper;

import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.dto.CommentDto;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel ="spring" )
public interface CommentMapper {
    @Mapping(target = "commentText", source = "comment")
    @Mapping(target = "shopItemId", source = "item.id")
    @Mapping(target = "user_id", source = "author.id")
    @Mapping(target = "fullName", source = "author.fullName")
    CommentDto toCommentDto(Comments comments);
    List<CommentDto> toCommentDtoList(List<Comments > commentsList);

}
