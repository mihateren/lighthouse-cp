package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.operation.SearchOperationRequest;
import ru.mai.lighthouse.entity.api.operation.SearchOperationResponse;

import java.util.List;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Search Operations", description = "API для работы с операциями поиска")
public class SearchOperationController {

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
        return Result.success(new SearchOperationResponse());
    }

    @GetMapping(GET_OPERATION)
    @Operation(summary = "Получение операции по ID", description = "Возвращает информацию об операции поиска")
    public Result<SearchOperationResponse> getOperation(@PathVariable Long id) {
        return Result.success(new SearchOperationResponse());
    }

    @GetMapping(GET_ALL_OPERATIONS)
    @Operation(summary = "Получение всех операций", description = "Возвращает список всех операций поиска")
    public Result<List<SearchOperationResponse>> getAllOperations() {
        return Result.success(List.of());
    }

    @GetMapping(GET_OPERATIONS_BY_STATUS)
    @Operation(summary = "Получение операций по статусу", description = "Возвращает список операций с указанным статусом")
    public Result<List<SearchOperationResponse>> getOperationsByStatus(@PathVariable String status) {
        return Result.success(List.of());
    }

    @GetMapping(GET_OPERATIONS_BY_VICTIM)
    @Operation(summary = "Получение операций по пропавшему", description = "Возвращает список операций для указанного пропавшего")
    public Result<List<SearchOperationResponse>> getOperationsByVictim(@PathVariable Long victimId) {
        return Result.success(List.of());
    }

    @GetMapping(GET_OPERATIONS_BY_ADMIN)
    @Operation(summary = "Получение операций по админу", description = "Возвращает список операций, созданных указанным админом")
    public Result<List<SearchOperationResponse>> getOperationsByAdmin(@PathVariable Long adminId) {
        return Result.success(List.of());
    }

    @PutMapping(UPDATE_OPERATION)
    @Operation(summary = "Обновление операции", description = "Обновляет информацию об операции поиска")
    public Result<SearchOperationResponse> updateOperation(@PathVariable Long id, @Valid @RequestBody SearchOperationRequest request) {
        return Result.success(new SearchOperationResponse());
    }

    @DeleteMapping(DELETE_OPERATION)
    @Operation(summary = "Удаление операции", description = "Удаляет операцию поиска")
    public Result<Void> deleteOperation(@PathVariable Long id) {
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание операций", description = "Создает несколько операций поиска за один запрос")
    public Result<BatchResponse<SearchOperationResponse>> batchCreateOperations(@Valid @RequestBody BatchRequest<SearchOperationRequest> request) {
        return Result.success(new BatchResponse<>());
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление операций", description = "Обновляет несколько операций поиска за один запрос")
    public Result<BatchResponse<SearchOperationResponse>> batchUpdateOperations(@Valid @RequestBody BatchRequest<SearchOperationRequest> request) {
        return Result.success(new BatchResponse<>());
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Возвращает примеры DTO для пакетных операций")
    public Result<List<SearchOperationRequest>> getBatchExamples() {
        return Result.success(List.of(new SearchOperationRequest()));
    }
}

