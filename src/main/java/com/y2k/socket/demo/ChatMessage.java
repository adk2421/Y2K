package com.y2k.socket.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    
    public enum MessageType {
        ENTER, TALK
    }

    // 메세지 타입
    private MessageType type;

    // 방 번호
    private String roomId;

    // 메세지 발신자
    private String sender;

    // 메세지
    private String message;
}
