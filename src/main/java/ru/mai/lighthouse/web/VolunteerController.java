package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerRequest;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerResponse;

import java.util.List;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Volunteers", description = "API для работы с волонтерами")
public class VolunteerController {

    public static final String BASE_PREFIX = API_V1 + "/volunteers";
    public static final String CREATE_VOLUNTEER = BASE_PREFIX;
    public static final String GET_VOLUNTEER = BASE_PREFIX + "/{id}";
    public static final String UPDATE_VOLUNTEER = BASE_PREFIX + "/{id}";
    public static final String DELETE_VOLUNTEER = BASE_PREFIX + "/{id}";
    public static final String GET_ALL_VOLUNTEERS = BASE_PREFIX;
    public static final String GET_VOLUNTEERS_BY_CENTER = BASE_PREFIX + "/center/{centerId}";
    public static final String GET_ACTIVE_VOLUNTEERS = BASE_PREFIX + "/active";
    public static final String BATCH_CREATE = BASE_PREFIX + "/batch/create";
    public static final String BATCH_UPDATE = BASE_PREFIX + "/batch/update";
    public static final String GET_BATCH_EXAMPLES = BASE_PREFIX + "/batch/examples";

    @PostMapping(CREATE_VOLUNTEER)
    @Operation(summary = "Создание волонтера", description = "Создает нового волонтера")
    public Result<VolunteerResponse> createVolunteer(@Valid @RequestBody VolunteerRequest request) {
        return Result.success(new VolunteerResponse());
    }

    @GetMapping(GET_VOLUNTEER)
    @Operation(summary = "Получение волонтера по ID", description = "Возвращает информацию о волонтере")
    public Result<VolunteerResponse> getVolunteer(@PathVariable Long id) {
        return Result.success(new VolunteerResponse());
    }

    @GetMapping(GET_ALL_VOLUNTEERS)
    @Operation(summary = "Получение всех волонтеров", description = "Возвращает список всех волонтеров")
    public Result<List<VolunteerResponse>> getAllVolunteers() {
        return Result.success(List.of());
    }

    @GetMapping(GET_VOLUNTEERS_BY_CENTER)
    @Operation(summary = "Получение волонтеров по центру", description = "Возвращает список волонтеров указанного центра")
    public Result<List<VolunteerResponse>> getVolunteersByCenter(@PathVariable Long centerId) {
        return Result.success(List.of());
    }

    @GetMapping(GET_ACTIVE_VOLUNTEERS)
    @Operation(summary = "Получение активных волонтеров", description = "Возвращает список активных волонтеров")
    public Result<List<VolunteerResponse>> getActiveVolunteers() {
        return Result.success(List.of());
    }

    @PutMapping(UPDATE_VOLUNTEER)
    @Operation(summary = "Обновление волонтера", description = "Обновляет информацию о волонтере")
    public Result<VolunteerResponse> updateVolunteer(@PathVariable Long id, @Valid @RequestBody VolunteerRequest request) {
        return Result.success(new VolunteerResponse());
    }

    @DeleteMapping(DELETE_VOLUNTEER)
    @Operation(summary = "Удаление волонтера", description = "Удаляет волонтера")
    public Result<Void> deleteVolunteer(@PathVariable Long id) {
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание волонтеров", description = "Создает несколько волонтеров за один запрос")
    public Result<BatchResponse<VolunteerResponse>> batchCreateVolunteers(@Valid @RequestBody BatchRequest<VolunteerRequest> request) {
        return Result.success(new BatchResponse<>());
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление волонтеров", description = "Обновляет несколько волонтеров за один запрос")
    public Result<BatchResponse<VolunteerResponse>> batchUpdateVolunteers(@Valid @RequestBody BatchRequest<VolunteerRequest> request) {
        return Result.success(new BatchResponse<>());
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Возвращает примеры DTO для пакетных операций")
    public Result<List<VolunteerRequest>> getBatchExamples() {
        return Result.success(List.of(new VolunteerRequest()));
    }
}

