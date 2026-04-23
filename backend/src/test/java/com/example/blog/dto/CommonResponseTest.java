package com.example.blog.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonResponseTest {

    @Test
    void ok_shouldCreateSuccessResponse() {
        String data = "test data";
        CommonResponse<String> response = CommonResponse.ok(data);

        assertEquals(200, response.getCode());
        assertEquals("成功", response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void ok_withNullData_shouldCreateSuccessResponse() {
        CommonResponse<Void> response = CommonResponse.ok(null);

        assertEquals(200, response.getCode());
        assertEquals("成功", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void fail_shouldCreateErrorResponse() {
        CommonResponse<Void> response = CommonResponse.fail(400, "Bad Request");

        assertEquals(400, response.getCode());
        assertEquals("Bad Request", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void fail_withUnauthorized_shouldCreate401Response() {
        CommonResponse<Void> response = CommonResponse.fail(401, "Unauthorized");

        assertEquals(401, response.getCode());
        assertEquals("Unauthorized", response.getMessage());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        CommonResponse<String> response = new CommonResponse<>();
        response.setCode(200);
        response.setMessage("OK");
        response.setData("data");

        assertEquals(200, response.getCode());
        assertEquals("OK", response.getMessage());
        assertEquals("data", response.getData());
    }
}
