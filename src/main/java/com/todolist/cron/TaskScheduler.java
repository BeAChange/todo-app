package com.todolist.cron;

import com.todolist.config.SlackConfig;
import com.todolist.entities.Task;
import com.todolist.exception.TodoException;
import com.todolist.services.TaskService;
import com.todolist.services.impl.SlackService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class TaskScheduler {

    private TaskService taskService;
    private SlackService slackService;
    private SlackConfig slackConfig;


    @Autowired
    public TaskScheduler(TaskService taskService,
                         SlackService slackService, SlackConfig slackConfig) {
        this.taskService = taskService;
        this.slackService = slackService;
        this.slackConfig = slackConfig;
    }

    //@Scheduled(cron = "60 * * * * ?")
    public void scheduleNotificationForAllTaskDue() {

        try {
            List<Task> taskList = taskService.findAllTaskWithDueDate(1);
            StringBuilder sb = new StringBuilder();
            taskList.forEach(task -> {
                sb.append(task.getId() + ":" + task.getDescription() + "\n");
            });
            String content = "Task Pending Need Attention:\n" + "" + sb.toString();
            slackService.postMessage(slackConfig.getChannelId(), content).get();
        } catch (Exception e) {
            throw new TodoException("Could not send message. Reason:" + e.getMessage());
        }
    }
}
