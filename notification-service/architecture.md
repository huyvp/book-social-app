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