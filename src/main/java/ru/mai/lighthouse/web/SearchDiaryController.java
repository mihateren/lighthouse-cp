package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryRequest;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryResponse;
import ru.mai.lighthouse.service.SearchDiaryService;

import java.util.List;
import java.util.stream.IntStream;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Search Diaries", description = "API для работы с дневниками поиска")
@RequiredArgsConstructor
public class SearchDiaryController {

    private final SearchDiaryService searchDiaryService;

    public static final String BASE_PREFIX = API_V1 + "/diaries";
    public static final String CREATE_DIARY_ENTRY = BASE_PREFIX;
    public static final String GET_DIARY_ENTRY = BASE_PREFIX + "/{id}";
    public static final String UPDATE_DIARY_ENTRY = BASE_PREFIX + "/{id}";
    public static final String DELETE_DIARY_ENTRY = BASE_PREFIX + "/{id}";
    public static final String GET_ALL_DIARY_ENTRIES = BASE_PREFIX;
    public static final String GET_DIARY_ENTRIES_BY_TEAM = BASE_PREFIX + "/team/{teamId}";
    public static final String GET_DIARY_ENTRIES_BY_VOLUNTEER = BASE_PREFIX + "/volunteer/{volunteerId}";
    public static final String BATCH_CREATE = BASE_PREFIX + "/batch/create";
    public static final String BATCH_UPDATE = BASE_PREFIX + "/batch/update";
    public static final String GET_BATCH_EXAMPLES = BASE_PREFIX + "/batch/examples";

    @PostMapping(CREATE_DIARY_ENTRY)
    @Operation(summary = "Создание записи в дневнике", description = "Создает новую запись в дневнике поиска")
    public Result<SearchDiaryResponse> createDiaryEntry(@Valid @RequestBody SearchDiaryRequest request) {
        return Result.success(searchDiaryService.create(request));
    }

    @GetMapping(GET_DIARY_ENTRY)
    @Operation(summary = "Получение записи по ID", description = "Возвращает информацию о записи в дневнике")
    public Result<SearchDiaryResponse> getDiaryEntry(@PathVariable Long id) {
        return Result.success(searchDiaryService.getById(id));
    }

    @GetMapping(GET_ALL_DIARY_ENTRIES)
    @Operation(summary = "Получение всех записей", description = "Возвращает список всех записей в дневниках")
    public Result<List<SearchDiaryResponse>> getAllDiaryEntries() {
        return Result.success(searchDiaryService.getAll());
    }

    @GetMapping(GET_DIARY_ENTRIES_BY_TEAM)
    @Operation(summary = "Получение записей по команде", description = "Возвращает список записей в дневнике для указанной команды")
    public Result<List<SearchDiaryResponse>> getDiaryEntriesByTeam(@PathVariable Long teamId) {
        return Result.success(searchDiaryService.getByTeam(teamId));
    }

    @GetMapping(GET_DIARY_ENTRIES_BY_VOLUNTEER)
    @Operation(summary = "Получение записей по волонтеру", description = "Возвращает список записей в дневнике для указанного волонтера")
    public Result<List<SearchDiaryResponse>> getDiaryEntriesByVolunteer(@PathVariable Long volunteerId) {
        return Result.success(searchDiaryService.getByVolunteer(volunteerId));
    }

    @PutMapping(UPDATE_DIARY_ENTRY)
    @Operation(summary = "Обновление записи", description = "Обновляет запись в дневнике поиска")
    public Result<SearchDiaryResponse> updateDiaryEntry(@PathVariable Long id, @Valid @RequestBody SearchDiaryRequest request) {
        return Result.success(searchDiaryService.update(id, request));
    }

    @DeleteMapping(DELETE_DIARY_ENTRY)
    @Operation(summary = "Удаление записи", description = "Удаляет запись из дневника поиска")
    public Result<Void> deleteDiaryEntry(@PathVariable Long id) {
        searchDiaryService.delete(id);
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание записей", description = "Создает несколько записей в дневнике за один запрос")
    public Result<BatchResponse<SearchDiaryResponse>> batchCreateDiaryEntries(@Valid @RequestBody BatchRequest<SearchDiaryRequest> request) {
        List<SearchDiaryResponse> successItems = new java.util.ArrayList<>();
        List<ru.mai.lighthouse.entity.api.batch.BatchError> errors = new java.util.ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            try {
                SearchDiaryResponse response = searchDiaryService.create(request.getItems().get(i));
                successItems.add(response);
            } catch (Exception e) {
                errors.add(new ru.mai.lighthouse.entity.api.batch.BatchError(i, e.getMessage()));
            }
        }

        BatchResponse<SearchDiaryResponse> batchResponse = new BatchResponse<SearchDiaryResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(errors.size())
                .setSuccessItems(successItems)
                .setErrors(errors);

        return Result.success(batchResponse);
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление записей", description = "Обновляет несколько записей в дневнике за один запрос")
    public Result<BatchResponse<SearchDiaryResponse>> batchUpdateDiaryEntries(@Valid @RequestBody BatchRequest<SearchDiaryRequest> request) {
        return Result.success(searchDiaryService.batchUpdate(request));
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Генерирует примеры DTO для пакетных операций")
    public Result<List<SearchDiaryRequest>> getBatchExamples() {
        String[] activityTypes = {"расклейка_листовок", "опрос_знакомых", "обход_территории", "работа_с_соцсетями", "другое"};
        List<SearchDiaryRequest> examples = IntStream.range(0, 5)
                .mapToObj(i -> new SearchDiaryRequest()
                        .setTeamId(1L)
                        .setVolunteerId((long) (i + 1))
                        .setActivityType(activityTypes[i])
                        .setDescription(String.format("Описание активности %d", i + 1))
                        .setLocation(String.format("Местоположение %d", i + 1)))
                .toList();
        return Result.success(examples);
    }
}

