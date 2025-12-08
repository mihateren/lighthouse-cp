package ru.mai.lighthouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lighthouse.entity.api.batch.BatchError;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.center.CenterResponse;
import ru.mai.lighthouse.entity.api.center.CreateCenterRequest;
import ru.mai.lighthouse.entity.api.center.UpdateCenterRequest;
import ru.mai.lighthouse.repository.volunteer_center.VolunteerCenterRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerCenterService {

    private final VolunteerCenterRepository volunteerCenterRepository;

    public CenterResponse create(CreateCenterRequest request) {
        log.info("Создание волонтерского центра: {}", request.getTitle());
        return volunteerCenterRepository.create(request);
    }

    public CenterResponse getById(Long id) {
        log.info("Получение центра по ID: {}", id);
        return volunteerCenterRepository.getById(id)
                .orElseThrow(() -> new ru.mai.lighthouse.exception.NotFoundException(
                        String.format("Центр с ID %d не найден", id)));
    }

    public List<CenterResponse> getAll() {
        log.info("Получение всех центров");
        return volunteerCenterRepository.getAll();
    }

    public CenterResponse update(Long id, UpdateCenterRequest request) {
        log.info("Обновление центра: {}", id);
        return volunteerCenterRepository.update(id, request);
    }

    public void delete(Long id) {
        log.info("Удаление центра: {}", id);
        volunteerCenterRepository.delete(id);
    }

    public BatchResponse<CenterResponse> batchCreate(BatchRequest<CreateCenterRequest> request) {
        log.info("Пакетное создание центров: {} элементов", request.getItems().size());

        List<CenterResponse> successItems = request.getItems().stream()
                .map(volunteerCenterRepository::create)
                .toList();

        return new BatchResponse<CenterResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(0)
                .setSuccessItems(successItems)
                .setErrors(List.of());
    }

    public BatchResponse<CenterResponse> batchUpdate(BatchRequest<UpdateCenterRequest> request) {
        log.info("Пакетное обновление центров: {} элементов", request.getItems().size());

        List<BatchError> errors = IntStream.range(0, request.getItems().size())
                .mapToObj(index -> new BatchError(index, "Для обновления требуется ID центра"))
                .collect(Collectors.toList());

        return new BatchResponse<CenterResponse>()
                .setSuccessCount(0)
                .setErrorCount(request.getItems().size())
                .setSuccessItems(List.of())
                .setErrors(errors);
    }
}
