package com.devb.thuchi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    private Transaction build(TransactionType type, String amount) {
        Transaction t = new Transaction();
        t.setType(type);
        t.setAmount(new BigDecimal(amount));
        t.setDate(LocalDate.of(2026, 10, 1));
        t.setDescription("test");
        return t;
    }

    @Test
    void summary_tinhDungTongThuChiVaSoDu() {
        when(repository.findAll()).thenReturn(List.of(
                build(TransactionType.INCOME, "15000000"),
                build(TransactionType.EXPENSE, "3000000"),
                build(TransactionType.EXPENSE, "2000000")
        ));

        Summary summary = service.getSummary();

        assertEquals(new BigDecimal("15000000"), summary.totalIncome());
        assertEquals(new BigDecimal("5000000"), summary.totalExpense());
        assertEquals(new BigDecimal("10000000"), summary.balance());
    }

    @Test
    void summary_khongCoGiaoDich_TraVe0() {
        when(repository.findAll()).thenReturn(List.of());

        Summary summary = service.getSummary();

        assertEquals(0, summary.balance().compareTo(BigDecimal.ZERO));
    }

    @Test
    void delete_idTonTai_goiDeleteById() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_idKhongTonTai_nemLoi404() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> service.delete(999L));

        verify(repository, never()).deleteById(anyLong());
    }
}