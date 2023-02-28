package com.dev.responses;

import com.dev.models.MessageModel;
import com.dev.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesResponse extends BasicResponse {
    private List<MessageModel> messageList;

    public MessagesResponse (List<Message> messages, int userId) {
        this.setSuccess(true);
        this.messageList = new ArrayList<>();
        for (Message message : messages) {
            this.messageList.add(new MessageModel(message, userId));
        }
    }


    public List<MessageModel> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageModel> messageList) {
        this.messageList = messageList;
    }
}
