package com.caophu2305.popfigure.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caophu2305.popfigure.dto.supplier.SupplierRequest;
import com.caophu2305.popfigure.dto.supplier.SupplierResponse;
import com.caophu2305.popfigure.entity.Supplier;
import com.caophu2305.popfigure.exception.ResourceNotFoundException;
import com.caophu2305.popfigure.repository.SupplierRepository;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> getAll() {
        return supplierRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SupplierResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public SupplierResponse create(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.name());
        supplier.setContactInfo(request.contactInfo());
        return toResponse(supplierRepository.save(supplier));
    }

    public SupplierResponse update(Long id, SupplierRequest request) {
        Supplier supplier = findEntity(id);
        supplier.setName(request.name());
        supplier.setContactInfo(request.contactInfo());
        return toResponse(supplierRepository.save(supplier));
    }

    public void delete(Long id) {
        supplierRepository.delete(findEntity(id));
    }

    @Transactional(readOnly = true)
    public Supplier findEntity(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + id));
    }

    private SupplierResponse toResponse(Supplier supplier) {
        return new SupplierResponse(
                supplier.getId(),
                supplier.getName(),
                supplier.getContactInfo(),
                supplier.getCreatedAt(),
                supplier.getUpdatedAt()
        );
    }
}

