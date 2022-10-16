package com.akkodis.technical.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.akkodis.technical.TestUtils;
import com.akkodis.technical.application.port.secondary.PriceRepository;
import com.akkodis.technical.domain.entity.PriceEntity;
import com.akkodis.technical.domain.exception.NotFoundPriceException;
import com.akkodis.technical.domain.vo.Brand;
import com.akkodis.technical.domain.vo.Currency;
import com.akkodis.technical.domain.vo.PriceConstants;
import com.akkodis.technical.domain.vo.PriceData;
import com.akkodis.technical.domain.vo.Priority;
import com.akkodis.technical.infrastructure.mapper.PriceMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetPriceUseCaseTest {

  @Mock
  private PriceRepository repo;

  @Mock
  private PriceMapper mapper;

  @InjectMocks
  private GetPriceUseCase useCase;

  @Test
  void getPriceDataForGivenBrandProductIdAndDate_whenCalled_shouldMakeExpectedCalls() {
    final PriceEntity expected = getValidPriceEntity();
    final Long brandId = expected.getBrandId();
    final Long productId = expected.getProductId();
    mockPriceRepoFindByBrandIdAndProductId(expected);
    mockPriceMapperMapEntityListToResult(expected);

    useCase.getPriceDataForGivenBrandProductIdAndDate(brandId, productId, TestUtils.VALID_DATE);

    verify(repo, only()).findByBrandIdAndProductId(eq(brandId), eq(productId));
    verify(mapper, only()).mapPriceEntityToData(eq(expected));
  }

  @Test
  void getPriceDataForGivenBrandProductIdAndDate_whenFound_shouldReturnExpectedValue() {
    final PriceEntity expected = getValidPriceEntity();
    final Long brandId = expected.getBrandId();
    final Long productId = expected.getProductId();
    mockPriceRepoFindByBrandIdAndProductId(expected);
    mockPriceMapperMapEntityListToResult(expected);

    final List<PriceData> result = useCase.getPriceDataForGivenBrandProductIdAndDate(brandId,
        productId, TestUtils.VALID_DATE);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertNotNull(result.get(0).getProductId());
    assertEquals(expected.getProductId(), result.get(0).getProductId());
    assertNotNull(result.get(0).getBrand());
    assertEquals(expected.getBrandId(), result.get(0).getBrand().getCode());
    assertNotNull(result.get(0).getPriceValue());
    assertEquals(expected.getPriceValue(), result.get(0).getPriceValue());
    assertNotNull(result.get(0).getPriceList());
    assertEquals(expected.getPriceList(), result.get(0).getPriceList());
    assertNotNull(result.get(0).getCurrency());
    assertEquals(expected.getCurr(), result.get(0).getCurrency().getCode());
    assertNotNull(result.get(0).getStartDate());
    assertEquals(expected.getStartDate(), result.get(0).getStartDate());
    assertNotNull(result.get(0).getEndDate());
    assertEquals(expected.getEndDate(), result.get(0).getEndDate());
  }

  @Test
  void getPriceDataForGivenBrandProductIdAndDate_whenNotFound_shouldReturnExpectedException() {
    final PriceEntity expected = getValidPriceEntity();
    final Long brandId = expected.getBrandId();
    final Long productId = expected.getProductId();
    final String invalidDate = "2030-06-14 17:00:00";
    mockPriceRepoFindByBrandIdAndProductId(expected);

    assertThrows(NotFoundPriceException.class, () ->
        useCase.getPriceDataForGivenBrandProductIdAndDate(brandId, productId, invalidDate));
  }

  @Test
  void getPriceDataForGivenBrandProductIdAndDate_whenNoResultOnceFiltered_shouldReturnExpectedException() {
    final Long invalidBrandId = 99L;
    final Long invalidProductId = 88L;
    when(repo.findByBrandIdAndProductId(anyLong(), anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundPriceException.class,
        () -> useCase.getPriceDataForGivenBrandProductIdAndDate(invalidBrandId, invalidProductId,
            TestUtils.VALID_DATE));
  }

  private PriceEntity getValidPriceEntity() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PriceConstants.DATE_FORMAT);

    final PriceEntity result = new PriceEntity();
    result.setBrandId(Brand.ZARA.getCode());
    result.setPriceList(1);
    result.setPriceValue(35.50);
    result.setCurr(Currency.EURO.getCode());
    result.setStartDate(LocalDateTime.parse("2020-06-14 10:00:00", formatter));
    result.setEndDate(LocalDateTime.parse("2020-06-14 20:00:00", formatter));
    result.setPriority(Priority.HIGH.getCode());
    result.setProductId(35455L);

    return result;
  }

  private void mockPriceRepoFindByBrandIdAndProductId(final PriceEntity result) {
    when(repo.findByBrandIdAndProductId(anyLong(), anyLong()))
        .thenReturn(Optional.of(Collections.singletonList(result)));
  }

  private void mockPriceMapperMapEntityListToResult(final PriceEntity entity) {
    final PriceData result = new PriceData();
    result.setProductId(entity.getProductId());
    result.setBrand(Brand.getByValue(entity.getBrandId()));
    result.setPriceValue(entity.getPriceValue());
    result.setPriceList(entity.getPriceList());
    result.setCurrency(Currency.getByValue(entity.getCurr()));
    result.setStartDate(entity.getStartDate());
    result.setEndDate(entity.getEndDate());

    when(mapper.mapPriceEntityToData(any(PriceEntity.class))).thenReturn(result);
  }

}