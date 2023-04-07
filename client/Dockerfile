FROM node:14-alpine 
WORKDIR /app
COPY . .
RUN npm ci 
RUN npm run build
CMD [ "npx", "serve", "build" ]