package com.akkodis.technical.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.akkodis.technical.TestUtils;
import com.akkodis.technical.domain.dto.PriceDTO;
import com.akkodis.technical.domain.service.PriceService;
import com.akkodis.technical.domain.vo.PriceData;
import java.util.Collections;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceControllerTest {

  @Mock
  private PriceService service;

  @InjectMocks
  private PriceController controller;

  @Test
  void getPriceDataForGivenBrandProductIdAndDate_shouldMakeExpectedCalls() {
    final Long brandId = TestUtils.VALID_BRAND_ID;
    final Long productId = TestUtils.VALID_PRODUCT_ID;
    final String date = TestUtils.VALID_DATE;
    mockGetPriceDataForGivenBrandProductIdAndDate(new PriceData());

    controller.getPriceDataForGivenBrandProductIdAndDate(brandId, productId, date);

    verify(service, only())
        .getPriceDataForGivenBrandProductIdAndDate(eq(brandId), eq(productId), eq(date));
  }

  @Test
  void getPriceDataForGivenBrandProductIdAndDate_whenOk_shouldReturnExpectedValue() {
    final PriceData expected = new EasyRandom().nextObject(PriceData.class);
    final Long brandId = expected.getBrand().getCode();
    final Long productId = expected.getBrand().getCode();
    final String date = expected.getStartDate().toString();
    mockGetPriceDataForGivenBrandProductIdAndDate(expected);

    final PriceDTO result = controller.getPriceDataForGivenBrandProductIdAndDate(brandId, productId, date);

    assertNotNull(result);
    assertNotNull(result.getPriceDataList());
    assertFalse(result.getPriceDataList().isEmpty());
    final PriceData resultData = result.getPriceDataList().get(0);
    assertEquals(1, result.getPriceDataList().size());
    assertNotNull(resultData.getProductId());
    assertEquals(expected.getProductId(), resultData.getProductId());
    assertNotNull(resultData.getBrand());
    assertEquals(expected.getBrand(), resultData.getBrand());
    assertNotNull(resultData.getPriceValue());
    assertEquals(expected.getPriceValue(), resultData.getPriceValue());
    assertNotNull(resultData.getPriceList());
    assertEquals(expected.getPriceList(), resultData.getPriceList());
    assertNotNull(resultData.getCurrency());
    assertEquals(expected.getCurrency(), resultData.getCurrency());
    assertNotNull(resultData.getStartDate());
    assertEquals(expected.getStartDate(), resultData.getStartDate());
    assertNotNull(resultData.getEndDate());
    assertEquals(expected.getEndDate(), resultData.getEndDate());
  }

  private void mockGetPriceDataForGivenBrandProductIdAndDate(final PriceData expected) {
    when(service.getPriceDataForGivenBrandProductIdAndDate(anyLong(), anyLong(), anyString()))
        .thenReturn(Collections.singletonList(expected));
  }

}