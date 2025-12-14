package ru.mai.lighthouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lighthouse.entity.api.batch.BatchError;
import ru.mai.lighthouse.entity.api.batch.BatchRequest;
import ru.mai.lighthouse.entity.api.batch.BatchResponse;
import ru.mai.lighthouse.entity.api.victim.VictimCreateRequest;
import ru.mai.lighthouse.entity.api.victim.VictimRequest;
import ru.mai.lighthouse.entity.api.victim.VictimResponse;
import ru.mai.lighthouse.mapper.VictimMapper;
import ru.mai.lighthouse.repository.victim.VictimRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class VictimService {

    private final VictimRepository victimRepository;
    private final VictimMapper victimMapper;

    public VictimResponse create(VictimCreateRequest request) {
        log.info("Создание записи о пропавшем: {} {}", request.getFirstName(), request.getLastName());

        VictimRequest victimRequest = victimMapper.toRequest(request);

        return victimRepository.create(victimRequest);
    }

    public VictimResponse getById(Long id) {
        log.info("Получение записи о пропавшем по ID: {}", id);
        return victimRepository.getById(id)
                .orElseThrow(() -> new ru.mai.lighthouse.exception.NotFoundException(
                        String.format("Запись о пропавшем с ID %d не найдена", id)));
    }

    public List<VictimResponse> getAll() {
        log.info("Получение всех записей о пропавших");
        return victimRepository.getAll();
    }

    public List<VictimResponse> getByCity(String city) {
        log.info("Получение записей о пропавших по городу: {}", city);
        return victimRepository.getByCity(city);
    }

    public VictimResponse update(Long id, VictimRequest request) {
        log.info("Обновление записи о пропавшем: {}", id);
        return victimRepository.update(id, request);
    }

    public void delete(Long id) {
        log.info("Удаление записи о пропавшем: {}", id);
        victimRepository.delete(id);
    }

    public BatchResponse<VictimResponse> batchCreate(BatchRequest<VictimRequest> request) {
        log.info("Пакетное создание записей о пропавших: {} элементов", request.getItems().size());

        List<VictimResponse> successItems = request.getItems().stream()
                .map(victimRepository::create)
                .collect(Collectors.toList());

        return new BatchResponse<VictimResponse>()
                .setSuccessCount(successItems.size())
                .setErrorCount(0)
                .setSuccessItems(successItems)
                .setErrors(List.of());
    }

    public BatchResponse<VictimResponse> batchUpdate(BatchRequest<VictimRequest> request) {
        log.info("Пакетное обновление записей о пропавших: {} элементов", request.getItems().size());

        List<BatchError> errors = IntStream.range(0, request.getItems().size())
                .mapToObj(index -> new BatchError(index, "Для обновления требуется ID записи"))
                .collect(Collectors.toList());

        return new BatchResponse<VictimResponse>()
                .setSuccessCount(0)
                .setErrorCount(request.getItems().size())
                .setSuccessItems(List.of())
                .setErrors(errors);
    }
}
