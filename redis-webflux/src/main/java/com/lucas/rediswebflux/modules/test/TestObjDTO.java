package com.lucas.rediswebflux.modules.test;

import lombok.Data;

/**
 * @package : com.lucas.rediswebflux.modules.test
 * @name : TestObjDTO.java
 * @date : 2025. 2. 27. 오후 3:25
 * @author : lucaskang(swings134man)
 * @Description: Production 에서 사용 할 수 있는 형식의 Test DTO Object
**/
@Data
public class TestObjDTO {
    
    private Long id;
    private String content;
}
