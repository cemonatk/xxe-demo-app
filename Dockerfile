FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .

COPY src ./src
RUN mvn -f pom.xml clean package || cat /root/.m2/repository/org/springframework/boot/spring-boot-starter-parent/2.7.0/spring-boot-starter-parent-2.7.0.pom

FROM openjdk:11-jre
WORKDIR /app

RUN useradd -m admin -s /bin/bash && \
    mkdir -p /home/admin/.ssh && \
    chown admin:admin /home/admin/.ssh && \
    chmod 700 /home/admin/.ssh

RUN echo "-----BEGIN RSA PRIVATE KEY----- \
MIIEowIBAAKCAQEAx0j/mkrMVKJUysF5J5OyYo6gq/qGJH1LEfJfZANUJHufITDX\
cYRQVoaRHYxTf3QcybvEYpWrBqLOA5yLjfJ9++y3uB44QfweeukO56nq5pQyQ0qE\
UlXnDS3Bqy7uRWE5ZgQLqHxvDK3dyTOwj0Ai9wJFHTRNYJ9q2UMFqH6RCR2BDKyg\
1C6cYCGqu8TnY6O5a/5HzfEiCeu8cukC1TkuFqv5Gix5DmX5LbUuBHQd6X6QwX6x\
xR97EbKFsoYKjZTdXp5oTVu4fjy2C0/5nAl6grSkJFi3RDU35a6M5FcN5L3vlH4a\
B5ksIs0+uwxFNTPwA8bPjvjUzTUTBuxrE9uXWp1T17eZGRRx7XB44V5gXDQkZbIR\
DbbYgPVPW1EsbcpLPygTwKcMx2u1tHQ0zWiiZzGKokGDz5C1lv9oCGBIU3hA7e4y\
GOWiFjJnaHbvgHt0CgYEA0+QDd5u8bqwk8WLZaLggBnI6mk/PyCkLjKl6hqBYnZi\
CJzRaQolH5X+gSeCC8uekAlvhFVBCTl2/3HDiTHjVdZVRB3YWpFbPk2RaCgfY+pB\
f1M034Pl3ZAS2T7CoCJhyJlVvqtJYkQxJqgPHbJhC1X3bPHy8CETleSh4ECvpMU=\
-----END RSA PRIVATE KEY-----" > /home/admin/.ssh/id_rsa

RUN chown admin:admin /home/admin/.ssh/id_rsa && chmod 600 /home/admin/.ssh/id_rsa

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /demo.jar

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/demo.jar"]