package xin.ryven.flashsale.order.web;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import xin.ryven.flashsale.order.entity.FlashSale;
import xin.ryven.flashsale.order.utils.JsonU;
import xin.ryven.flashsale.order.vo.Result;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlashSaleControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFlashSaleList() {
        String uri = "/flash-sale/list";
        // 发送 HTTP 请求并获取响应
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        // 验证响应状态码是否为200
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Result<List<FlashSale>> result = new JsonU().fromStr(response.getBody(), new TypeToken<>() {
        });
        assertEquals(result.getErrorCode(), 0);
    }
}