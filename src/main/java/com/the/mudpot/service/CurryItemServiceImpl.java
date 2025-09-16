package com.the.mudpot.service;

import com.the.mudpot.dto.CurryItemDTO;
import com.the.mudpot.model.CurryItem;
import com.the.mudpot.repository.CurryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurryItemServiceImpl implements CurryItemService {

    @Autowired
    private CurryItemRepository repo;

    @Autowired
    private LocalStorageService storageService;

    @Transactional
    @Override
    public CurryItemDTO create(CurryItemDTO req, MultipartFile image) {
        if (repo.existsByNameIgnoreCase(req.getName())) {
            throw new IllegalArgumentException("Curry item already exists: " + req.getName());
        }

        String imageUrl = (image != null && !image.isEmpty())
                ? storageService.storeImage(image)
                : null;

        CurryItem entity = CurryItem.builder()
                .name(req.getName())
                .price(req.getPrice())
                .description(req.getDescription())
                .availability(req.getAvailability())
                .imageUrl(imageUrl)
                .build();

        return CurryItemDTO.fromEntity(repo.save(entity));
    }

    @Override
    public CurryItemDTO update(String id, CurryItemDTO dto) {
        CurryItem item = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found with id " + id));
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setDescription(dto.getDescription());
        item.setAvailability(dto.getAvailability());
        return CurryItemDTO.fromEntity(repo.save(item));
    }

    @Override
    public void delete(String id) {
        if (!repo.existsById(id)) throw new RuntimeException("Not found with id " + id);
        repo.deleteById(id);
    }

    @Override
    public CurryItemDTO getById(String id) {
        return repo.findById(id)
                .map(CurryItemDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Not found with id " + id));
    }

    @Override
    public List<CurryItemDTO> listAll() {
        return repo.findAll().stream()
                .map(CurryItemDTO::fromEntity)
                .toList();
    }
}

