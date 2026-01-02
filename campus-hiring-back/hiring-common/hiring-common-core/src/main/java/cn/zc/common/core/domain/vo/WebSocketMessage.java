package cn.zc.common.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * WebSocket 推送消息统一结构。
 *
 * @param <T> 数据类型
 */
@Data
@AllArgsConstructor
public class WebSocketMessage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String type;

    private T data;

    private long timestamp;

    public static <T> WebSocketMessage<T> of(String type, T data) {
        return new WebSocketMessage<>(type, data, System.currentTimeMillis());
    }
}


