# API Server for SlackDiagram
## Database
Please refer to `slackdata.mwb`.
## Configuration
Create **config.properties** in root directory:
```properties
db_host=127.0.0.1
db_port=3306
db_schema=schema
db_user=user
db_password=123456
```
## Usage
### Team
Get all available teams:
`/api/team`

Query a team by domain:
`/api/team?domain={TEAM_DOMAIN}`

### Channel
Get all available channels of a team:
`/api/channel?team={TEAM_ID}`

### User
Get all users of a team:
`/api/user/all?team={TEAM_ID}`

### Mention
Get the statistics of mentions within a date range:
`/api/mention?team={TEAM_ID}&channel={CHANNEL_ID}&from={FROM_TIME}&to={TO_TIME}`

Note that `channel` is optional. Times are in Unix epoch format with unit `second`.