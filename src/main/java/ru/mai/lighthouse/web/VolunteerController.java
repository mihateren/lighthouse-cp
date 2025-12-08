package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerRequest;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerResponse;
import ru.mai.lighthouse.service.VolunteerService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Volunteers", description = "API для работы с волонтерами")
@RequiredArgsConstructor
public class VolunteerController {

    private final VolunteerService volunteerService;

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
        return Result.success(volunteerService.create(request));
    }

    @GetMapping(GET_VOLUNTEER)
    @Operation(summary = "Получение волонтера по ID", description = "Возвращает информацию о волонтере")
    public Result<VolunteerResponse> getVolunteer(@PathVariable Long id) {
        return Result.success(volunteerService.getById(id));
    }

    @GetMapping(GET_ALL_VOLUNTEERS)
    @Operation(summary = "Получение всех волонтеров", description = "Возвращает список всех волонтеров")
    public Result<List<VolunteerResponse>> getAllVolunteers() {
        return Result.success(volunteerService.getAll());
    }

    @GetMapping(GET_VOLUNTEERS_BY_CENTER)
    @Operation(summary = "Получение волонтеров по центру", description = "Возвращает список волонтеров указанного центра")
    public Result<List<VolunteerResponse>> getVolunteersByCenter(@PathVariable Long centerId) {
        return Result.success(volunteerService.getByCenter(centerId));
    }

    @GetMapping(GET_ACTIVE_VOLUNTEERS)
    @Operation(summary = "Получение активных волонтеров", description = "Возвращает список активных волонтеров")
    public Result<List<VolunteerResponse>> getActiveVolunteers() {
        return Result.success(volunteerService.getActive());
    }

    @PutMapping(UPDATE_VOLUNTEER)
    @Operation(summary = "Обновление волонтера", description = "Обновляет информацию о волонтере")
    public Result<VolunteerResponse> updateVolunteer(@PathVariable Long id, @Valid @RequestBody VolunteerRequest request) {
        return Result.success(volunteerService.update(id, request));
    }

    @DeleteMapping(DELETE_VOLUNTEER)
    @Operation(summary = "Удаление волонтера", description = "Удаляет волонтера")
    public Result<Void> deleteVolunteer(@PathVariable Long id) {
        volunteerService.delete(id);
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание волонтеров", description = "Создает несколько волонтеров за один запрос")
    public Result<BatchResponse<VolunteerResponse>> batchCreateVolunteers(@Valid @RequestBody BatchRequest<VolunteerRequest> request) {
        List<VolunteerResponse> successItems = new java.util.ArrayList<>();
        List<ru.mai.lighthouse.entity.api.batch.BatchError> errors = new java.util.ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            try {
                VolunteerResponse response = volunteerService.create(request.getItems().get(i));
                successItems.add(response);
            } catch (Exception e) {
                errors.add(new ru.mai.lighthouse.entity.api.batch.BatchError(i, e.getMessage()));
            }
        }

        BatchResponse<VolunteerResponse> batchResponse = new BatchResponse<VolunteerResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(errors.size())
                .setSuccessItems(successItems)
                .setErrors(errors);

        return Result.success(batchResponse);
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление волонтеров", description = "Обновляет несколько волонтеров за один запрос")
    public Result<BatchResponse<VolunteerResponse>> batchUpdateVolunteers(@Valid @RequestBody BatchRequest<VolunteerRequest> request) {
        return Result.success(volunteerService.batchUpdate(request));
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Генерирует примеры DTO для пакетных операций")
    public Result<List<VolunteerRequest>> getBatchExamples() {
        List<VolunteerRequest> examples = IntStream.range(1, 6)
                .mapToObj(i -> new VolunteerRequest()
                        .setCenterId(1L)
                        .setFirstName(String.format("Волонтер%d", i))
                        .setLastName(String.format("Фамилия%d", i))
                        .setBirthdate(LocalDate.of(1995 + i, 5, 15))
                        .setPhone(String.format("+7999123456%d", i))
                        .setEmail(String.format("volunteer%d@example.com", i))
                        .setSkills(String.format("Навыки %d", i))
                        .setIsActive(true))
                .toList();
        return Result.success(examples);
    }
}

