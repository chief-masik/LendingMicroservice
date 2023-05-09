package com.example.lendingmicroservice.unit.service.impl;

import com.example.lendingmicroservice.constants.StatusEnum;
import com.example.lendingmicroservice.entity.*;
import com.example.lendingmicroservice.repository.LoanOrderRepository;
import com.example.lendingmicroservice.response.exception.BusinessException;
import com.example.lendingmicroservice.service.LoanOrderMapper;
import com.example.lendingmicroservice.service.impl.LoanOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class LoanOrderServiceImplTest {
    private static final UUID uuid = UUID.randomUUID();
    private static final String approved = StatusEnum.APPROVED.toString();
    private static final String refused = StatusEnum.REFUSED.toString();
    private static final String in_progress = StatusEnum.IN_PROGRESS.toString();
    private static final LocalDateTime time = LocalDateTime.now();
    private static final LoanOrderCreateDTO loanOrderCreateDTO = new LoanOrderCreateDTO(11111111L, 1L);
    private static final LoanOrderDeleteDTO loanOrderDeleteDTO = new LoanOrderDeleteDTO(11111111L, UUID.randomUUID());
    @Mock
    private LoanOrderRepository loanOrderRepository;
    @Mock
    private LoanOrderMapper loanOrderMapper;
    @InjectMocks
    private LoanOrderServiceImpl loanOrderService;

    @Test
    public void canCreateLoanOrder_shouldThrowBusinessException_whenStatusInProgress() {                                    // IN_PROGRESS
        LoanOrder inProgressLoanOrder = new LoanOrder().setStatus(in_progress);
        when(loanOrderRepository.findByUserIdAndTariffId(loanOrderCreateDTO.getUserId(), loanOrderCreateDTO.getTariffId()))
                .thenReturn(Arrays.asList(inProgressLoanOrder));

        assertThrows(BusinessException.class, () -> {
            loanOrderService.canCreateLoanOrder(loanOrderCreateDTO);
        }, "Заявка на рассмотрении");
        verify(loanOrderRepository).findByUserIdAndTariffId(loanOrderCreateDTO.getUserId(), loanOrderCreateDTO.getTariffId());
    }
    @Test
    public void canCreateLoanOrder_shouldThrowBusinessException_whenStatusApproved() {                                      // APPROVED
        LoanOrder approvedLoanOrder = new LoanOrder().setStatus(approved);
        when(loanOrderRepository.findByUserIdAndTariffId(loanOrderCreateDTO.getUserId(), loanOrderCreateDTO.getTariffId()))
                .thenReturn(Arrays.asList(approvedLoanOrder));

        assertThrows(BusinessException.class, () -> {
            loanOrderService.canCreateLoanOrder(loanOrderCreateDTO);
        }, "Заявка уже одобрена");
        verify(loanOrderRepository).findByUserIdAndTariffId(loanOrderCreateDTO.getUserId(), loanOrderCreateDTO.getTariffId());
    }
    @Test
    public void canCreateLoanOrder_shouldThrowBusinessException_whenStatusRefusedAndTimeUpdateLessThan2MinutesAgo() {       // REFUSED
        LoanOrder refusedLoanOrder = new LoanOrder().setStatus(refused).setTimeUpdate(LocalDateTime.now().minus(119, ChronoUnit.SECONDS));
        when(loanOrderRepository.findByUserIdAndTariffId(loanOrderCreateDTO.getUserId(), loanOrderCreateDTO.getTariffId()))
                .thenReturn(Arrays.asList(refusedLoanOrder));

        assertThrows(BusinessException.class, () -> {
            loanOrderService.canCreateLoanOrder(loanOrderCreateDTO);
        }, "Попробуйте позже");
        verify(loanOrderRepository).findByUserIdAndTariffId(loanOrderCreateDTO.getUserId(), loanOrderCreateDTO.getTariffId());
    }
    @Test
    public void canCreateLoanOrder_shouldDoesNotThrow() {
        LoanOrder oldRefusedLoanOrder = new LoanOrder().setStatus(refused).setTimeUpdate(LocalDateTime.now().minus(121, ChronoUnit.SECONDS));
        when(loanOrderRepository.findByUserIdAndTariffId(loanOrderCreateDTO.getUserId(), loanOrderCreateDTO.getTariffId()))
                .thenReturn(Arrays.asList(oldRefusedLoanOrder));

        assertDoesNotThrow(() -> {
            loanOrderService.canCreateLoanOrder(loanOrderCreateDTO);
        });
        verify(loanOrderRepository).findByUserIdAndTariffId(loanOrderCreateDTO.getUserId(), loanOrderCreateDTO.getTariffId());
    }

    @Test
    public void getOrdersByUserId() {
        UserDTO userDTO = new UserDTO(123L);
        LoanOrder loanOrder1 = new LoanOrder().setId(123L).setStatus(refused);
        LoanOrder loanOrder2 = new LoanOrder().setId(123L).setStatus(approved);
        List<LoanOrder> listLoanOrder = Arrays.asList(loanOrder1, loanOrder2);
        LoanOrderToFront loanOrderToFront1 = LoanOrderToFront.builder().orderId(uuid).tariffId(1L).status(refused).timeInsert(time).timeUpdate(time).build();
        LoanOrderToFront loanOrderToFront2 = LoanOrderToFront.builder().orderId(uuid).tariffId(2L).status(approved).timeInsert(time).timeUpdate(time).build();
        List<LoanOrderToFront> expectedListLoanOrderToFront = Arrays.asList(loanOrderToFront1, loanOrderToFront2);
        when(loanOrderRepository.findLoanOrdersToFrontByUserId(123L)).thenReturn(listLoanOrder);
        when(loanOrderMapper.toFront(loanOrder1)).thenReturn(loanOrderToFront1);
        when(loanOrderMapper.toFront(loanOrder2)).thenReturn(loanOrderToFront2);

        List<LoanOrderToFront> actualListLoanOrderToFront = loanOrderService.getOrdersByUserId(userDTO);

        assertNotNull(actualListLoanOrderToFront);
        assertEquals(expectedListLoanOrderToFront, actualListLoanOrderToFront);
        verify(loanOrderRepository).findLoanOrdersToFrontByUserId(123L);
        verify(loanOrderMapper).toFront(loanOrder1);
        verify(loanOrderMapper).toFront(loanOrder2);
    }

    @Test
    public void setNewLoanOrder_shouldReturnUUID_whenSuccessfullyCalledRepository() {
        when(loanOrderRepository.saveLoanOrder(any(UUID.class), eq(loanOrderCreateDTO.getUserId()), eq(loanOrderCreateDTO.getTariffId()), any(Double.class), any(String.class), any(LocalDateTime.class))).thenReturn(1);

        UUID actualUuid = loanOrderService.setNewLoanOrder(loanOrderCreateDTO);

        assertNotNull(actualUuid);
        verify(loanOrderRepository).saveLoanOrder(any(UUID.class), eq(loanOrderCreateDTO.getUserId()), eq(loanOrderCreateDTO.getTariffId()), any(Double.class), any(String.class), any(LocalDateTime.class));
    }
    @Test
    public void setNewLoanOrder_shouldThrowBusinessException_whenIneffectivelyCalledRepository() {
        when(loanOrderRepository.saveLoanOrder(any(UUID.class), eq(loanOrderCreateDTO.getUserId()), eq(loanOrderCreateDTO.getTariffId()), any(Double.class), any(String.class), any(LocalDateTime.class))).thenReturn(0);

        assertThrows(BusinessException.class, () -> {
            loanOrderService.setNewLoanOrder(loanOrderCreateDTO);
        }, "Попробуйте еще раз");
        verify(loanOrderRepository).saveLoanOrder(any(UUID.class), eq(loanOrderCreateDTO.getUserId()), eq(loanOrderCreateDTO.getTariffId()), any(Double.class), any(String.class), any(LocalDateTime.class));
    }

    @Test
    public void getStatusOrder_shouldFindStatus_whenExists() {
        when(loanOrderRepository.getStatus(uuid)).thenReturn(approved);

        final String actualStatus = loanOrderService.getStatusOrder(uuid);

        assertNotNull(actualStatus);
        assertEquals(approved, actualStatus);
        verify(loanOrderRepository).getStatus(uuid);
    }
    @Test
    public void getStatusOrder_shouldThrowBusinessException_whenOrderNotExists() {
        when(loanOrderRepository.getStatus(uuid)).thenReturn(null);

        assertThrows(BusinessException.class, () -> {
            loanOrderService.getStatusOrder(uuid);
        }, "Заявка не найдена");
        verify(loanOrderRepository).getStatus(uuid);
    }

    @Test
    public void deleteLoanOrder_shouldDoesNotThrow_whenDeletedOrder() {
        when(loanOrderRepository.deleteByUserIdAndOrderId(loanOrderDeleteDTO.getUserId(), loanOrderDeleteDTO.getOrderId())).thenReturn(1);

        assertDoesNotThrow(() -> {
            loanOrderService.deleteLoanOrder(loanOrderDeleteDTO);
        });
        verify(loanOrderRepository).deleteByUserIdAndOrderId(loanOrderDeleteDTO.getUserId(), loanOrderDeleteDTO.getOrderId());
    }
    @Test
    public void deleteLoanOrder_shouldThrowBusinessException_whenNotDeletedOrder() {
        when(loanOrderRepository.deleteByUserIdAndOrderId(loanOrderDeleteDTO.getUserId(), loanOrderDeleteDTO.getOrderId())).thenReturn(0);

        assertThrows(BusinessException.class, () -> {
           loanOrderService.deleteLoanOrder(loanOrderDeleteDTO);
        }, "Невозможно удалить заявку");
        verify(loanOrderRepository).deleteByUserIdAndOrderId(loanOrderDeleteDTO.getUserId(), loanOrderDeleteDTO.getOrderId());
    }
}
