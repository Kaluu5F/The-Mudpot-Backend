package com.the.mudpot.service;

import com.the.mudpot.model.CurryItem;
import com.the.mudpot.model.MenuItem;
import com.the.mudpot.model.Role;
import com.the.mudpot.model.User;
import com.the.mudpot.repository.CurryItemRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class AppInitializerService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CurryItemRepository curryItemRepository;

    @PostConstruct
    public void init() {
        try {

            this.createMenuItemsIfNotExists();
            this.createCurryItemsIfNotExists();
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


    public void createCurryItemsIfNotExists() throws IOException {
        log.info("Seeding default curry items if not present");

        String jsonContent = AppResources.asString("default/curry-items.json")
                .orElseThrow(() -> new RuntimeException("Error reading JSON file: default/curry-items.json"));

        ObjectMapper objectMapper = new ObjectMapper();
        List<CurryItem> items = objectMapper.readValue(
                jsonContent,
                new com.fasterxml.jackson.core.type.TypeReference<List<CurryItem>>() {
                }
        );

        int inserted = 0;
        for (CurryItem item : items) {

            boolean exists = curryItemRepository.existsByNameIgnoreCase(item.getName());

            if (!exists) {
                item.setId(null);
                curryItemRepository.save(item);
                inserted++;
                log.info("Seeded curry: {}", item.getName());
            } else {
                log.info("Exists, skipping: {}", item.getName());
            }
            log.info("Curry item seeding done. Inserted: {}", inserted);
        }
    }



}
