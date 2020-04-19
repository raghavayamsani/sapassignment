FROM centos:latest
RUN yum install -y java
WORKDIR /local/mnt/projects/assignment
RUN mkdir -p /local/mnt/projects/assignment
COPY target/assignment.jar /local/mnt/projects/assignment/
CMD java -Dserver.port=8080 -jar /local/mnt/projects/assignment/assignment.jar