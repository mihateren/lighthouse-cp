package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.admin.AdminRequest;
import ru.mai.lighthouse.entity.api.admin.AdminResponse;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.service.AdminService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Admins", description = "API для работы с администраторами волонтерских центров")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    public static final String BASE_PREFIX = API_V1 + "/admins";
    public static final String CREATE_ADMIN = BASE_PREFIX;
    public static final String GET_ADMIN = BASE_PREFIX + "/{id}";
    public static final String UPDATE_ADMIN = BASE_PREFIX + "/{id}";
    public static final String DELETE_ADMIN = BASE_PREFIX + "/{id}";
    public static final String GET_ALL_ADMINS = BASE_PREFIX;
    public static final String GET_ADMINS_BY_CENTER = BASE_PREFIX + "/center/{centerId}";
    public static final String BATCH_CREATE = BASE_PREFIX + "/batch/create";
    public static final String BATCH_UPDATE = BASE_PREFIX + "/batch/update";
    public static final String GET_BATCH_EXAMPLES = BASE_PREFIX + "/batch/examples";

    @PostMapping(CREATE_ADMIN)
    @Operation(summary = "Создание админа", description = "Создает нового администратора волонтерского центра")
    public Result<AdminResponse> createAdmin(@Valid @RequestBody AdminRequest request) {
        return Result.success(adminService.create(request));
    }

    @GetMapping(GET_ADMIN)
    @Operation(summary = "Получение админа по ID", description = "Возвращает информацию об администраторе")
    public Result<AdminResponse> getAdmin(@PathVariable Long id) {
        return Result.success(adminService.getById(id));
    }

    @GetMapping(GET_ALL_ADMINS)
    @Operation(summary = "Получение всех админов", description = "Возвращает список всех администраторов")
    public Result<List<AdminResponse>> getAllAdmins() {
        return Result.success(adminService.getAll());
    }

    @GetMapping(GET_ADMINS_BY_CENTER)
    @Operation(summary = "Получение админов по центру", description = "Возвращает список администраторов указанного центра")
    public Result<List<AdminResponse>> getAdminsByCenter(@PathVariable Long centerId) {
        return Result.success(adminService.getByCenter(centerId));
    }

    @PutMapping(UPDATE_ADMIN)
    @Operation(summary = "Обновление админа", description = "Обновляет информацию об администраторе")
    public Result<AdminResponse> updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminRequest request) {
        return Result.success(adminService.update(id, request));
    }

    @DeleteMapping(DELETE_ADMIN)
    @Operation(summary = "Удаление админа", description = "Удаляет администратора")
    public Result<Void> deleteAdmin(@PathVariable Long id) {
        adminService.delete(id);
        return Result.success(null);
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание админов", description = "Создает несколько администраторов за один запрос")
    public Result<BatchResponse<AdminResponse>> batchCreateAdmins(@Valid @RequestBody BatchRequest<AdminRequest> request) {
        List<AdminResponse> successItems = new java.util.ArrayList<>();
        List<ru.mai.lighthouse.entity.api.batch.BatchError> errors = new java.util.ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            try {
                AdminResponse response = adminService.create(request.getItems().get(i));
                successItems.add(response);
            } catch (Exception e) {
                errors.add(new ru.mai.lighthouse.entity.api.batch.BatchError(i, e.getMessage()));
            }
        }

        BatchResponse<AdminResponse> batchResponse = new BatchResponse<AdminResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(errors.size())
                .setSuccessItems(successItems)
                .setErrors(errors);

        return Result.success(batchResponse);
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление админов", description = "Обновляет несколько администраторов за один запрос")
    public Result<BatchResponse<AdminResponse>> batchUpdateAdmins(@Valid @RequestBody BatchRequest<AdminRequest> request) {
        return Result.success(adminService.batchUpdate(request));
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Генерирует примеры DTO для пакетных операций")
    public Result<List<AdminRequest>> getBatchExamples() {
        List<AdminRequest> examples = IntStream.range(1, 6)
                .mapToObj(i -> new AdminRequest()
                        .setCenterId(1L)
                        .setFirstName(String.format("Имя%d", i))
                        .setLastName(String.format("Фамилия%d", i))
                        .setEmail(String.format("admin%d@example.com", i))
                        .setBirthdate(LocalDate.of(1990 + i, 1, 1)))
                .toList();
        return Result.success(examples);
    }
}

