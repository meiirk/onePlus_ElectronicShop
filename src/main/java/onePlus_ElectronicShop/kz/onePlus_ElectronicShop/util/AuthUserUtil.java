package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.util;

import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.Users;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUserUtil {

    protected Users getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users authUser=(Users) authentication.getPrincipal();
            return authUser;
        }
        return null;
    }
}
