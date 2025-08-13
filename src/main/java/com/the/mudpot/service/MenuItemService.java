package com.the.mudpot.service;

import com.the.mudpot.ResponseHandler.CustomException;
import com.the.mudpot.dto.MenuItemDTO;
import com.the.mudpot.dto.UserDTO;
import com.the.mudpot.model.MenuItem;
import com.the.mudpot.model.Session;
import com.the.mudpot.model.User;
import com.the.mudpot.repository.MenuItemRepository;
import com.the.mudpot.repository.UserRepository;
import com.the.mudpot.util.MenuItemSpecification;
import com.the.mudpot.util.Messages;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocalStorageService storageService;

    @Transactional
    public MenuItemDTO create(MenuItemDTO req, MultipartFile image) {
        if (req.getSku() != null && !req.getSku().isBlank() && menuItemRepository.existsBySku(req.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }

        String imageUrl = image != null && !image.isEmpty() ? storageService.storeImage(image) : null;

        MenuItem entity = MenuItem.builder()
                .name(req.getName())
                .category(req.getCategory())
                .description(req.getDescription())
                .price(req.getPrice())
                .currency(req.getCurrency())
                .availability(req.getAvailability())
                .sku(req.getSku())
                .vegetarian(req.isVegetarian())
                .vegan(req.isVegan())
                .glutenFree(req.isGlutenFree())
                .lactoseFree(req.isLactoseFree())
                .spicyLevel(req.getSpicyLevel())
                .calories(req.getCalories())
                .prepTimeMinutes(req.getPrepTimeMinutes())
                .imageUrl(imageUrl)
                .tags(req.getTags() != null ? new HashSet<>(req.getTags()) : new HashSet<>())
                .build();

        return MenuItemDTO.init(menuItemRepository.save(entity));
    }


    public MenuItemDTO getById(String id) {
        MenuItem entity = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found: " + id));
        return MenuItemDTO.init(entity);
    }


    public Page<MenuItemDTO> list(Pageable pageable) {
        return menuItemRepository.findAll(pageable).map(MenuItemDTO::init);
    }

    @Transactional
    public MenuItemDTO update(String id, MenuItemDTO req, MultipartFile image) {
        MenuItem entity = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found: " + id));

        // SKU uniqueness if changed
        if (req.getSku() != null && !req.getSku().isBlank()
                && !req.getSku().equals(entity.getSku())
                && menuItemRepository.existsBySku(req.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }

        // Update fields
        entity.setName(req.getName());
        entity.setCategory(req.getCategory());
        entity.setDescription(req.getDescription());
        entity.setPrice(req.getPrice());
        entity.setCurrency(req.getCurrency());
        entity.setAvailability(req.getAvailability());
        entity.setSku(req.getSku());
        entity.setVegetarian(req.isVegetarian());
        entity.setVegan(req.isVegan());
        entity.setGlutenFree(req.isGlutenFree());
        entity.setLactoseFree(req.isLactoseFree());
        entity.setSpicyLevel(req.getSpicyLevel());
        entity.setCalories(req.getCalories());
        entity.setPrepTimeMinutes(req.getPrepTimeMinutes());
        entity.setTags(req.getTags() != null ? new HashSet<>(req.getTags()) : new HashSet<>());

        // Optional image replacement
        if (image != null && !image.isEmpty()) {
            String imageUrl = storageService.storeImage(image);
            entity.setImageUrl(imageUrl);
        }

        return MenuItemDTO.init(menuItemRepository.save(entity));
    }


    public void delete(String id) {
        if (!menuItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu item not found: " + id);
        }
        menuItemRepository.deleteById(id);
    }

}

