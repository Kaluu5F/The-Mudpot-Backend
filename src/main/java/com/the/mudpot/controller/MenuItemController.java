package com.the.mudpot.controller;

import com.the.mudpot.dto.MenuItemDTO;
import com.the.mudpot.model.MenuItem;
import com.the.mudpot.model.Session;
import com.the.mudpot.model.User;
import com.the.mudpot.service.MenuItemService;
import com.the.mudpot.service.FileStorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private static final Logger log = LoggerFactory.getLogger(MenuItemController.class);
    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private FileStorageService fileStorageService;


//    @PostMapping
//    public ResponseEntity<MenuItemDTO> addDisaster(
//            @RequestPart("disaster") MenuItem menuItem,
//            @RequestParam("image") MultipartFile image) {
//
//        if (!image.isEmpty()) {
//            String imageUrl = fileStorageService.storeFile(image);
//            menuItem.setImageUrl(imageUrl);
//        }
//        MenuItem savedMenuItem = menuItemService.addDisaster(menuItem);
//
//        MenuItemDTO menuItemDTO = MenuItemDTO.init(savedMenuItem);
//        return ResponseEntity.ok(menuItemDTO);
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemDTO create(
            @Valid @ModelAttribute MenuItemDTO request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return menuItemService.create(request, image);
    }


    @GetMapping("/{id}")
    public MenuItemDTO getById(@PathVariable String id) {
        return menuItemService.getById(id);
    }

    @GetMapping
    public Page<MenuItemDTO> list(Pageable pageable) {
        return menuItemService.list(pageable);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MenuItemDTO update(
            @PathVariable String id,
            @Valid @ModelAttribute MenuItemDTO request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return menuItemService.update(id, request, image);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        menuItemService.delete(id);
    }



}

