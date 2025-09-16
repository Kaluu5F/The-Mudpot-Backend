package com.the.mudpot.controller;

import com.the.mudpot.dto.CurryItemDTO;
import com.the.mudpot.service.CurryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/curries")
@RequiredArgsConstructor
public class CurryItemController {

    private final CurryItemService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CurryItemDTO create(
            @Valid @ModelAttribute CurryItemDTO request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return service.create(request, image);
    }

    @PutMapping("/{id}")
    public CurryItemDTO update(@PathVariable String id, @Valid @RequestBody CurryItemDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public CurryItemDTO get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    public List<CurryItemDTO> listAll() {
        return service.listAll();
    }
}

