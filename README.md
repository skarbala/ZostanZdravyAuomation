# ZostanZdravyAuomation

## Mailosaur
1. register on [Mailosaur](https://mailosaur.com/) and create new virtual server
2. change `MAILOSAUR_KEY` and `MAILOSAUR_SERVER_ID` according to your account and server settings

## Browserstack
1. register on [BrowserStack](browserstack.com)
2. change `BS_NAME` and `BS_PASSWORD`

## Download app and upload it to Browserstack
1. download app from [here](https://apkcombo.com/zosta%C5%88-zdrav%C3%BD/sk.marekgogol.zostanzdravy/)
2. upload app to BrowserStack - you can use api, more info at this [link](https://www.browserstack.com/app-live/rest-api)
3. as result you'll get app id in format `bs://`{id}`
4. set value `BS_APP_ID` in project to the app id recieved from BS

RUN THE TEST :)
