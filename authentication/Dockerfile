# Builder
FROM golang:1.22.4-alpine AS builder

WORKDIR /app

COPY go.mod go.sum ./

RUN go mod download

COPY . .

RUN go build -o main src/cmd/server.go

# Runner
FROM alpine:latest

WORKDIR /app
COPY --from=builder /app/main .
COPY --from=builder /app/config ./config
ENV APP_CONF=/app/config/prod.yml

EXPOSE 8080
EXPOSE 50050

CMD ["./main"]
