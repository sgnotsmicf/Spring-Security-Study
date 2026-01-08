package cn.sgnxotsmicf.result;

import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/4 17:20
 * @Version: 1.0
 * @Description: 状态码枚举类
 */

@Getter
public enum StateCode {

    /**
     * 成功
     */
    SUCCESS(200),

    /**
     * 失败
     */
    FAIL(400),

    /**
     * 权限不足
     */
    ACCESS_DENIED(403);

    private final int code;

    StateCode(int code) {
        this.code = code;
    }

}
