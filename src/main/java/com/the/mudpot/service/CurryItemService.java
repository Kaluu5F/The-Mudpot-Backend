package com.the.mudpot.service;

import com.the.mudpot.dto.CurryItemDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CurryItemService {
    CurryItemDTO create(CurryItemDTO req, MultipartFile image);
    CurryItemDTO update(String id, CurryItemDTO dto);
    void delete(String id);
    CurryItemDTO getById(String id);
    List<CurryItemDTO> listAll();
}
