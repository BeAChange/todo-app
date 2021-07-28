#todo app
###tech stack
- spring 
    - boot 
    - web
    - batch 
    - security
    - jpa repository
    - actuator
    - dev tools
- jwt 
- h2db (in memory)
- slack-api
- docker-compose
- lombok

## local setup
### prerequisite 
- apache maven 3.x
- openjdk 11 +
- slack app & channel
- docker

### launch application
- import code into IDE
- start application (com.todolist.Application)
- access swagger url from web browser
   `http://localhost:8080/swagger-ui.html`
   
### access api(s)
- obtain JWT token:
    ``/api/v1/login``
    
    ``{
        "login": "admin",
        "password": "123"
      }``

- create todo-list:
    ````
    Authorization: 
    Bearer xyz............
    
    Request Payload:
    {
      "createdDate": "2021-07-28T15:26:41.353Z",
      "id": 0,
      "modifiedDate": "2021-07-28T15:26:41.353Z",
      "tasks": [
        {
          "active": true,
          "createdDate": "2021-08-31T15:26:41.353Z",
          "description": "Perform detailed analysis ",
          "dueDate": "2021-08-03T15:26:41.353Z",
          "id": 0,
          "prvTotalTimeSpent": 0,
          "status": "NS",
          "title": "Sample - Task",
          "totalTimeSpent": 0
        }
      ],
      "title": "Sample - Todo list"
    }
  
    ````
- use authorization token for subsequent api(s) calls

- slack notification can be triggered manually for task's due in "no. of days": 
    ````
    controller : task-controller

    api endpoint: 
    /api/v1/tasks/sendTaskDueMessage
    Send Task Due Messages to Slack Channel
    
    Authorization: 
    Bearer xyz............
  
    channelId:
    C028L7AMRS8
  
    message:
    Sample - Task is about to due in 2 days 
        
    noOfDays:
    2
  
    ````
