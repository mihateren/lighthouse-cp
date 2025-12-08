package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.status.AdminDecisionRequest;
import ru.mai.lighthouse.entity.api.status.OperationStatusUpdateRequest;
import ru.mai.lighthouse.entity.api.status.OperationStatusUpdateResponse;
import ru.mai.lighthouse.service.OperationStatusUpdateService;

import java.util.List;
import java.util.stream.IntStream;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Operation Status Updates", description = "API для работы с предложениями изменения статуса операций")
@RequiredArgsConstructor
public class OperationStatusUpdateController {

    private final OperationStatusUpdateService operationStatusUpdateService;

    public static final String BASE_PREFIX = API_V1 + "/status-updates";
    public static final String CREATE_STATUS_UPDATE = BASE_PREFIX;
    public static final String GET_STATUS_UPDATE = BASE_PREFIX + "/{id}";
    public static final String GET_ALL_STATUS_UPDATES = BASE_PREFIX;
    public static final String GET_STATUS_UPDATES_BY_OPERATION = BASE_PREFIX + "/operation/{operationId}";
    public static final String GET_PENDING_STATUS_UPDATES = BASE_PREFIX + "/pending";
    public static final String DECIDE_STATUS_UPDATE = BASE_PREFIX + "/{id}/decide";
    public static final String BATCH_CREATE = BASE_PREFIX + "/batch/create";
    public static final String GET_BATCH_EXAMPLES = BASE_PREFIX + "/batch/examples";

    @PostMapping(CREATE_STATUS_UPDATE)
    @Operation(summary = "Создание предложения изменения статуса", 
               description = "Волонтер предлагает изменить статус операции (найден, погиб, закрыт)")
    public Result<OperationStatusUpdateResponse> createStatusUpdate(@Valid @RequestBody OperationStatusUpdateRequest request) {
        return Result.success(operationStatusUpdateService.create(request));
    }

    @GetMapping(GET_STATUS_UPDATE)
    @Operation(summary = "Получение предложения по ID", description = "Возвращает информацию о предложении изменения статуса")
    public Result<OperationStatusUpdateResponse> getStatusUpdate(@PathVariable Long id) {
        return Result.success(operationStatusUpdateService.getById(id));
    }

    @GetMapping(GET_ALL_STATUS_UPDATES)
    @Operation(summary = "Получение всех предложений", description = "Возвращает список всех предложений изменения статуса")
    public Result<List<OperationStatusUpdateResponse>> getAllStatusUpdates() {
        return Result.success(operationStatusUpdateService.getAll());
    }

    @GetMapping(GET_STATUS_UPDATES_BY_OPERATION)
    @Operation(summary = "Получение предложений по операции", description = "Возвращает список предложений для указанной операции")
    public Result<List<OperationStatusUpdateResponse>> getStatusUpdatesByOperation(@PathVariable Long operationId) {
        return Result.success(operationStatusUpdateService.getByOperation(operationId));
    }

    @GetMapping(GET_PENDING_STATUS_UPDATES)
    @Operation(summary = "Получение ожидающих решения предложений", 
               description = "Возвращает список предложений, ожидающих решения админа")
    public Result<List<OperationStatusUpdateResponse>> getPendingStatusUpdates() {
        return Result.success(operationStatusUpdateService.getPending());
    }

    @PutMapping(DECIDE_STATUS_UPDATE)
    @Operation(summary = "Принятие решения по предложению", 
               description = "Админ принимает решение по предложению изменения статуса (одобрить/отклонить)")
    public Result<OperationStatusUpdateResponse> decideStatusUpdate(
            @PathVariable Long id, 
            @Valid @RequestBody AdminDecisionRequest request) {
        return Result.success(operationStatusUpdateService.decideStatusUpdate(id, request));
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание предложений", description = "Создает несколько предложений изменения статуса за один запрос")
    public Result<BatchResponse<OperationStatusUpdateResponse>> batchCreateStatusUpdates(
            @Valid @RequestBody BatchRequest<OperationStatusUpdateRequest> request) {
        List<OperationStatusUpdateResponse> successItems = new java.util.ArrayList<>();
        List<ru.mai.lighthouse.entity.api.batch.BatchError> errors = new java.util.ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            try {
                OperationStatusUpdateResponse response = operationStatusUpdateService.create(request.getItems().get(i));
                successItems.add(response);
            } catch (Exception e) {
                errors.add(new ru.mai.lighthouse.entity.api.batch.BatchError(i, e.getMessage()));
            }
        }

        BatchResponse<OperationStatusUpdateResponse> batchResponse = new BatchResponse<OperationStatusUpdateResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(errors.size())
                .setSuccessItems(successItems)
                .setErrors(errors);

        return Result.success(batchResponse);
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Генерирует примеры DTO для пакетных операций")
    public Result<List<OperationStatusUpdateRequest>> getBatchExamples() {
        String[] statuses = {"found", "deceased", "closed"};
        List<OperationStatusUpdateRequest> examples = IntStream.range(0, 5)
                .mapToObj(i -> new OperationStatusUpdateRequest()
                        .setOperationId((long) (i + 1))
                        .setVolunteerId((long) (i + 1))
                        .setProposedStatus(statuses[i % statuses.length])
                        .setReason(String.format("Причина %d", i + 1)))
                .toList();
        return Result.success(examples);
    }
}

