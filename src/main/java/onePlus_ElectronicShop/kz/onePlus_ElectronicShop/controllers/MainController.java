package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.controllers;

import lombok.RequiredArgsConstructor;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.*;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.services.AuthUserService;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.services.OrderService;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.services.ShopItemService;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.util.AuthUserUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController extends AuthUserUtil {

    private final ShopItemService shopItemService;
    private final AuthUserService authUserServise;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;


    @GetMapping(value = "/")
    public String index(Model model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }

        model.addAttribute("amount", amount);
        model.addAttribute("currentUser", getCurrentUser());
        List<ShopItem> shopItems = shopItemService.getAllItems();
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("shopItems", shopItems);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);

        return "index";
    }

    @GetMapping(value = "/details/{id}")
    public String detais(Model model, @PathVariable(name = "id") Long id,
                         HttpServletRequest request
    ) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        model.addAttribute("currentUser", getCurrentUser());
        List<ShopItem> shopItems = shopItemService.getAllItems();
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        ShopItem item = shopItemService.getItem(id);
        model.addAttribute("item", item);
        return "details";
    }


    @GetMapping(value = "/search/{id}")
    public String search(Model model, @PathVariable(name = "id") Long id, HttpServletRequest request) {
        model.addAttribute("currentUser", getCurrentUser());
        List<ShopItem> shopItems = shopItemService.getAllItems();
        List<ShopItem> currentCategoryItems = new ArrayList<>();
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }

        model.addAttribute("amount", amount);

        for (ShopItem shopItem : shopItems) {
            List<Categories> categories = shopItem.getCategories();
            for (Categories c : categories) {
                if (c.getId().equals(id)) {
                    currentCategoryItems.add(shopItem);
                }
            }
        }


        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        ShopItem item = shopItemService.getItem(id);
        model.addAttribute("shopItems", currentCategoryItems);
        return "SearchResult";
    }

    @GetMapping(value = "/category/{id}")
    public String Category(Model model, @PathVariable(name = "id") Long id, HttpServletRequest request) {
        List<ShopItem> shopItems = shopItemService.getAllItems();
        List<ShopItem> currentCategoryItems = new ArrayList<>();
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }

        model.addAttribute("amount", amount);

        for (ShopItem shopItem : shopItems) {
            if (shopItem.getBrand().getId().equals(id)) {
                currentCategoryItems.add(shopItem);
            }
        }

        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        ShopItem item = shopItemService.getItem(id);
        model.addAttribute("shopItems", currentCategoryItems);
        return "SearchResult";
    }


    @GetMapping(value = "/search")
    public String AaddSearch(@RequestParam(name = "name") String name,

                             @RequestParam(name = "priceFrom") String priceFrom,
                             @RequestParam(name = "priceTo") String priceTo, Model model,
                             @RequestParam(name = "accAndDesc") String accAndDesc, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        int priceFromm = 0;
        int priceToo = 0;
        if (priceFrom != "") {
            priceFromm = Integer.parseInt(priceFrom);
        }
        if (priceTo != "") {
            priceToo = Integer.parseInt(priceTo);
        }


        model.addAttribute("amount", amount);
        List<ShopItem> shopItems = shopItemService.getAllItems();
        if ((!name.equals("")) && accAndDesc.equals("asc") && priceFrom.equals("") && priceTo.equals("")) {
            shopItems = shopItemService.byPriceAsc(name);
            if (shopItems.size() == 0) {
                shopItems = shopItemService.NameStartingWith(name);
            }
            if (shopItems.size() == 0) {
                shopItems = shopItemService.NameContaining(name);
            }
            if (shopItems.size() == 0) {
                shopItems = shopItemService.NameContainingIgnoreCase(name);
            }
        } else if ((!name.equals("")) && accAndDesc.equals("desc") && priceFrom.equals("") && priceTo.equals("")) {
            shopItems = shopItemService.byPriceDesc(name);
        } else if ((!name.equals("")) && (!priceFrom.equals("")) && (!priceTo.equals("")) && accAndDesc.equals("asc")) {
            shopItems = shopItemService.byPriceAscPriceBetween(name, priceFromm, priceToo);
        } else if (name.equals("") && (!priceFrom.equals("")) && (!priceTo.equals("")) && accAndDesc.equals("desc")) {
            shopItems = shopItemService.byPriceDescPriceBetween(name, priceFromm, priceToo);
        } else if (name.equals("") && (!priceFrom.equals("")) && (!priceTo.equals("")) && accAndDesc.equals("asc")) {
            shopItems = shopItemService.byPriceAscPriceBetween(priceFromm, priceToo);
        } else if (name.equals("") && (!priceFrom.equals("")) && priceTo.equals("") && accAndDesc.equals("desc")) {
            shopItems = shopItemService.byFromPriceDescPriceBetween(priceFromm);
        } else if (name.equals("") && (!priceFrom.equals("")) && priceTo.equals("") && accAndDesc.equals("asc")) {
            shopItems = shopItemService.byFromPriceAscPriceBetween(priceFromm);
        } else if (name.equals("") && priceFrom.equals("") && (!priceTo.equals("")) && accAndDesc.equals("asc")) {
            shopItems = shopItemService.byToPriceAscPriceBetween(priceToo);
        } else if (name.equals("") && priceFrom.equals("") && (!priceTo.equals("")) && accAndDesc.equals("desc")) {
            shopItems = shopItemService.byToPriceDescPriceBetween(priceToo);
        }

        model.addAttribute("shopItems", shopItems);
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "SearchResult";
    }


    @GetMapping(value = "signin")
    public String signIn(Model model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getCurrentUser());
        return "signin";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, HttpServletRequest request) {

        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("currentUser", getCurrentUser());
        return "profile";
    }

    @GetMapping(value = "/adminpanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String adminpanel(Model model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);

        List<Categories> categories = shopItemService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("currentUser", getCurrentUser());

        return "categories";
    }

    @GetMapping(value = "/categories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String categories(Model model, HttpServletRequest request) {
        List<Categories> categories = shopItemService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("currentUser", getCurrentUser());
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        return "categories";
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String users(Model model, HttpServletRequest request) {
        List<Users> users = authUserServise.getUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        return "users";
    }

    @GetMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String roles(Model model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        List<Roles> roles = authUserServise.getRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("currentUser", getCurrentUser());

        return "roles";
    }

    @GetMapping(value = "/itemsAdmin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String items(Model model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        List<ShopItem> items = shopItemService.getAllItems();
        model.addAttribute("items", items);
        model.addAttribute("currentUser", getCurrentUser());
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("brands", brands);
        return "items";
    }

    @GetMapping(value = "/countries")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String countries(Model model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        model.addAttribute("currentUser", getCurrentUser());
        List<Countries> countries = shopItemService.getCountries();
        model.addAttribute("countries", countries);

        return "countries";
    }

    @GetMapping(value = "/brands")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String brands(Model model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        model.addAttribute("currentUser", getCurrentUser());
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<Countries> countries = shopItemService.getCountries();
        model.addAttribute("countries", countries);
        return "brands";
    }


    @GetMapping(value = "/accesserror")
    public String accessError(Model model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("currentUser", getCurrentUser());
        return "accessdeniedpage";
    }


    @GetMapping(value = "/signup")
    public String signin(Model model, HttpServletRequest request) {

        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("currentUser", getCurrentUser());

        return "signup";
    }

    @PostMapping(value = "/signup")
    public String registrationUser
            (@RequestParam(name = "user_email") String email,
             @RequestParam(name = "user_password") String password,
             @RequestParam(name = "confirm_user_password") String confpassword,
             @RequestParam(name = "full_name") String fullname,
             Model model) {
        Users newUser = null;
        if (password.equals(confpassword)) {
            Users checkUser = authUserServise.findUserByEmail(email);
            if (checkUser == null) {
                Users user = new Users();
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));
                user.setFullName(fullname);
                user.setPictureURL("");
                authUserServise.registerUser(user);
                newUser = authUserServise.registerUser(user);
            }

            if (newUser != null) {
                return "redirect:/signup?success";
            }
            return "redirect:/signup?error";
        }
        model.addAttribute("currentUser", getCurrentUser());
        return "redirect:/";
    }

    @PostMapping(value = "/changePassword")
    @PreAuthorize("isAuthenticated()")
    public String changePass(Model model,
                             @RequestParam(name = "old_password") String oldPassword,
                             @RequestParam(name = "new_password") String newPassword,
                             @RequestParam(name = "retype_password") String retypepassword) {
        model.addAttribute("currentUser", getCurrentUser());


        if (newPassword.equals(retypepassword)) {
            Users user = getCurrentUser();
            PasswordHistory checkHistory = null;
            List<PasswordHistory> historyList = authUserServise.getAllPasswordHistory(user);
            if (!CollectionUtils.isEmpty(historyList)) {
                checkHistory = historyList.stream().filter(passwordHistory -> passwordEncoder.matches(newPassword, passwordHistory.getPassword())).findFirst().orElse(null);
            }
            if (checkHistory == null) {
                if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    authUserServise.saveUser(user);

                    PasswordHistory history = new PasswordHistory();
                    history.setUser(user);
                    history.setPassword(user.getPassword());
                    history.setSaveDate(new Date());
                    authUserServise.addPasswordHistory(history);
                }
            }
        }
        return "redirect:/profile";

    }

    @PostMapping(value = "/changePicture")
    @PreAuthorize("isAuthenticated()")
    public String changePic(Model model,
                            @RequestParam(name = "picture") MultipartFile file) {

        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {


        }
        try {
            Users user = getCurrentUser();

            String picname = DigestUtils.sha1Hex("avatar_" + user.getId() + "_!Picture");

            byte[] bytes = file.getBytes();

            Path path = Paths.get(uploadPath + picname + ".jpg");
            Files.write(path, bytes);
            user.setPictureURL(picname);
            authUserServise.saveUser(user);
            return "redirect:/profile?success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("currentUser", getCurrentUser());

        return "redirect:/profile";


    }

    @PostMapping(value = "/changeProfile")
    @PreAuthorize("isAuthenticated()")
    public String changeProfile(Model model,
                                @RequestParam(name = "fullname") String fullname) {

        model.addAttribute("currentUser", getCurrentUser());

        Users user = getCurrentUser();
        user.setFullName(fullname);
        authUserServise.saveUser(user);

        return "redirect:/";
    }

    @GetMapping(value = "/viewphoto/{url}", produces = (MediaType.IMAGE_JPEG_VALUE))
    public @ResponseBody
    byte[] viewPhoto(@PathVariable(name = "url") String url, HttpServletRequest request, Model model) throws IOException {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        String picUrl = viewPath + defaultPicture;
        if (getCurrentUser() != null) {
            picUrl = viewPath + url + ".jpg";
        }
        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(picUrl);
            in = resource.getInputStream();
        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/itemsDetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String itemDetails(Model model, @PathVariable(name = "id") Long id, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        ShopItem item = shopItemService.getShopItem(id);
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        if (item != null && categories != null) {
            categories.removeAll(item.getCategories());
        }

        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("item", item);
        List<Pictures> pictures = shopItemService.getPictures(id);
        model.addAttribute("pictures", pictures);
        return "itemsDetails";
    }

    @PostMapping(value = "/itemEdit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String saveItem(Model model,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "price") int price,
                           @RequestParam(name = "description") String description,
                           @RequestParam(name = "small_picture") String small_picture,
                           @RequestParam(name = "large_picture") String large_picture,
                           @RequestParam(name = "brand_id") Long brand_id,
                           @RequestParam(name = "rating") int rating,
                           @RequestParam(name = "is_top") boolean is_top
    ) {
        model.addAttribute("currentUser", getCurrentUser());

        ShopItem item = shopItemService.getItem(id);
        Brands brand = shopItemService.getBrand(brand_id);
        if (item != null) {
            item.setName(name);
            item.setPrice(price);
            item.setDescription(description);
            item.setSmallPicURL(small_picture);
            item.setLargePicURL(large_picture);
            item.setBrand(brand);
            item.setInTopPage(is_top);
            shopItemService.saveItem(item);
        }

        return "redirect:/itemsDetails/" + id;

    }

    @PostMapping(value = "/deleteItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteItem(Model model,
                             @RequestParam(name = "id") Long id) {
        model.addAttribute("currentUser", getCurrentUser());
        ShopItem item = shopItemService.getItem(id);

        shopItemService.deleteItem(item);

        return "redirect:/itemsAdmin";

    }

    @PostMapping(value = "/addItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addItem(Model model,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "description") String description,
                          @RequestParam(name = "small_picture") String small_picture,
                          @RequestParam(name = "large_picture") String large_picture,
                          @RequestParam(name = "brand_id") Long brand_id,
                          @RequestParam(name = "rating") int rating,
                          @RequestParam(name = "amount") int amount,
                          @RequestParam(name = "is_top") boolean is_top

    ) {
        Brands brand = shopItemService.getBrand(brand_id);
        ShopItem item = new ShopItem();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setAmount(amount);
        item.setStars(rating);
        item.setSmallPicURL(small_picture);
        item.setLargePicURL(large_picture);
        item.setInTopPage(is_top);
        item.setBrand(brand);
        shopItemService.saveItem(item);


        return "redirect:/itemsAdmin";

    }

    @PostMapping(value = "/asigncategories")
    public String asignSubject(@RequestParam(name = "categori_id") Long categ_id,
                               @RequestParam(name = "item_id") Long item_id) {

        ShopItem item = shopItemService.getItem(item_id);
        if (item != null) {

            Categories categories = shopItemService.getCategories(categ_id);
            List<Categories> categoriesList = item.getCategories();
            if (categoriesList == null) {
                categoriesList = new ArrayList<>();
            }
            categoriesList.add(categories);
            item.setCategories(categoriesList);
        }

        shopItemService.saveItem(item);
        return "redirect:/itemsDetails/" + item_id;
    }


    @PostMapping(value = "/unasignscategories")
    public String unasignSubject(@RequestParam(name = "categori_id") Long categ_id,
                                 @RequestParam(name = "item_id") Long item_id) {

        ShopItem item = shopItemService.getItem(item_id);
        if (item != null) {

            Categories categories = shopItemService.getCategories(categ_id);
            List<Categories> categoriesList = item.getCategories();
            if (categoriesList == null) {
                categoriesList = new ArrayList<>();
            }
            categoriesList.remove(categories);
            item.setCategories(categoriesList);
        }

        shopItemService.saveItem(item);
        return "redirect:/itemsDetails/" + item_id;
    }


    @PostMapping(value = "/addUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addUser(@RequestParam(name = "user_email") String email,
                          @RequestParam(name = "user_password") String password,
                          @RequestParam(name = "confirm_user_password") String confpassword,
                          @RequestParam(name = "full_name") String fullname,
                          @RequestParam(name = "picture_url") String picture_url,
                          Model model

    ) {

        if (password.equals(confpassword)) {

            Users checkUser = authUserServise.findUserByEmail(email);
            if (checkUser == null) {
                Users user = new Users();
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));
                user.setFullName(fullname);
                user.setPictureURL(picture_url);
                authUserServise.registerUser(user);
                Users newUser = authUserServise.registerUser(user);
                model.addAttribute("currentUser", getCurrentUser());


            }
        }
        return "redirect:/users";
    }

    @PostMapping(value = "/editUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String saveUser(@RequestParam(name = "user_email") String email,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "user_password") String password,
                           @RequestParam(name = "full_name") String fullname,
                           @RequestParam(name = "picture_url") String picture_url,
                           Model model
    ) {
        model.addAttribute("currentUser", getCurrentUser());

        Users user = authUserServise.getUser(id);

        if (user != null) {
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(fullname);
            user.setPictureURL(picture_url);
            authUserServise.saveUser(user);
        }

        return "redirect:/userDetails/" + id;

    }


    @PostMapping(value = "/asignroles")
    public String asignRole(@RequestParam(name = "role_id") Long role_id,
                            @RequestParam(name = "user_id") Long user_id) {


        Users user = authUserServise.getUser(user_id);
        if (user != null) {

            Roles role = authUserServise.getRole(role_id);
            List<Roles> roleList = user.getRoles();
            if (roleList == null) {
                roleList = new ArrayList<>();
            }
            roleList.add(role);
            user.setRoles(roleList);
        }

        authUserServise.saveUser(user);
        return "redirect:/userDetails/" + user_id;
    }


    @PostMapping(value = "/unasignroles")
    public String unasignRole(@RequestParam(name = "role_id") Long role_id,
                              @RequestParam(name = "user_id") Long user_id) {


        Users user = authUserServise.getUser(user_id);
        if (user != null) {

            Roles role = authUserServise.getRole(role_id);
            List<Roles> roleList = user.getRoles();
            if (roleList == null) {
                roleList = new ArrayList<>();
            }
            roleList.remove(role);
            user.setRoles(roleList);
        }

        authUserServise.saveUser(user);
        return "redirect:/userDetails/" + user_id;
    }


    @GetMapping(value = "/userDetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String userDetails(Model model, @PathVariable(name = "id") Long id, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        Users user = authUserServise.getUser(id);
        List<Roles> roles = authUserServise.getRoles();
        if (user != null && roles != null) {
            roles.removeAll(user.getRoles());
        }
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);

        return "userDtails";
    }

    @GetMapping(value = "/roleDetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String roleDetails(Model model, @PathVariable(name = "id") Long id, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);
        Users user = authUserServise.getUser(id);
        Roles role = authUserServise.getRole(id);
        model.addAttribute("role", role);

        return "roleDetails";
    }


    @PostMapping(value = "/addRole")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addRole(@RequestParam(name = "name") String name,
                          @RequestParam(name = "description") String description,
                          Model model) {
        Roles role = new Roles();
        role.setRole(name);
        role.setDescription(description);

        authUserServise.addRole(role);
        return "redirect:/roles";
    }


    @PostMapping(value = "/editRole")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String saveUser(@RequestParam(name = "name") String name,
                           @RequestParam(name = "description") String description,
                           @RequestParam(name = "id") Long id,
                           Model model) {
        Roles role = authUserServise.getRole(id);
        model.addAttribute("role", role);
        if (role != null) {

            role.setRole(name);
            role.setDescription(description);
            authUserServise.addRole(role);
        }

        return "redirect:/roleDetails/" + id;

    }


    @PostMapping(value = "/deleteRole")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteRole(Model model,
                             @RequestParam(name = "id") Long id) {

        Roles role = authUserServise.getRole(id);

        if (role != null) {
            authUserServise.deleteRole(role);
        }

        return "redirect:/roles";

    }


    @GetMapping(value = "/countryDetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String countryDetails(Model model, @PathVariable(name = "id") Long id, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);

        Countries country = shopItemService.getCountry(id);
        model.addAttribute("country", country);

        return "countryDetails";
    }

    @PostMapping(value = "/addCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCountry(@RequestParam(name = "name") String name,
                             @RequestParam(name = "code") String code,
                             Model model) {
        Countries countries = new Countries();
        countries.setName(name);
        countries.setCode(code);

        shopItemService.saveCountries(countries);
        return "redirect:/countries";
    }

    @PostMapping(value = "/editCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String saveCountry(@RequestParam(name = "name") String name,
                              @RequestParam(name = "code") String code,
                              @RequestParam(name = "id") Long id,
                              Model model) {
        Countries countries = shopItemService.getCountry(id);

        if (countries != null) {

            countries.setName(name);
            countries.setCode(code);
            shopItemService.saveCountries(countries);
        }

        return "redirect:/countryDetails/" + id;

    }


    @PostMapping(value = "/deleteCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCountry(Model model,
                                @RequestParam(name = "id") Long id) {

        Countries countries = shopItemService.getCountry(id);

        if (countries != null) {
            shopItemService.deleteCountry(countries);
        }

        return "redirect:/countries";

    }

    @GetMapping(value = "/brandDetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String brandDetails(Model model, @PathVariable(name = "id") Long id, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);

        Brands brand = shopItemService.getBrand(id);
        model.addAttribute("brand", brand);
        List<Countries> countries = shopItemService.getCountries();
        model.addAttribute("countries", countries);

        return "brandDetails";
    }


    @PostMapping(value = "/addBrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCountry(@RequestParam(name = "name") String name,
                             @RequestParam(name = "country_id") Long country_id,
                             Model model) {

        Countries country = shopItemService.getCountry(country_id);

        Brands brand = new Brands();
        if (country != null) {
            brand.setCountry(country);
            brand.setName(name);
            shopItemService.saveBrand(brand);
        }

        return "redirect:/brands";
    }


    @PostMapping(value = "/editBrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String saveCountry(@RequestParam(name = "name") String name,
                              @RequestParam(name = "country_id") Long country_id,
                              @RequestParam(name = "id") Long id,
                              Model model) {
        Brands brands = shopItemService.getBrand(id);
        Countries country = shopItemService.getCountry(country_id);

        if (brands != null) {

            brands.setName(name);
            brands.setCountry(country);
            shopItemService.saveBrand(brands);
        }

        return "redirect:/brandDetails/" + id;

    }


    @PostMapping(value = "/deleteBrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteBrand(Model model,
                              @RequestParam(name = "id") Long id) {

        Brands brands = shopItemService.getBrand(id);

        if (brands != null) {
            shopItemService.deleteBrand(brands);
        }

        return "redirect:/brands";

    }

    @GetMapping(value = "/categoriesDetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String categoriesDetails(Model model, @PathVariable(name = "id") Long id, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        int amount = 0;
        if (getCurrentUser() == null) {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length;
            }
        } else {
            if (cookies == null) {
                amount = 0;
            } else {
                amount = cookies.length - 1;
            }

        }
        model.addAttribute("amount", amount);

        Categories categories = shopItemService.getCategories(id);
        model.addAttribute("categories", categories);


        return "categoriesDetails";
    }


    @PostMapping(value = "/addCategories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCategories(@RequestParam(name = "name") String name,
                                @RequestParam(name = "logo_url") String logo_url,
                                Model model) {

        Categories categories = new Categories();
        categories.setName(name);
        categories.setLogoURL(logo_url);
        shopItemService.saveCategories(categories);
        return "redirect:/categories";
    }


    @PostMapping(value = "/editCategories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String saveCategories(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "logo_url") String logo_url,
                                 @RequestParam(name = "id") Long id,

                                 Model model) {
        Categories categories = shopItemService.getCategories(id);

        if (categories != null) {

            categories.setName(name);
            categories.setLogoURL(logo_url);
            shopItemService.saveCategories(categories);
        }

        return "redirect:/categoriesDetails/" + id;

    }


    @PostMapping(value = "/deleteCategories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCategoriesl(Model model,
                                    @RequestParam(name = "id") Long id) {

        Categories categories = shopItemService.getCategories(id);

        if (categories != null) {
            shopItemService.deleteCategories(categories);
        }

        return "redirect:/categories";

    }

    @PostMapping(value = "/addToBasket")
    public String addItemToBasket(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "name") String name,
            HttpServletResponse response) {
        String cookieName = name.replaceAll(" ", "");

        int random = (int) (Math.random() * 1000);
        cookieName += random;
        Cookie basket = new Cookie(cookieName, id);
        basket.setMaxAge(3600);
        ShopItem item = shopItemService.getItem(Long.parseLong(id));

        if (item.getAmount() != 0) {
            response.addCookie(basket);
        }

        return "redirect:/details/" + id;
    }

    @PostMapping(value = "/addAmount")
    public String addAmount(@RequestParam(name = "item_id") String id,
                            @RequestParam(name = "name") String name,
                            HttpServletResponse response, Model model) {


        String cookieName = name.replaceAll(" ", "");
        int random = (int) (Math.random() * 1000);
        cookieName += random;
        Cookie basket = new Cookie(cookieName, id);
        int amountOfItem = 1;
        basket.setMaxAge(3600);
        ShopItem item = shopItemService.getItem(Long.parseLong(id));
        if (item.getAmount() != 0) {
            response.addCookie(basket);
        }


        return "redirect:/getBasket";

    }

    @PostMapping(value = "/deleteAmount")
    public String minusAmount(@RequestParam(name = "item_id") String id,
                              @RequestParam(name = "name") String name,
                              HttpServletRequest request, HttpServletResponse response, Model model) {
        String cookieName = name.replaceAll(" ", "");


        Cookie cookies[] = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getValue().equals(id)) {
                cookies[i].setMaxAge(0);
                response.addCookie(cookies[i]);
                break;

            }
        }


        return "redirect:/getBasket";

    }

    @PostMapping(value = "/clearBasket")
    public String clearBasker(
            HttpServletRequest request, HttpServletResponse response, Model model) {

        Cookie cookies[] = request.getCookies();
        for (Cookie c : cookies) {
            c.setMaxAge(0);
            response.addCookie(c);
        }
        return "redirect:/getBasket";

    }


    @GetMapping("/getBasket")
    public String readCookie(Model model, HttpServletRequest request) {
        List<Categories> categories = shopItemService.getAllCategories();
        List<Brands> brands = shopItemService.getAllBrands();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);

        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            Long item_id[] = new Long[cookies.length];
            Map<String, Integer> amountOfItem = new HashMap<>();
            Map<ShopItem, Integer> items = new HashMap<>();

            for (int i = 0; i < cookies.length; i++) {
                if (!cookies[i].getName().equals("JSESSIONID")) {
                    item_id[i] = Long.parseLong(cookies[i].getValue());
                }
            }

            ShopItem[] basketItems = new ShopItem[item_id.length];
            int totalPrice = 0;
            if (item_id != null) {
                for (int j = 0; j < item_id.length; j++) {
                    if (item_id[j] != null) {
                        if (shopItemService.getItem(item_id[j]) != null) {
                            String id_key = String.valueOf(item_id[j]);
                            if (!amountOfItem.containsKey(id_key)) {
                                amountOfItem.put(id_key, 1);
                                basketItems[j] = shopItemService.getItem(item_id[j]);
                            } else {
                                amountOfItem.put(id_key, amountOfItem.get(id_key) + 1);
                            }
                        }
                        totalPrice += shopItemService.getItem(item_id[j]).getPrice();
                    }
                }
            }
            for (int i = 0; i < basketItems.length; i++) {
                if (basketItems[i] != null) {
                    items.put(basketItems[i], amountOfItem.get(String.valueOf(basketItems[i].getId())));
                }
            }

            model.addAttribute("items", items);
            int amount = 0;
            if (getCurrentUser() == null) {
                if (cookies == null) {
                    amount = 0;
                } else {
                    amount = cookies.length;
                }
            } else {
                if (cookies == null) {
                    amount = 0;
                } else {
                    amount = cookies.length - 1;
                }

            }
            model.addAttribute("amount", amount);

            model.addAttribute("totalPrice", totalPrice);


        }
        return "basketPage";


    }

    @PostMapping(value = "/checkIn")
    public String checkIn(@RequestParam(name = "by_items") Map<Integer, ShopItem> by_items,
                          @RequestParam(name = "fullName") String fullName,
                          @RequestParam(name = "totalPrice") int totalPrice) {

        List<ShopItem> shopItems = (List<ShopItem>) by_items.values();
        System.out.println(shopItems.get(0).getId());
        String itemName = "";
        for (int i = 0; i < shopItems.size(); i++) {
            itemName += shopItems.get(i).getName() + ",";
        }

        Orders newOrder = new Orders();
        newOrder.setFullName(fullName);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setItemsName(itemName);

        orderService.addOrder(newOrder);
        return "redirect:/getBasket";
    }


}


