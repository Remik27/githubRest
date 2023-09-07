You can use this up for find repositories and branches of some user. You can use this command in terminal  docker run --name github-rest-app -p 8190:8190 github-rest-app
 for start application on your computer. Application allows to use swagger localhost:8190/githubRest/swagger-ui/index.html or curl like this : curl -X 'GET' \
  'http://localhost:8190/githubRest/api/repositories/username' \
  -H 'accept: application/json'
