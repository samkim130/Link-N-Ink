# Link & Ink
Social Media Tattoo App

Work in progress for a tattoo social media app that connects artists to app users/potential customers

credit to a friend (UX/UI designer student: jaeheegil96@gmail.com) for initial design and ideas



### Technologies:
  Spring Boot - backend\
  Spring Data & temporary PostgreSQL on local docker machine\
  React - frontend (https://github.com/samkim130/linkink-web) \
  Amazon S3 - for image uploads and other future data

### Things to note:
to use your own Amazon S3 features, create your own account and save the info in a "cred.txt" in the folder where AmazonS3Config.java is located \
also start a local database on docker using `sh start_postgres.sh` in the bin folder


### Next Steps...
1. upload to cloud and establish database (probably Heroku)
2. add more functionalities as designed by the UX/UI designer
3. clean up React code
4. Add Security (OAuth2 and 3rd party app access)
5. Connect RabbitMQ
6. Begin building on React native

