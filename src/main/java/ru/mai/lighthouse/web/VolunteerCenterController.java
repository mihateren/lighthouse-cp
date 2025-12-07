package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.center.CreateCenterRequest;
import ru.mai.lighthouse.entity.api.center.CreateCenterResponse;
import ru.mai.lighthouse.entity.api.center.CenterResponse;
import ru.mai.lighthouse.entity.api.center.UpdateCenterRequest;

import java.util.List;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Volunteer Centers", description = "API для работы с волонтерскими центрами")
public class VolunteerCenterController {

    public static final String BASE_PREFIX = API_V1 + "/centers";
    public static final String CREATE_CENTER = BASE_PREFIX;
    public static final String GET_CENTER = BASE_PREFIX + "/{id}";
    public static final String UPDATE_CENTER = BASE_PREFIX + "/{id}";
    public static final String DELETE_CENTER = BASE_PREFIX + "/{id}";
    public static final String GET_ALL_CENTERS = BASE_PREFIX;
    public static final String BATCH_CREATE = BASE_PREFIX + "/batch/create";
    public static final String BATCH_UPDATE = BASE_PREFIX + "/batch/update";
    public static final String GET_BATCH_EXAMPLES = BASE_PREFIX + "/batch/examples";

    @PostMapping(CREATE_CENTER)
    @Operation(summary = "Создание волонтерского центра", description = "Создает новый волонтерский центр")
    public Result<CreateCenterResponse> createCenter(@Valid @RequestBody CreateCenterRequest request) {
        return Result.success(new CreateCenterResponse());
    }

    @GetMapping(GET_CENTER)
    @Operation(summary = "Получение центра по ID", description = "Возвращает информацию о волонтерском центре")
    public Result<CenterResponse> getCenter(@PathVariable Long id) {
        return Result.success(new CenterResponse());
    }

    @GetMapping(GET_ALL_CENTERS)
    @Operation(summary = "Получение всех центров", description = "Возвращает список всех волонтерских центров")
    public Result<List<CenterResponse>> getAllCenters() {
        return Result.success(List.of());
    }

    @PutMapping(UPDATE_CENTER)
    @Operation(summary = "Обновление центра", description = "Обновляет информацию о волонтерском центре")
    public Result<CenterResponse> updateCenter(@PathVariable Long id, @Valid @RequestBody UpdateCenterRequest request) {
        return Result.success(new CenterResponse());
    }

    @DeleteMapping(DELETE_CENTER)
    @Operation(summary = "Удаление центра", description = "Удаляет волонтерский центр")
    public Result<Void> deleteCenter(@PathVariable Long id) {
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание центров", description = "Создает несколько волонтерских центров за один запрос")
    public Result<BatchResponse<CreateCenterResponse>> batchCreateCenters(@Valid @RequestBody BatchRequest<CreateCenterRequest> request) {
        return Result.success(new BatchResponse<>());
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление центров", description = "Обновляет несколько волонтерских центров за один запрос")
    public Result<BatchResponse<CenterResponse>> batchUpdateCenters(@Valid @RequestBody BatchRequest<UpdateCenterRequest> request) {
        return Result.success(new BatchResponse<>());
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Возвращает примеры DTO для пакетных операций")
    public Result<List<CreateCenterRequest>> getBatchExamples() {
        return Result.success(List.of(new CreateCenterRequest()));
    }
}
