package com.todolist.services.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.config.SlackConfig;
import com.todolist.exception.TodoException;
import com.todolist.model.SlackMessage;
import com.todolist.model.SlackMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SlackService {
    private static final String ERROR_IN_POST_SLACK_MESSAGE = "Error in post slack message";
    private static final String COULD_NOT_POST_SLACK_MESSAGE = "Could not post slack message {}";

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final SlackConfig slackConfig;
    private final RetryTemplate retryTemplate;

    @Autowired
    public SlackService(RestTemplateBuilder restTemplateBuilder, SlackConfig slackConfig, RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
        this.slackConfig = slackConfig;
    }

    public Optional<SlackMessageResponse> postMessage(String channelId, String message) {
        try {
            channelId = channelId != null ? channelId : slackConfig.getChannelId();
            SlackMessage slackMessage = SlackMessage.builder()
                    .channelId(channelId)
                    .messageContent(message)
                    .build();
            return postMessageWithRetries(slackMessage);
        } catch (Exception e) {
            final String errorMessage = COULD_NOT_POST_SLACK_MESSAGE;
            throw new TodoException(errorMessage, e);
        }
    }

    private Optional<SlackMessageResponse> postMessageWithRetries
            (final SlackMessage message) {
        return retryTemplate.execute(context -> postMessage(message), context -> {
            return Optional.empty();
        });
    }

    private Optional<SlackMessageResponse> postMessage(final SlackMessage message) {
        HttpPost httpPost = new HttpPost(slackConfig.getSlackChatApiEndpoint());
        SlackMessageResponse slackPostMessageResponse = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(message);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, slackConfig.getAuthorization());

            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial((chain, authType) -> true).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201)
                    slackPostMessageResponse = SlackMessageResponse.builder().success(true).threadId(UUID.randomUUID().toString()).build();
                else
                    slackPostMessageResponse = SlackMessageResponse.builder().success(false).threadId(UUID.randomUUID().toString()).build();
            } finally {
                response.close();
                httpclient.close();
            }
        } catch (Exception e) {
            final String errorMessage = ERROR_IN_POST_SLACK_MESSAGE;
            log.debug(errorMessage, e);
        }
        return Optional.ofNullable(slackPostMessageResponse);
    }
}
