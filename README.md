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
**Get all available teams:**
`/api/team`

**Query a team by domain:**
`/api/team?domain={TEAM_DOMAIN}`

### Channel
**Get all available channels of a team:**
`/api/channel?team={TEAM_ID}`

### User
**Get all users of a team:**
`/api/user/all?team={TEAM_ID}`

### Mention
**Get the statistics of mentions within a date range:**
`/api/mention?team={TEAM_ID}&channel={CHANNEL_ID}&from={FROM_TIME}&to={TO_TIME}`

Channel is optional.

### Message
**Get count of messages by all participants:**
`/api/message/count?team={TEAM_ID}&channel={CHANNEL_ID}&from={FROM_TIME}&to={TO_TIME}`

Channel is optional.

**Get count of messages by a specific user:**
`/api/message/byuser?team={TEAM_ID}&channel={CHANNEL_ID}&user={USER_ID}`

Channel is optional.

**Get messages within a time range (ordered by time, the most recent first):**
`/api/message?team={TEAM_ID}&channel={CHANNEL_ID}&from={FROM_TIME}&to={TO_TIME}&length={LENGTH}&offset={OFFSET}`

`length` and `offset` are used to limit output.
The default value of `offset` is 0.
If `length` is not specified, no message will be returned.
Both `from` and `to` are optional.
Count of messages are also returned even if `length` and `offset` are missing.

Note that times are in Unix epoch format with unit `second`.