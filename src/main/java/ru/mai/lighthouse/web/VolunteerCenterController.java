package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.center.CreateCenterRequest;
import ru.mai.lighthouse.entity.api.center.CreateCenterResponse;
import ru.mai.lighthouse.entity.api.center.CenterResponse;
import ru.mai.lighthouse.entity.api.center.UpdateCenterRequest;
import ru.mai.lighthouse.service.VolunteerCenterService;

import java.util.List;
import java.util.stream.IntStream;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Volunteer Centers", description = "API для работы с волонтерскими центрами")
@RequiredArgsConstructor
public class VolunteerCenterController {

    private final VolunteerCenterService volunteerCenterService;

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
    public Result<CenterResponse> createCenter(@Valid @RequestBody CreateCenterRequest request) {
        return Result.success(volunteerCenterService.create(request));
    }

    @GetMapping(GET_CENTER)
    @Operation(summary = "Получение центра по ID", description = "Возвращает информацию о волонтерском центре")
    public Result<CenterResponse> getCenter(@PathVariable Long id) {
        return Result.success(volunteerCenterService.getById(id));
    }

    @GetMapping(GET_ALL_CENTERS)
    @Operation(summary = "Получение всех центров", description = "Возвращает список всех волонтерских центров")
    public Result<List<CenterResponse>> getAllCenters() {
        return Result.success(volunteerCenterService.getAll());
    }

    @PutMapping(UPDATE_CENTER)
    @Operation(summary = "Обновление центра", description = "Обновляет информацию о волонтерском центре")
    public Result<CenterResponse> updateCenter(@PathVariable Long id, @Valid @RequestBody UpdateCenterRequest request) {
        return Result.success(volunteerCenterService.update(id, request));
    }

    @DeleteMapping(DELETE_CENTER)
    @Operation(summary = "Удаление центра", description = "Удаляет волонтерский центр")
    public Result<Void> deleteCenter(@PathVariable Long id) {
        volunteerCenterService.delete(id);
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание центров", description = "Создает несколько волонтерских центров за один запрос")
    public Result<BatchResponse<CenterResponse>> batchCreateCenters(@Valid @RequestBody BatchRequest<CreateCenterRequest> request) {
        List<CenterResponse> successItems = new java.util.ArrayList<>();
        List<ru.mai.lighthouse.entity.api.batch.BatchError> errors = new java.util.ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            try {
                CenterResponse response = volunteerCenterService.create(request.getItems().get(i));
                successItems.add(response);
            } catch (Exception e) {
                errors.add(new ru.mai.lighthouse.entity.api.batch.BatchError(i, e.getMessage()));
            }
        }

        BatchResponse<CenterResponse> batchResponse = new BatchResponse<CenterResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(errors.size())
                .setSuccessItems(successItems)
                .setErrors(errors);

        return Result.success(batchResponse);
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление центров", description = "Обновляет несколько волонтерских центров за один запрос")
    public Result<BatchResponse<CenterResponse>> batchUpdateCenters(@Valid @RequestBody BatchRequest<UpdateCenterRequest> request) {
        return Result.success(volunteerCenterService.batchUpdate(request));
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Генерирует примеры DTO для пакетных операций")
    public Result<List<CreateCenterRequest>> getBatchExamples() {
        List<CreateCenterRequest> examples = IntStream.range(1, 6)
                .mapToObj(i -> new CreateCenterRequest()
                        .setTitle(String.format("Волонтерский центр %d", i))
                        .setCity(String.format("Город %d", i))
                        .setPostCode(String.format("10000%d", i))
                        .setAddress(String.format("ул. Примерная, д. %d", i)))
                .toList();
        return Result.success(examples);
    }
}
