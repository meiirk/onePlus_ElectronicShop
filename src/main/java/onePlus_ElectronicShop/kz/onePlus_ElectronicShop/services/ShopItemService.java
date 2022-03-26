package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.services;

import lombok.RequiredArgsConstructor;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.dto.CommentDto;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.mapper.CommentMapper;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.*;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopItemService {

    private final ItemRepasitories itemRepasitories;
    private final CategoriesRepasitory categoriesRepasitory;
    private final BrandsRepasitory brandsRepasitory;
    private final SearchElementDataRepasitory searchElementDataRepasitory;
    private final CountriesRepasitory countriesRepasitory;
    private final PictureRepasitory pictureRepasitory;
    private final CommentsRepasitory commentsRepasitory;
    private final CommentMapper commentMapper;


    public List<ShopItem> getAllItems() {
        return itemRepasitories.findAll();
    }

    public List<Categories> getAllCategories() {
        return categoriesRepasitory.findAll();
    }

    public List<Brands> getAllBrands() {
        return brandsRepasitory.findAll();
    }

    public ShopItem getItem(Long id) {
        return itemRepasitories.getById(id);
    }


    public List<ShopItem> byPriceAsc(String name) {
        return itemRepasitories.findAllByNameLikeOrderByPriceAsc(name);
    }

    public List<ShopItem> byPriceDesc(String name) {
        return itemRepasitories.findAllByNameLikeOrderByPriceDesc(name);
    }

    public List<ShopItem> byPriceAscPriceBetween(String name, int price1, int price2) {
        return itemRepasitories.findAllByNameLikeAndPriceBetweenOrderByPriceAsc(name, price1, price2);
    }

    public List<ShopItem> byPriceDescPriceBetween(String name, int price1, int price2) {
        return itemRepasitories.findAllByNameLikeAndPriceBetweenOrderByPriceDesc(name, price1, price2);
    }

    public void addSearchElement(SearchElemetData searchElemetData) {
        searchElementDataRepasitory.save(searchElemetData);
    }

    public List<SearchElemetData> getSearch() {
        return searchElementDataRepasitory.findAll();
    }

    public List<ShopItem> byPriceAscPriceBetween(int price1, int price2) {
        return itemRepasitories.findAllByPriceBetweenOrderByPriceAsc(price1, price2);
    }

    public List<ShopItem> NameStartingWith(String name) {
        return itemRepasitories.findAllByNameStartingWith(name);
    }

    public List<ShopItem> NameContaining(String name) {
        return itemRepasitories.findAllByNameContaining(name);
    }

    public List<ShopItem> NameContainingIgnoreCase(String name) {
        return itemRepasitories.findAllByNameContainingIgnoreCase(name);
    }

    public List<ShopItem> byPriceDescPriceBetween(int price1, int price2) {
        return itemRepasitories.findAllByPriceBetweenOrderByPriceDesc(price1, price2);
    }

    public List<ShopItem> byFromPriceDescPriceBetween(int price1) {
        return itemRepasitories.findAllByPriceIsGreaterThanOrderByPriceDesc(price1);
    }

    public List<ShopItem> byFromPriceAscPriceBetween(int price1) {
        return itemRepasitories.findAllByPriceIsGreaterThanOrderByPriceAsc(price1);
    }

    public List<ShopItem> byToPriceAscPriceBetween(int price2) {
        return itemRepasitories.findAllByPriceIsLessThanOrderByPriceAsc(price2);
    }

    public List<ShopItem> byToPriceDescPriceBetween(int price2) {
        return itemRepasitories.findAllByPriceIsLessThanOrderByPriceDesc(price2);
    }

    public List<Countries> getCountries() {
        return countriesRepasitory.findAll();
    }

    public ShopItem getShopItem(Long id) {
        return itemRepasitories.findById(id).orElse(null);
    }

    public Brands getBrand(Long id) {
        return brandsRepasitory.findById(id).orElse(null);
    }

    public void saveItem(ShopItem item) {
        itemRepasitories.save(item);
    }

    public void deleteItem(ShopItem item) {
        itemRepasitories.delete(item);
    }

    public Categories getCategories(Long id) {
        return categoriesRepasitory.findById(id).orElse(null);

    }

    public Countries getCountry(Long id) {
        return countriesRepasitory.findById(id).orElse(null);
    }

    public void saveCountries(Countries countries) {
        countriesRepasitory.save(countries);
    }

    public void deleteCountry(Countries countries) {
        countriesRepasitory.delete(countries);
    }

    public void deleteBrand(Brands brand) {
        brandsRepasitory.delete(brand);
    }

    public void saveBrand(Brands brand) {
        brandsRepasitory.save(brand);
    }

    public void deleteCategories(Categories categories) {
        categoriesRepasitory.delete(categories);
    }

    public void saveCategories(Categories categories) {
        categoriesRepasitory.save(categories);
    }

    public List<Pictures> getPictures(Long id) {
        return pictureRepasitory.findByItem_Id(id);
    }

    public List<CommentDto> getCommentsDto(Long item_id) {
        ShopItem item = itemRepasitories.getById(item_id);
        if (item != null) {
            List<Comments> commentsList = commentsRepasitory.findAllByItemOrderByAddedDateDesc(item);
            if (!CollectionUtils.isEmpty(commentsList)) {
                return commentMapper.toCommentDtoList(commentsList);
            }
        }
        return null;
    }

    public Comments getComment(Long id) {
        return commentsRepasitory.getById(id);
    }

    public void deleteComment(Long id) {

        commentsRepasitory.deleteById(id);
    }

    public Comments addComment(Comments comments) {
        commentsRepasitory.save(comments);
        return comments;
    }


}
