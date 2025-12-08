package ru.mai.lighthouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lighthouse.entity.api.batch.BatchError;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerRequest;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerResponse;
import ru.mai.lighthouse.repository.volunteer.VolunteerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerResponse create(VolunteerRequest request) {
        log.info("Создание волонтера: {} {}", request.getFirstName(), request.getLastName());
        return volunteerRepository.create(request);
    }

    public VolunteerResponse getById(Long id) {
        log.info("Получение волонтера по ID: {}", id);
        return volunteerRepository.getById(id)
                .orElseThrow(() -> new ru.mai.lighthouse.exception.NotFoundException(
                        String.format("Волонтер с ID %d не найден", id)));
    }

    public List<VolunteerResponse> getAll() {
        log.info("Получение всех волонтеров");
        return volunteerRepository.getAll();
    }

    public List<VolunteerResponse> getByCenter(Long centerId) {
        log.info("Получение волонтеров по центру: {}", centerId);
        return volunteerRepository.getByCenter(centerId);
    }

    public List<VolunteerResponse> getActive() {
        log.info("Получение активных волонтеров");
        return volunteerRepository.getActive();
    }

    public VolunteerResponse update(Long id, VolunteerRequest request) {
        log.info("Обновление волонтера: {}", id);
        return volunteerRepository.update(id, request);
    }

    public void delete(Long id) {
        log.info("Удаление волонтера: {}", id);
        volunteerRepository.delete(id);
    }

    public BatchResponse<VolunteerResponse> batchCreate(BatchRequest<VolunteerRequest> request) {
        log.info("Пакетное создание волонтеров: {} элементов", request.getItems().size());

        List<VolunteerResponse> successItems = request.getItems().stream()
                .map(volunteerRepository::create)
                .toList();

        return new BatchResponse<VolunteerResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(0)
                .setSuccessItems(successItems)
                .setErrors(List.of());
    }

    public BatchResponse<VolunteerResponse> batchUpdate(BatchRequest<VolunteerRequest> request) {
        log.info("Пакетное обновление волонтеров: {} элементов", request.getItems().size());

        List<BatchError> errors = IntStream.range(0, request.getItems().size())
                .mapToObj(index -> new BatchError(index, "Для обновления требуется ID волонтера"))
                .collect(Collectors.toList());

        return new BatchResponse<VolunteerResponse>()
                .setSuccessCount(0)
                .setErrorCount(request.getItems().size())
                .setSuccessItems(List.of())
                .setErrors(errors);
    }
}
