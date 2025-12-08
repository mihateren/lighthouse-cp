package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.victim.VictimCreateRequest;
import ru.mai.lighthouse.entity.api.victim.VictimRequest;
import ru.mai.lighthouse.entity.api.victim.VictimResponse;
import ru.mai.lighthouse.service.VictimService;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Victims", description = "API для работы с данными о пропавших людях")
@RequiredArgsConstructor
public class VictimController {

    private final VictimService victimService;

    public static final String BASE_PREFIX = API_V1 + "/victims";
    public static final String CREATE_VICTIM = BASE_PREFIX;
    public static final String GET_VICTIM = BASE_PREFIX + "/{id}";
    public static final String UPDATE_VICTIM = BASE_PREFIX + "/{id}";
    public static final String DELETE_VICTIM = BASE_PREFIX + "/{id}";
    public static final String GET_ALL_VICTIMS = BASE_PREFIX;
    public static final String GET_VICTIMS_BY_CITY = BASE_PREFIX + "/city/{city}";
    public static final String BATCH_CREATE = BASE_PREFIX + "/batch/create";
    public static final String BATCH_UPDATE = BASE_PREFIX + "/batch/update";
    public static final String GET_BATCH_EXAMPLES = BASE_PREFIX + "/batch/examples";

    @PostMapping(value = CREATE_VICTIM, consumes = {"multipart/form-data"})
    @Operation(summary = "Создание записи о пропавшем с фотографиями", 
               description = "Создает новую запись о пропавшем человеке. Фотографии будут загружены в S3.")
    public Result<VictimResponse> createVictim(
            @RequestPart("data") @Valid VictimCreateRequest request,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) throws IOException {
        return Result.success(victimService.create(request, photos));
    }

    @GetMapping(GET_VICTIM)
    @Operation(summary = "Получение записи о пропавшем по ID", description = "Возвращает информацию о пропавшем человеке")
    public Result<VictimResponse> getVictim(@PathVariable Long id) {
        return Result.success(victimService.getById(id));
    }

    @GetMapping(GET_ALL_VICTIMS)
    @Operation(summary = "Получение всех записей о пропавших", description = "Возвращает список всех пропавших людей")
    public Result<List<VictimResponse>> getAllVictims() {
        return Result.success(victimService.getAll());
    }

    @GetMapping(GET_VICTIMS_BY_CITY)
    @Operation(summary = "Получение пропавших по городу", description = "Возвращает список пропавших людей в указанном городе")
    public Result<List<VictimResponse>> getVictimsByCity(@PathVariable String city) {
        return Result.success(victimService.getByCity(city));
    }

    @PutMapping(UPDATE_VICTIM)
    @Operation(summary = "Обновление записи о пропавшем", description = "Обновляет информацию о пропавшем человеке")
    public Result<VictimResponse> updateVictim(@PathVariable Long id, @Valid @RequestBody VictimRequest request) {
        return Result.success(victimService.update(id, request));
    }

    @DeleteMapping(DELETE_VICTIM)
    @Operation(summary = "Удаление записи о пропавшем", description = "Удаляет запись о пропавшем человеке")
    public Result<Void> deleteVictim(@PathVariable Long id) {
        victimService.delete(id);
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание записей о пропавших", description = "Создает несколько записей о пропавших за один запрос")
    public Result<BatchResponse<VictimResponse>> batchCreateVictims(@Valid @RequestBody BatchRequest<VictimRequest> request) {
        List<VictimResponse> successItems = new java.util.ArrayList<>();
        List<ru.mai.lighthouse.entity.api.batch.BatchError> errors = new java.util.ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            try {
                VictimRequest item = request.getItems().get(i);
                VictimResponse response = victimService.create(
                        new ru.mai.lighthouse.entity.api.victim.VictimCreateRequest()
                                .setFirstName(item.getFirstName())
                                .setLastName(item.getLastName())
                                .setCity(item.getCity())
                                .setAge(item.getAge())
                                .setHeight(item.getHeight())
                                .setNotes(item.getNotes()),
                        null
                );
                successItems.add(response);
            } catch (Exception e) {
                errors.add(new ru.mai.lighthouse.entity.api.batch.BatchError(i, e.getMessage()));
            }
        }

        BatchResponse<VictimResponse> batchResponse = new BatchResponse<VictimResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(errors.size())
                .setSuccessItems(successItems)
                .setErrors(errors);

        return Result.success(batchResponse);
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление записей о пропавших", description = "Обновляет несколько записей о пропавших за один запрос")
    public Result<BatchResponse<VictimResponse>> batchUpdateVictims(@Valid @RequestBody BatchRequest<VictimRequest> request) {
        return Result.success(victimService.batchUpdate(request));
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Генерирует примеры DTO для пакетных операций")
    public Result<List<VictimRequest>> getBatchExamples() {
        List<VictimRequest> examples = IntStream.range(1, 6)
                .mapToObj(i -> new VictimRequest()
                        .setFirstName(String.format("Имя%d", i))
                        .setLastName(String.format("Фамилия%d", i))
                        .setCity(String.format("Город%d", i))
                        .setAge(20 + i)
                        .setHeight(170 + i)
                        .setNotes(String.format("Заметки %d", i)))
                .toList();
        return Result.success(examples);
    }
}

