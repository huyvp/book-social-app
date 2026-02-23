## Prerequisites

### Mongodb
Install Mongodb from Docker Hub:
`docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin mongo:latest`

# Notification Microservice Architecture

```mermaid
flowchart LR

    NMS[Notification microservice]

    NS[NotificationService]
    TS[TemplateService]
    DS[DeviceService]
    NMSGS[NotificationMessageService]
    USS[UserSettingsService]

    PNS[PushNotificationService]
    ES[EmailService]
    SMS[SmsService]

    NMS --> NS
    NMS --> TS
    NMS --> DS
    NMS --> NMSGS
    NMS --> USS

    NS --> PNS
    NS --> ES
    NS --> SMS
```