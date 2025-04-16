# Build stage
FROM node:22-alpine as builder
WORKDIR /app
COPY . .
ENV REACT_APP_GATEWAY_URL=/api
RUN npm install
RUN npm run build

# Serve with NGINX
FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html

# Add custom nginx.conf for proxying /api to gateway
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
