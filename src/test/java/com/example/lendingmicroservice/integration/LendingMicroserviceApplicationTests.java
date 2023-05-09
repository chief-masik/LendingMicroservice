package com.example.lendingmicroservice.integration;

import com.example.lendingmicroservice.constants.StatusEnum;
import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.entity.LoanOrderDeleteDTO;
import com.example.lendingmicroservice.entity.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class LendingMicroserviceApplicationTests {
	private final static String fakeOrderId = "11bb1111-11e1-11e1-11fa-a1b111c1abe1";
	private final static Matcher<String> uuidMatcher = Matchers.matchesRegex("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void givenWac_whenServletContext_thenItProvidesControllers() {
		ServletContext servletContext = webApplicationContext.getServletContext();

		assertNotNull(servletContext);
		assertTrue(servletContext instanceof MockServletContext);
		assertNotNull(webApplicationContext.getBean("loanOrderController"));
	}

	@Test
	public void getMapping_getTariffs() throws Exception {

		this.mockMvc.perform(get("/loan-service/getTariffs"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.tariffs[0].id").value(1))
				.andExpect(jsonPath("$.data.tariffs[0].type").value("standard"))
				.andExpect(jsonPath("$.data.tariffs[0].interestRate").value("12%"))
				.andExpect(jsonPath("$.data.tariffs[1].id").value(2))
				.andExpect(jsonPath("$.data.tariffs[1].type").value("premium"))
				.andExpect(jsonPath("$.data.tariffs[1].interestRate").value("9.9%"))
				.andExpect(jsonPath("$.data.tariffs[2].id").value(3))
				.andExpect(jsonPath("$.data.tariffs[2].type").value("elite"))
				.andExpect(jsonPath("$.data.tariffs[2].interestRate").value("8%"));
	}

	@Test
	public void postMapping_getOrders() throws Exception {
		UserDTO userDTO = new UserDTO(11111111L);

		this.mockMvc.perform(post("/loan-service/getOrders")
						.content(objectMapper.writeValueAsString(userDTO))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.orders[0].orderId").value("80bb0833-47e8-43e4-84fa-a3b045c7abe1"))
				.andExpect(jsonPath("$.data.orders[0].tariffId").value(1))
				.andExpect(jsonPath("$.data.orders[0].status").value("APPROVED"))
				.andExpect(jsonPath("$.data.orders[0].timeInsert").value("2023-04-24T01:36:15"))
				.andExpect(jsonPath("$.data.orders[0].timeUpdate").value("2023-04-24T01:38:15"));
	}

	@Test
	public void postMapping_newOrder_shouldReturnUUID_whenOrderCreated() throws Exception {
		LoanOrderCreateDTO loanOrderCreateDTO = new LoanOrderCreateDTO(123456789L, 1L);

		this.mockMvc.perform(post("/loan-service/order")
						.content(objectMapper.writeValueAsString(loanOrderCreateDTO))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.orderId").value(uuidMatcher));
	}
	@Test
	public void postMapping_newOrder_shouldThrowError_whenTariffNotExists() throws Exception {
		LoanOrderCreateDTO loanOrderCreateDTO = new LoanOrderCreateDTO(123456789L, 12345L);

		this.mockMvc.perform(post("/loan-service/order")
						.content(objectMapper.writeValueAsString(loanOrderCreateDTO))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value("TARIFF_NOT_FOUND"))
				.andExpect(jsonPath("$.error.message").value("Тариф не найден"));
	}
	@Test
	public void postMapping_newOrder_shouldThrowError_whenStatusApproved() throws Exception {
		LoanOrderCreateDTO loanOrderCreateDTO = new LoanOrderCreateDTO(11111111L, 1L);

		this.mockMvc.perform(post("/loan-service/order")
						.content(objectMapper.writeValueAsString(loanOrderCreateDTO))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value("LOAN_ALREADY_APPROVED"))
				.andExpect(jsonPath("$.error.message").value("Заявка уже одобрена"));
	}
	@Test
	public void postMapping_newOrder_shouldThrowError_whenStatusRefusedAndTimeUpdateLessThan2MinutesAgo() throws Exception {
		LoanOrderCreateDTO loanOrderCreateDTO = new LoanOrderCreateDTO(22222222L, 2L);

		this.mockMvc.perform(post("/loan-service/order")
						.content(objectMapper.writeValueAsString(loanOrderCreateDTO))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value("TRY_LATER"))
				.andExpect(jsonPath("$.error.message").value("Попробуйте позже"));
	}
	@Test
	public void postMapping_newOrder_shouldThrowError_whenStatusInProgress() throws Exception {
		LoanOrderCreateDTO loanOrderCreateDTO = new LoanOrderCreateDTO(33333333L, 3L);

		this.mockMvc.perform(post("/loan-service/order")
						.content(objectMapper.writeValueAsString(loanOrderCreateDTO))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value("LOAN_CONSIDERATION"))
				.andExpect(jsonPath("$.error.message").value("Заявка на рассмотрении"));
	}

	@Test
	public void getMapping_getStatusOrder_shouldReturnOrderStatus_whenOrderExists() throws Exception {
		String realOrderId = "80bb0833-47e8-43e4-84fa-a3b045c7abe1";

		this.mockMvc.perform(get("/loan-service/getStatusOrder")
						.param("orderId", realOrderId))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.orderStatus").value(StatusEnum.APPROVED.toString()));
	}
	@Test
	public void getMapping_getStatusOrder_shouldReturnError_whenOrderNotExists() throws Exception {

		this.mockMvc.perform(get("/loan-service/getStatusOrder")
						.param("orderId", fakeOrderId))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value("ORDER_NOT_FOUND"))
				.andExpect(jsonPath("$.error.message").value("Заявка не найдена"));
	}

	@Test
	public void deleteMapping_deleteLoanOrder_shouldReturnStatusOK_whenOrderDeleted() throws Exception {
		LoanOrderDeleteDTO loanOrderDeleteDTO = new LoanOrderDeleteDTO(44444444L, UUID.fromString("ee7194f2-e908-11ed-a05b-0242ac120003"));

		this.mockMvc.perform(delete("/loan-service/deleteOrder")
					.content(objectMapper.writeValueAsString(loanOrderDeleteDTO))
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
	@Test
	public void deleteMapping_deleteLoanOrder_shouldReturnError_whenOrderNotDeleted() throws Exception {
		LoanOrderDeleteDTO loanOrderDeleteDTO = new LoanOrderDeleteDTO(44444444L, UUID.fromString(fakeOrderId));

		this.mockMvc.perform(delete("/loan-service/deleteOrder")
						.content(objectMapper.writeValueAsString(loanOrderDeleteDTO))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value("ORDER_IMPOSSIBLE_TO_DELETE"))
				.andExpect(jsonPath("$.error.message").value("Невозможно удалить заявку"));
	}
}
