package ru.mai.lighthouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lighthouse.entity.api.batch.BatchError;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.operation.SearchOperationRequest;
import ru.mai.lighthouse.entity.api.operation.SearchOperationResponse;
import ru.mai.lighthouse.repository.search_operation.SearchOperationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchOperationService {

    private final SearchOperationRepository searchOperationRepository;

    public SearchOperationResponse create(SearchOperationRequest request) {
        log.info("Создание операции поиска для пропавшего: {}", request.getVictimId());
        return searchOperationRepository.create(request);
    }

    public SearchOperationResponse getById(Long id) {
        log.info("Получение операции по ID: {}", id);
        return searchOperationRepository.getById(id)
                .orElseThrow(() -> new ru.mai.lighthouse.exception.NotFoundException(
                        String.format("Операция с ID %d не найдена", id)));
    }

    public List<SearchOperationResponse> getAll() {
        log.info("Получение всех операций");
        return searchOperationRepository.getAll();
    }

    public List<SearchOperationResponse> getByStatus(String status) {
        log.info("Получение операций по статусу: {}", status);
        return searchOperationRepository.getByStatus(status);
    }

    public List<SearchOperationResponse> getByVictim(Long victimId) {
        log.info("Получение операций по пропавшему: {}", victimId);
        return searchOperationRepository.getByVictim(victimId);
    }

    public List<SearchOperationResponse> getByAdmin(Long adminId) {
        log.info("Получение операций по админу: {}", adminId);
        return searchOperationRepository.getByAdmin(adminId);
    }

    public SearchOperationResponse update(Long id, SearchOperationRequest request) {
        log.info("Обновление операции: {}", id);
        return searchOperationRepository.update(id, request);
    }

    public void delete(Long id) {
        log.info("Удаление операции: {}", id);
        searchOperationRepository.delete(id);
    }

    public BatchResponse<SearchOperationResponse> batchCreate(BatchRequest<SearchOperationRequest> request) {
        log.info("Пакетное создание операций: {} элементов", request.getItems().size());

        List<SearchOperationResponse> successItems = request.getItems().stream()
                .map(searchOperationRepository::create)
                .toList();

        return new BatchResponse<SearchOperationResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(0)
                .setSuccessItems(successItems)
                .setErrors(List.of());
    }

    public BatchResponse<SearchOperationResponse> batchUpdate(BatchRequest<SearchOperationRequest> request) {
        log.info("Пакетное обновление операций: {} элементов", request.getItems().size());

        List<BatchError> errors = IntStream.range(0, request.getItems().size())
                .mapToObj(index -> new BatchError(index, "Для обновления требуется ID операции"))
                .collect(Collectors.toList());

        return new BatchResponse<SearchOperationResponse>()
                .setSuccessCount(0)
                .setErrorCount(request.getItems().size())
                .setSuccessItems(List.of())
                .setErrors(errors);
    }
}
