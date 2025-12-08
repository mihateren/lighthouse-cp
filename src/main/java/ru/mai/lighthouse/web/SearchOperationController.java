package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.operation.SearchOperationRequest;
import ru.mai.lighthouse.entity.api.operation.SearchOperationResponse;
import ru.mai.lighthouse.service.SearchOperationService;

import java.util.List;
import java.util.stream.IntStream;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Search Operations", description = "API для работы с операциями поиска")
@RequiredArgsConstructor
public class SearchOperationController {

    private final SearchOperationService searchOperationService;

    public static final String BASE_PREFIX = API_V1 + "/operations";
    public static final String CREATE_OPERATION = BASE_PREFIX;
    public static final String GET_OPERATION = BASE_PREFIX + "/{id}";
    public static final String UPDATE_OPERATION = BASE_PREFIX + "/{id}";
    public static final String DELETE_OPERATION = BASE_PREFIX + "/{id}";
    public static final String GET_ALL_OPERATIONS = BASE_PREFIX;
    public static final String GET_OPERATIONS_BY_STATUS = BASE_PREFIX + "/status/{status}";
    public static final String GET_OPERATIONS_BY_VICTIM = BASE_PREFIX + "/victim/{victimId}";
    public static final String GET_OPERATIONS_BY_ADMIN = BASE_PREFIX + "/admin/{adminId}";
    public static final String BATCH_CREATE = BASE_PREFIX + "/batch/create";
    public static final String BATCH_UPDATE = BASE_PREFIX + "/batch/update";
    public static final String GET_BATCH_EXAMPLES = BASE_PREFIX + "/batch/examples";

    @PostMapping(CREATE_OPERATION)
    @Operation(summary = "Создание операции поиска", description = "Создает новую операцию поиска пропавшего человека")
    public Result<SearchOperationResponse> createOperation(@Valid @RequestBody SearchOperationRequest request) {
        return Result.success(searchOperationService.create(request));
    }

    @GetMapping(GET_OPERATION)
    @Operation(summary = "Получение операции по ID", description = "Возвращает информацию об операции поиска")
    public Result<SearchOperationResponse> getOperation(@PathVariable Long id) {
        return Result.success(searchOperationService.getById(id));
    }

    @GetMapping(GET_ALL_OPERATIONS)
    @Operation(summary = "Получение всех операций", description = "Возвращает список всех операций поиска")
    public Result<List<SearchOperationResponse>> getAllOperations() {
        return Result.success(searchOperationService.getAll());
    }

    @GetMapping(GET_OPERATIONS_BY_STATUS)
    @Operation(summary = "Получение операций по статусу", description = "Возвращает список операций с указанным статусом")
    public Result<List<SearchOperationResponse>> getOperationsByStatus(@PathVariable String status) {
        return Result.success(searchOperationService.getByStatus(status));
    }

    @GetMapping(GET_OPERATIONS_BY_VICTIM)
    @Operation(summary = "Получение операций по пропавшему", description = "Возвращает список операций для указанного пропавшего")
    public Result<List<SearchOperationResponse>> getOperationsByVictim(@PathVariable Long victimId) {
        return Result.success(searchOperationService.getByVictim(victimId));
    }

    @GetMapping(GET_OPERATIONS_BY_ADMIN)
    @Operation(summary = "Получение операций по админу", description = "Возвращает список операций, созданных указанным админом")
    public Result<List<SearchOperationResponse>> getOperationsByAdmin(@PathVariable Long adminId) {
        return Result.success(searchOperationService.getByAdmin(adminId));
    }

    @PutMapping(UPDATE_OPERATION)
    @Operation(summary = "Обновление операции", description = "Обновляет информацию об операции поиска")
    public Result<SearchOperationResponse> updateOperation(@PathVariable Long id, @Valid @RequestBody SearchOperationRequest request) {
        return Result.success(searchOperationService.update(id, request));
    }

    @DeleteMapping(DELETE_OPERATION)
    @Operation(summary = "Удаление операции", description = "Удаляет операцию поиска")
    public Result<Void> deleteOperation(@PathVariable Long id) {
        searchOperationService.delete(id);
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание операций", description = "Создает несколько операций поиска за один запрос")
    public Result<BatchResponse<SearchOperationResponse>> batchCreateOperations(@Valid @RequestBody BatchRequest<SearchOperationRequest> request) {
        List<SearchOperationResponse> successItems = new java.util.ArrayList<>();
        List<ru.mai.lighthouse.entity.api.batch.BatchError> errors = new java.util.ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            try {
                SearchOperationResponse response = searchOperationService.create(request.getItems().get(i));
                successItems.add(response);
            } catch (Exception e) {
                errors.add(new ru.mai.lighthouse.entity.api.batch.BatchError(i, e.getMessage()));
            }
        }

        BatchResponse<SearchOperationResponse> batchResponse = new BatchResponse<SearchOperationResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(errors.size())
                .setSuccessItems(successItems)
                .setErrors(errors);

        return Result.success(batchResponse);
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление операций", description = "Обновляет несколько операций поиска за один запрос")
    public Result<BatchResponse<SearchOperationResponse>> batchUpdateOperations(@Valid @RequestBody BatchRequest<SearchOperationRequest> request) {
        return Result.success(searchOperationService.batchUpdate(request));
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Генерирует примеры DTO для пакетных операций")
    public Result<List<SearchOperationRequest>> getBatchExamples() {
        List<SearchOperationRequest> examples = IntStream.range(1, 6)
                .mapToObj(i -> new SearchOperationRequest()
                        .setVictimId((long) i)
                        .setAdminId(1L)
                        .setStatus("active"))
                .toList();
        return Result.success(examples);
    }
}

