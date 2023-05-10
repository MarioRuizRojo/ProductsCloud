FROM node:20-alpine3.16
ARG API_URL=nourl
ARG REACT_APP_API_URL=$API_URL
COPY . /defaultfolder
WORKDIR /defaultfolder
RUN npm install
RUN npm run build
EXPOSE 80
CMD node ./server.js
