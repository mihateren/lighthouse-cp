package ru.mai.lighthouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lighthouse.entity.api.admin.AdminRequest;
import ru.mai.lighthouse.entity.api.admin.AdminResponse;
import ru.mai.lighthouse.entity.api.batch.BatchError;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.repository.admin.AdminRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminResponse create(AdminRequest request) {
        log.info("Создание админа: {} {}", request.getFirstName(), request.getLastName());
        return adminRepository.create(request);
    }

    public AdminResponse getById(Long id) {
        log.info("Получение админа по ID: {}", id);
        return adminRepository.getById(id)
                .orElseThrow(() -> new ru.mai.lighthouse.exception.NotFoundException(
                        String.format("Админ с ID %d не найден", id)));
    }

    public List<AdminResponse> getAll() {
        log.info("Получение всех админов");
        return adminRepository.getAll();
    }

    public List<AdminResponse> getByCenter(Long centerId) {
        log.info("Получение админов по центру: {}", centerId);
        return adminRepository.getByCenter(centerId);
    }

    public AdminResponse update(Long id, AdminRequest request) {
        log.info("Обновление админа: {}", id);
        return adminRepository.update(id, request);
    }

    public void delete(Long id) {
        log.info("Удаление админа: {}", id);
        adminRepository.delete(id);
    }

    public BatchResponse<AdminResponse> batchCreate(BatchRequest<AdminRequest> request) {
        log.info("Пакетное создание админов: {} элементов", request.getItems().size());

        List<AdminResponse> successItems = request.getItems().stream()
                .map(adminRepository::create)
                .toList();

        return new BatchResponse<AdminResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(0)
                .setSuccessItems(successItems)
                .setErrors(List.of());
    }

    public BatchResponse<AdminResponse> batchUpdate(BatchRequest<AdminRequest> request) {
        log.info("Пакетное обновление админов: {} элементов", request.getItems().size());

        List<BatchError> errors = IntStream.range(0, request.getItems().size())
                .mapToObj(index -> new BatchError(index, "Для обновления требуется ID админа"))
                .collect(Collectors.toList());

        return new BatchResponse<AdminResponse>()
                .setSuccessCount(0)
                .setErrorCount(request.getItems().size())
                .setSuccessItems(List.of())
                .setErrors(errors);
    }
}
