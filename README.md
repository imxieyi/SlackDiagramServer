# API Server for SlackDiagram
## Front end
[https://github.com/edwardfang/SNA4slack](https://github.com/edwardfang/SNA4slack)
## Data Crawler
[https://github.com/imxieyi/SlackArchiveCrawler](https://github.com/imxieyi/SlackArchiveCrawler)
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

**Get basic statistics of a team or channel:**
`/api/channel/info?team={TEAM_ID}&channel={CHANNEL_ID}&from={FROM_TIME}&to={TO_TIME}`

Channel is optional.
Note that `earliest message` is independent from given time range.

### User
**Get all users of a channel:**
`/api/user/all?team={TEAM_ID}&channel={CHANNEL_ID}`

Channel is optional.

**Get messages of a user within a time range:**
`/api/user/message?team={TEAM_ID}&channel={CHANNEL_ID}&user={USER_ID}&from={FROM_TIME}&to={TO_TIME}&length={LENGTH}&offset={OFFSET}`

Channel, length, offset are optional.

**Get days after a user send the first message in a channel:**
`/api/user/joindays?team={TEAM_ID}&channel={CHANNEL_ID}&user={USER_ID}`

Channel is optional.

### Mention
**Get the statistics of mentions within a time range:**
`/api/mention?team={TEAM_ID}&channel={CHANNEL_ID}&from={FROM_TIME}&to={TO_TIME}`

Channel is optional.

**Get messages of mentions with a pair of users:**
`/api/mention/message?team={TEAM_ID}&channel={CHANNEL_ID}&from={FROM_TIME}&to={TO_TIME}&user1={USER_ID}&user2={ANOTHER_USER_ID}`

Channel is optional.

**Get days after two users mention each other for the first time in a channel:**
`/api/mention/meetdays?team={TEAM_ID}&channel={CHANNEL_ID}&user1={USER_ID}&user2={ANOTHER_USER_ID}`

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