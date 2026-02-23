# ===== Build Angular =====
FROM node:20 AS build
WORKDIR /app

# Copy Angular project
COPY frontend/expense-frontend/ .

# Install deps & build
RUN npm install && npm run build

# ===== Serve with Nginx =====
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80