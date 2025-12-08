package ru.mai.lighthouse.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.team.TeamMemberRequest;
import ru.mai.lighthouse.entity.api.team.VolunteerTeamRequest;
import ru.mai.lighthouse.entity.api.team.VolunteerTeamResponse;
import ru.mai.lighthouse.service.VolunteerTeamService;

import java.util.List;
import java.util.stream.IntStream;

import static ru.mai.lighthouse.config.GlobalConstants.API_V1;

@RestController
@Tag(name = "Volunteer Teams", description = "API для работы с командами волонтеров")
@RequiredArgsConstructor
public class VolunteerTeamController {

    private final VolunteerTeamService volunteerTeamService;

    public static final String BASE_PREFIX = API_V1 + "/teams";
    public static final String CREATE_TEAM = BASE_PREFIX;
    public static final String GET_TEAM = BASE_PREFIX + "/{id}";
    public static final String UPDATE_TEAM = BASE_PREFIX + "/{id}";
    public static final String DELETE_TEAM = BASE_PREFIX + "/{id}";
    public static final String GET_ALL_TEAMS = BASE_PREFIX;
    public static final String GET_TEAMS_BY_OPERATION = BASE_PREFIX + "/operation/{operationId}";
    public static final String ADD_MEMBER = BASE_PREFIX + "/{teamId}/members";
    public static final String REMOVE_MEMBER = BASE_PREFIX + "/{teamId}/members/{volunteerId}";
    public static final String GET_TEAM_MEMBERS = BASE_PREFIX + "/{teamId}/members";
    public static final String BATCH_CREATE = BASE_PREFIX + "/batch/create";
    public static final String BATCH_UPDATE = BASE_PREFIX + "/batch/update";
    public static final String GET_BATCH_EXAMPLES = BASE_PREFIX + "/batch/examples";

    @PostMapping(CREATE_TEAM)
    @Operation(summary = "Создание команды", description = "Создает новую команду волонтеров для операции поиска")
    public Result<VolunteerTeamResponse> createTeam(@Valid @RequestBody VolunteerTeamRequest request) {
        return Result.success(volunteerTeamService.create(request));
    }

    @GetMapping(GET_TEAM)
    @Operation(summary = "Получение команды по ID", description = "Возвращает информацию о команде волонтеров")
    public Result<VolunteerTeamResponse> getTeam(@PathVariable Long id) {
        return Result.success(volunteerTeamService.getById(id));
    }

    @GetMapping(GET_ALL_TEAMS)
    @Operation(summary = "Получение всех команд", description = "Возвращает список всех команд волонтеров")
    public Result<List<VolunteerTeamResponse>> getAllTeams() {
        return Result.success(volunteerTeamService.getAll());
    }

    @GetMapping(GET_TEAMS_BY_OPERATION)
    @Operation(summary = "Получение команд по операции", description = "Возвращает список команд для указанной операции")
    public Result<List<VolunteerTeamResponse>> getTeamsByOperation(@PathVariable Long operationId) {
        return Result.success(volunteerTeamService.getByOperation(operationId));
    }

    @PutMapping(UPDATE_TEAM)
    @Operation(summary = "Обновление команды", description = "Обновляет информацию о команде волонтеров")
    public Result<VolunteerTeamResponse> updateTeam(@PathVariable Long id, @Valid @RequestBody VolunteerTeamRequest request) {
        return Result.success(volunteerTeamService.update(id, request));
    }

    @DeleteMapping(DELETE_TEAM)
    @Operation(summary = "Удаление команды", description = "Удаляет команду волонтеров")
    public Result<Void> deleteTeam(@PathVariable Long id) {
        volunteerTeamService.delete(id);
        return Result.success(null);
    }

    @PostMapping(ADD_MEMBER)
    @Operation(summary = "Добавление участника в команду", description = "Добавляет волонтера в команду")
    public Result<Void> addMember(@PathVariable Long teamId, @Valid @RequestBody TeamMemberRequest request) {
        volunteerTeamService.addMember(teamId, request);
        return Result.success(null);
    }

    @DeleteMapping(REMOVE_MEMBER)
    @Operation(summary = "Удаление участника из команды", description = "Удаляет волонтера из команды")
    public Result<Void> removeMember(@PathVariable Long teamId, @PathVariable Long volunteerId) {
        volunteerTeamService.removeMember(teamId, volunteerId);
        return Result.success(null);
    }

    @GetMapping(GET_TEAM_MEMBERS)
    @Operation(summary = "Получение участников команды", description = "Возвращает список участников команды")
    public Result<List<Long>> getTeamMembers(@PathVariable Long teamId) {
        return Result.success(volunteerTeamService.getTeamMembers(teamId));
    }

    @PostMapping(BATCH_CREATE)
    @Operation(summary = "Пакетное создание команд", description = "Создает несколько команд волонтеров за один запрос")
    public Result<BatchResponse<VolunteerTeamResponse>> batchCreateTeams(@Valid @RequestBody BatchRequest<VolunteerTeamRequest> request) {
        List<VolunteerTeamResponse> successItems = new java.util.ArrayList<>();
        List<ru.mai.lighthouse.entity.api.batch.BatchError> errors = new java.util.ArrayList<>();

        for (int i = 0; i < request.getItems().size(); i++) {
            try {
                VolunteerTeamResponse response = volunteerTeamService.create(request.getItems().get(i));
                successItems.add(response);
            } catch (Exception e) {
                errors.add(new ru.mai.lighthouse.entity.api.batch.BatchError(i, e.getMessage()));
            }
        }

        BatchResponse<VolunteerTeamResponse> batchResponse = new BatchResponse<VolunteerTeamResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(errors.size())
                .setSuccessItems(successItems)
                .setErrors(errors);

        return Result.success(batchResponse);
    }

    @PutMapping(BATCH_UPDATE)
    @Operation(summary = "Пакетное обновление команд", description = "Обновляет несколько команд волонтеров за один запрос")
    public Result<BatchResponse<VolunteerTeamResponse>> batchUpdateTeams(@Valid @RequestBody BatchRequest<VolunteerTeamRequest> request) {
        return Result.success(volunteerTeamService.batchUpdate(request));
    }

    @GetMapping(GET_BATCH_EXAMPLES)
    @Operation(summary = "Получение примеров для batch операций", description = "Генерирует примеры DTO для пакетных операций")
    public Result<List<VolunteerTeamRequest>> getBatchExamples() {
        List<VolunteerTeamRequest> examples = IntStream.range(1, 6)
                .mapToObj(i -> new VolunteerTeamRequest()
                        .setOperationId((long) i)
                        .setTeamLeaderId(1L)
                        .setTeamName(String.format("Команда %d", i)))
                .toList();
        return Result.success(examples);
    }
}

