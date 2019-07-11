package mobileapp.ctemplar.com.ctemplarapp.main;

import java.util.List;

public class ResponseMessagesData {
    public final List<MessageProvider> messages;
    public final String folderName;

    public ResponseMessagesData(List<MessageProvider> messages, String folderName) {
        this.messages = messages;
        this.folderName = folderName;
    }
}