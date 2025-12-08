package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryRequest;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryResponse;

import java.util.List;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Search Diaries", description = "API для работы с дневниками поиска")
public class SearchDiaryController {

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
        return Result.success(new SearchDiaryResponse());
    }

    @GetMapping(GET_DIARY_ENTRY)
    @Operation(summary = "Получение записи по ID", description = "Возвращает информацию о записи в дневнике")
    public Result<SearchDiaryResponse> getDiaryEntry(@PathVariable Long id) {
        return Result.success(new SearchDiaryResponse());
    }

    @GetMapping(GET_ALL_DIARY_ENTRIES)
    @Operation(summary = "Получение всех записей", description = "Возвращает список всех записей в дневниках")
    public Result<List<SearchDiaryResponse>> getAllDiaryEntries() {
        return Result.success(List.of());
    }

    @GetMapping(GET_DIARY_ENTRIES_BY_TEAM)
    @Operation(summary = "Получение записей по команде", description = "Возвращает список записей в дневнике для указанной команды")
    public Result<List<SearchDiaryResponse>> getDiaryEntriesByTeam(@PathVariable Long teamId) {
        return Result.success(List.of());
    }

    @GetMapping(GET_DIARY_ENTRIES_BY_VOLUNTEER)
    @Operation(summary = "Получение записей по волонтеру", description = "Возвращает список записей в дневнике для указанного волонтера")
    public Result<List<SearchDiaryResponse>> getDiaryEntriesByVolunteer(@PathVariable Long volunteerId) {
        return Result.success(List.of());
    }

    @PutMapping(UPDATE_DIARY_ENTRY)
    @Operation(summary = "Обновление записи", description = "Обновляет запись в дневнике поиска")
    public Result<SearchDiaryResponse> updateDiaryEntry(@PathVariable Long id, @Valid @RequestBody SearchDiaryRequest request) {
        return Result.success(new SearchDiaryResponse());
    }

    @DeleteMapping(DELETE_DIARY_ENTRY)
    @Operation(summary = "Удаление записи", description = "Удаляет запись из дневника поиска")
    public Result<Void> deleteDiaryEntry(@PathVariable Long id) {
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание записей", description = "Создает несколько записей в дневнике за один запрос")
    public Result<BatchResponse<SearchDiaryResponse>> batchCreateDiaryEntries(@Valid @RequestBody BatchRequest<SearchDiaryRequest> request) {
        return Result.success(new BatchResponse<>());
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление записей", description = "Обновляет несколько записей в дневнике за один запрос")
    public Result<BatchResponse<SearchDiaryResponse>> batchUpdateDiaryEntries(@Valid @RequestBody BatchRequest<SearchDiaryRequest> request) {
        return Result.success(new BatchResponse<>());
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Возвращает примеры DTO для пакетных операций")
    public Result<List<SearchDiaryRequest>> getBatchExamples() {
        return Result.success(List.of(new SearchDiaryRequest()));
    }
}

