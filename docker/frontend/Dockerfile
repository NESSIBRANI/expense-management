# Build Angular
FROM node:20 AS build
WORKDIR /app

COPY frontend/expense-frontend/ .

RUN npm install && npm run build

# Serve with nginx
FROM nginx:alpine
COPY --from=build /app/dist/expense-frontend/browser/ /usr/share/nginx/html/
EXPOSE 80