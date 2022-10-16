package com.akkodis.technical.application.service;

import com.akkodis.technical.application.port.secondary.PriceRepository;
import com.akkodis.technical.domain.entity.PriceEntity;
import com.akkodis.technical.domain.exception.NotFoundPriceException;
import com.akkodis.technical.domain.service.PriceService;
import com.akkodis.technical.domain.vo.PriceConstants;
import com.akkodis.technical.domain.vo.PriceData;
import com.akkodis.technical.domain.vo.Priority;
import com.akkodis.technical.infrastructure.mapper.PriceMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetPriceUseCase implements PriceService {

  private final PriceRepository priceRepo;

  private final PriceMapper priceMapper;

  @Autowired
  public GetPriceUseCase(final PriceRepository priceRepo, final PriceMapper priceMapper) {
    this.priceRepo = priceRepo;
    this.priceMapper = priceMapper;
  }

  @Override
  public List<PriceData> getPriceDataForGivenBrandProductIdAndDate(final Long brand,
      final Long productId, final String date) {
    final LocalDateTime requestDate = mapStringToLocalDateTime(date);

    final List<PriceEntity> priceEntityList = priceRepo.findByBrandIdAndProductId(brand, productId)
        .orElseThrow(NotFoundPriceException::new);

    List<PriceEntity> filteredList = filterByDate(priceEntityList, requestDate);

    filteredList = (isMultipleResult(filteredList) ? filterByHighPriority(filteredList)
        : filteredList);

    if (filteredList.isEmpty()) {
      throw new NotFoundPriceException();
    }

    return mapEntityListToDataList(filteredList);
  }

  private LocalDateTime mapStringToLocalDateTime(String dateString) {
    dateString = dateString.replace(PriceConstants.DATE_REMOVE_CHAR,
        PriceConstants.DATE_REPLACE_CHAR);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PriceConstants.DATE_FORMAT);
    return LocalDateTime.parse(dateString, formatter);
  }

  private List<PriceEntity> filterByDate(final List<PriceEntity> priceEntityList,
      final LocalDateTime date) {
    return priceEntityList.stream()
        .filter(entity -> entity.getStartDate().isBefore(date))
        .filter(entity -> entity.getEndDate().isAfter(date))
        .collect(Collectors.toList());
  }

  private Boolean isMultipleResult(final List<PriceEntity> priceEntityList) {
    return priceEntityList.size() > PriceConstants.SIZE_ONE;
  }

  private List<PriceEntity> filterByHighPriority(final List<PriceEntity> priceEntityList) {
    return priceEntityList.stream()
        .filter(entity -> entity.getPriority().equals(Priority.HIGH.getCode()))
        .collect(Collectors.toList());
  }

  private List<PriceData> mapEntityListToDataList(final List<PriceEntity> priceEntityList) {
    return priceEntityList.stream()
        .map(priceMapper::mapPriceEntityToData)
        .collect(Collectors.toList());
  }

}
