package ar.com.kike.oauthserver;

import ar.com.kike.oauthserver.exception.AppException;
import ar.com.kike.oauthserver.model.Role;
import ar.com.kike.oauthserver.model.RoleName;
import ar.com.kike.oauthserver.model.User;
import ar.com.kike.oauthserver.repository.RoleRepository;
import ar.com.kike.oauthserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void loadInitialData(){
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()){ //no tengo ningun Role cargado, entonces cargar los 2 basicos.
            Role roleUser = new Role(RoleName.ROLE_USER);
            roleRepository.save(roleUser);
            Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
            roleRepository.save(roleAdmin);
        }
        Optional<User> userAdmin = userRepository.findByUsername("Administrator");

        if (!userAdmin.isPresent()){ //si el usuario administrador no existe, entonces crearlo
            List<User> userList = new ArrayList<>();
            User user = new User("Administrator", "Administrator",
                    "enriquericagno@gmail.com", "123456");

            user.setPassword(passwordEncoder.encode("123456"));
            Role userRoleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException("Admin Role not set."));
            Role userRoleUser = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));
            user.setRoles(Collections.singleton(userRoleAdmin));

            userList.add(user);
            User john = new User("john", "john",
                    "enriquericagno1@gmail.com", "123");

            john.setPassword(passwordEncoder.encode("123*"));
//            john.setPassword("123");
            john.setRoles(Collections.singleton(userRoleUser));
            userList.add(john);

            User user1 = new User("user1", "user1",
                    "enriquericagno2@gmail.com", "pass");

            user1.setPassword(passwordEncoder.encode("pass"));
            user1.setRoles(Collections.singleton(userRoleUser));
            userList.add(user1);

            User tom = new User("tom", "tom",
                    "enriquericagno3@gmail.com", "111");

            tom.setPassword(passwordEncoder.encode("111"));
            tom.setRoles(Collections.singleton(userRoleAdmin));
            userList.add(tom);

            User admin = new User("admin", "admin",
                    "enriquericagno4@gmail.com", "nimda");

            admin.setPassword(passwordEncoder.encode("nimda"));
            admin.setRoles(Collections.singleton(userRoleAdmin));
            userList.add(admin);
            for (User usuario : userList)  userRepository.save(usuario);
        }
    }
}
