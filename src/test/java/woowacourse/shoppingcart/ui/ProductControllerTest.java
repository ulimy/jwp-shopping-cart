package woowacourse.shoppingcart.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import woowacourse.shoppingcart.dto.AddProductRequest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("상품추가 - 이름에 빈값은 허용하지 않는다.")
    @ParameterizedTest
    @NullAndEmptySource
    void addWithNullName(String name) throws Exception {
        AddProductRequest request = new AddProductRequest(name, 1000, "이미지주소");
        assertBadRequestFromPost(request, "이름은 빈 값일 수 없습니다.");
    }

    @DisplayName("상품추가 - 금액은 양의 정수만 허용한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1000, 0})
    void addWithNegative(int price) throws Exception {
        AddProductRequest request = new AddProductRequest("이름", price, "이미지주소");
        System.out.println(objectMapper.writeValueAsString(request));
        assertBadRequestFromPost(request, "금액은 양의 정수만 허용합니다.");
    }

    @DisplayName("상품추가 - 금액이 int의 범위를 넘어가는 경우는 허용하지 않는다.")
    @Test
    void maxPrice() throws Exception {
        assertBadRequestFromPost("{\"name\":\"이름\",\"price\":2147483648,\"imageUrl\":\"이미지주소\"}", "out of range of int");
    }

    @DisplayName("상품추가 - 이미지 주소에 빈값은 허용하지 않는다.")
    @ParameterizedTest
    @NullAndEmptySource
    void addWithNullImageUrl(String imageUrl) throws Exception {
        AddProductRequest request = new AddProductRequest("이름", 1000, imageUrl);
        assertBadRequestFromPost(request, "이미지 주소는 빈 값일 수 없습니다.");
    }

    void assertBadRequestFromPost(String request, String errorMessage) throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(errorMessage)));
    }

    void assertBadRequestFromPost(Object request, String errorMessage) throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(errorMessage)));
    }
}
