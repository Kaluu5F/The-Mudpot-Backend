package com.the.mudpot.service;

import com.the.mudpot.model.MenuItem;
import com.the.mudpot.model.Role;
import com.the.mudpot.model.User;
import com.the.mudpot.repository.MenuItemRepository;
import com.the.mudpot.repository.RoleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.the.mudpot.config.AppResources;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class AppInitializerService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @PostConstruct
    public void init() {
        try {

            this.createMenuItemsIfNotExists();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void createMenuItemsIfNotExists() throws IOException {
        log.info("Seeding default menu items if not present");

        String jsonContent = AppResources.asString("default/menu-items.json")
                .orElseThrow(() -> new RuntimeException("Error reading JSON file: default/menu-items.json"));

        ObjectMapper objectMapper = new ObjectMapper();
        List<MenuItem> items = objectMapper.readValue(
                jsonContent,
                new com.fasterxml.jackson.core.type.TypeReference<List<MenuItem>>() {}
        );

        int inserted = 0;
        for (MenuItem item : items) {
            if (item.getTags() == null) item.setTags(new java.util.HashSet<>());

            boolean exists = menuItemRepository.existsBySku(item.getSku());

            if (!exists) {
                item.setId(null);
                menuItemRepository.save(item);
                inserted++;
                log.info("Seeded: {} ({})", item.getName(), item.getSku());
            } else {
                log.info("Exists, skipping: {} ({})", item.getName(), item.getSku());
            }
        }
        log.info("Menu item seeding done. Inserted: {}", inserted);
    }


//    public void createAgencyIfNotExists() throws IOException {
//        log.info("Creating default car colors");
//        String jsonContent = AppResources.asString("default/agency.json").orElseThrow(() ->
//                new RuntimeException("Error reading JSON file")
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        User user = objectMapper.readValue(
//                jsonContent,
//                new TypeReference<User>() {}
//        );
//        Role role = roleRepository.findByRole("agency");
//        user.setRole(role);
//        if(userService.findByEmailAddress(user.getEmailAddress())==null){
//            userService.saveAgency(user);
//        }
//    }


}
