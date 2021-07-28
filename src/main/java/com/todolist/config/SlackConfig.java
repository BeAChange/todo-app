package com.todolist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackConfig {

    private static final String BEARER = "Bearer ";

    @Value("${slack.api.chat}")
    private String slackChatApiEndpoint;

    @Value("${slack.channel.id}")
    private String channelId;

    @Value("${slack.api.access.token}")
    private String slackAccessToken;


    public String getAuthorization() {
        return BEARER + slackAccessToken;
    }

    public String getSlackChatApiEndpoint() {
        return slackChatApiEndpoint;
    }

    public void setSlackChatApiEndpoint(String slackChatApiEndpoint) {
        this.slackChatApiEndpoint = slackChatApiEndpoint;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}