# Build stage
FROM node:22-alpine as builder
WORKDIR /app
COPY . .
ENV REACT_APP_GATEWAY_URL=http://gateway:8085
RUN npm install
RUN npm run build

# Serve with Nginx
FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
