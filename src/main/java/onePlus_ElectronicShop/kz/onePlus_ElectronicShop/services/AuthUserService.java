package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.services;

import lombok.RequiredArgsConstructor;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.model.*;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories.PasswordHistoryRepasitory;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories.RolesRepasitory;
import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.repositories.UserRepasitory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserService implements UserDetailsService {

    private final UserRepasitory userRepasitory;
    private final RolesRepasitory rolesRepasitory;
    private final PasswordHistoryRepasitory passwordHistoryRepasitory;


    public Users findUserByEmail(String email) {
        return userRepasitory.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepasitory.findByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("USER NOT FOUND");
        }


    }

    public Users registerUser(Users user) {
        Roles role = rolesRepasitory.findByRole("ROLE_USER");
        if (role != null) {
            List<Roles> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);

            Users newUser = userRepasitory.save(user);
            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setSaveDate(new Date());
            passwordHistory.setPassword(newUser.getPassword());
            passwordHistory.setUser(newUser);
            passwordHistoryRepasitory.save(passwordHistory);
            return newUser;
        }
        return null;
    }


    public Users saveUser(Users user) {
        return userRepasitory.save(user);
    }

    public List<PasswordHistory> getAllPasswordHistory(Users user) {
        return passwordHistoryRepasitory.findAllByUser(user);
    }

    public void addPasswordHistory(PasswordHistory history) {
        passwordHistoryRepasitory.save(history);
    }

    public List<Users> getUsers() {
        return userRepasitory.findAll();
    }

    public List<Roles> getRoles() {
        return rolesRepasitory.findAll();
    }

    public Users getUser(Long id) {
        return userRepasitory.findById(id).orElse(null);
    }

    public Roles getRole(Long id) {
        return rolesRepasitory.getById(id);
    }

    public void addRole(Roles role) {
        rolesRepasitory.save(role);
    }

    public void deleteRole(Roles role) {
        rolesRepasitory.delete(role);
    }


}
