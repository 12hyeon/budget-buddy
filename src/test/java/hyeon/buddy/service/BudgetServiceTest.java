package hyeon.buddy.service;

import hyeon.buddy.domain.Budget;
import hyeon.buddy.domain.Category;
import hyeon.buddy.domain.User;
import hyeon.buddy.dto.BudgetSaveRequestDTO;
import hyeon.buddy.dto.BudgetUpdateRequestDTO;
import hyeon.buddy.enums.UserRole;
import hyeon.buddy.enums.UserStatus;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.repository.BudgetRepository;
import hyeon.buddy.repository.CategoryRepository;
import hyeon.buddy.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    @DisplayName("이번 달 - 예산 수정")
    public void testUpdateBudget() {
        // Given
        UserDetails userDetails = new UserDetailsTest("1","pw");
        Long uid = 1L;
        User user = new User(uid, "test","email@gmail.com",
                "pw", UserRole.ROLE_USER, UserStatus.UNVERIFIED);

        Long bid = 1L;
        BudgetUpdateRequestDTO dto = new BudgetUpdateRequestDTO(30000);

        BudgetSaveRequestDTO saveDto = new BudgetSaveRequestDTO(300000, 1L, "2023-11");
        Category ctg = new Category();
        Budget budget = Budget.from(saveDto, user, ctg);

        // when
        when(budgetRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(budgetRepository.findById(bid)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(Mockito.any())).thenReturn(budget);

        ExceptionResponse response = budgetService.updateBudget(userDetails, bid, dto);

        // then
        assertEquals(ExceptionCode.BUDGET_IMMUTABLE.getCode(), response.getCode());
    }
}
