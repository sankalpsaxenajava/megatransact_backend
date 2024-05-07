FROM ubuntu:latest
LABEL authors="romul"

ENTRYPOINT ["top", "-b"]