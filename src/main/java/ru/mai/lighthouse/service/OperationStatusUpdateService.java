package ru.mai.lighthouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lighthouse.entity.api.batch.BatchError;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.status.AdminDecisionRequest;
import ru.mai.lighthouse.entity.api.status.OperationStatusUpdateRequest;
import ru.mai.lighthouse.entity.api.status.OperationStatusUpdateResponse;
import ru.mai.lighthouse.repository.operation_status_update.OperationStatusUpdateRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationStatusUpdateService {

    private final OperationStatusUpdateRepository operationStatusUpdateRepository;

    public OperationStatusUpdateResponse create(OperationStatusUpdateRequest request) {
        log.info("Создание предложения изменения статуса для операции: {}", request.getOperationId());
        return operationStatusUpdateRepository.create(request);
    }

    public OperationStatusUpdateResponse getById(Long id) {
        log.info("Получение предложения по ID: {}", id);
        return operationStatusUpdateRepository.getById(id)
                .orElseThrow(() -> new ru.mai.lighthouse.exception.NotFoundException(
                        String.format("Предложение с ID %d не найдено", id)));
    }

    public List<OperationStatusUpdateResponse> getAll() {
        log.info("Получение всех предложений");
        return operationStatusUpdateRepository.getAll();
    }

    public List<OperationStatusUpdateResponse> getByOperation(Long operationId) {
        log.info("Получение предложений по операции: {}", operationId);
        return operationStatusUpdateRepository.getByOperation(operationId);
    }

    public List<OperationStatusUpdateResponse> getPending() {
        log.info("Получение ожидающих решения предложений");
        return operationStatusUpdateRepository.getPending();
    }

    public OperationStatusUpdateResponse decideStatusUpdate(Long id, AdminDecisionRequest request) {
        log.info("Принятие решения по предложению: {}", id);

        OperationStatusUpdateResponse response = operationStatusUpdateRepository.updateDecision(
                id,
                request.getDecision(),
                request.getAdminNotes()
        );

        // Если решение одобрено, обновляем статус операции
        Optional.of(request)
                .filter(req -> "approved".equals(req.getDecision()))
                .map(req -> response)
                .ifPresent(update -> operationStatusUpdateRepository.updateOperationStatus(
                        update.getOperationId(),
                        update.getProposedStatus()
                ));

        return response;
    }

    public BatchResponse<OperationStatusUpdateResponse> batchCreate(BatchRequest<OperationStatusUpdateRequest> request) {
        log.info("Пакетное создание предложений: {} элементов", request.getItems().size());

        List<OperationStatusUpdateResponse> successItems = request.getItems().stream()
                .map(operationStatusUpdateRepository::create)
                .toList();

        return new BatchResponse<OperationStatusUpdateResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(0)
                .setSuccessItems(successItems)
                .setErrors(List.of());
    }
}
