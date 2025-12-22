package ru.mai.lighthouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lighthouse.entity.api.batch.BatchError;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.team.TeamMemberRequest;
import ru.mai.lighthouse.entity.api.team.VolunteerTeamRequest;
import ru.mai.lighthouse.entity.api.team.VolunteerTeamResponse;
import ru.mai.lighthouse.exception.NotFoundException;
import ru.mai.lighthouse.repository.volunteer_team.VolunteerTeamRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerTeamService {

    private final VolunteerTeamRepository volunteerTeamRepository;

    public VolunteerTeamResponse create(VolunteerTeamRequest request) {
        log.info("Создание команды для операции: {}", request.getOperationId());
        return volunteerTeamRepository.create(request);
    }

    public VolunteerTeamResponse getById(Long id) {
        log.info("Получение команды по ID: {}", id);
        return volunteerTeamRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Команда с ID %d не найдена", id)));
    }

    public List<VolunteerTeamResponse> getAll() {
        log.info("Получение всех команд");
        return volunteerTeamRepository.getAll();
    }

    public List<VolunteerTeamResponse> getByOperation(Long operationId) {
        log.info("Получение команд по операции: {}", operationId);
        return volunteerTeamRepository.getByOperation(operationId);
    }

    public VolunteerTeamResponse update(Long id, VolunteerTeamRequest request) {
        log.info("Обновление команды: {}", id);
        return volunteerTeamRepository.update(id, request);
    }

    public void delete(Long id) {
        log.info("Удаление команды: {}", id);
        volunteerTeamRepository.delete(id);
    }

    public void addMember(Long teamId, TeamMemberRequest request) {
        log.info("Добавление участника {} в команду {}", request.getVolunteerId(), teamId);
        volunteerTeamRepository.addMember(teamId, request);
    }

    public void removeMember(Long teamId, Long volunteerId) {
        log.info("Удаление участника {} из команды {}", volunteerId, teamId);
        volunteerTeamRepository.removeMember(teamId, volunteerId);
    }

    public List<Long> getTeamMembers(Long teamId) {
        log.info("Получение участников команды: {}", teamId);
        return volunteerTeamRepository.getTeamMembers(teamId);
    }

    public BatchResponse<VolunteerTeamResponse> batchCreate(BatchRequest<VolunteerTeamRequest> request) {
        log.info("Пакетное создание команд: {} элементов", request.getItems().size());

        List<VolunteerTeamResponse> successItems = request.getItems().stream()
                .map(volunteerTeamRepository::create)
                .toList();

        return new BatchResponse<VolunteerTeamResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(0)
                .setSuccessItems(successItems)
                .setErrors(List.of());
    }

    public BatchResponse<VolunteerTeamResponse> batchUpdate(BatchRequest<VolunteerTeamRequest> request) {
        log.info("Пакетное обновление команд: {} элементов", request.getItems().size());
        return new BatchResponse<VolunteerTeamResponse>()
                .setSuccessCount(0)
                .setErrorCount(request.getItems().size())
                .setSuccessItems(List.of())
                .setErrors(List.of(new BatchError(0, "Для обновления требуется ID команды")));
    }
}
