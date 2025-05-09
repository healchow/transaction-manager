# Use the ARM64 image based on Debian platform
FROM --platform=linux/arm64/v8 openjdk:21-jdk-bookworm

# install tools
RUN apt-get update \
    && apt-get install -y net-tools vim \
    && rm -rf /var/lib/apt/lists/* \

EXPOSE 8080
# profile and env virables
ENV ACTIVE_PROFILE=dev

# JVM heap size
ENV JVM_HEAP_OPTS="-Xms1024m -Xmx1024m"
WORKDIR /opt/healchow/transaction

# add detail tarball
ARG VERSION=1.0.0
ADD target/transaction-detail-web-${VERSION}-bin.tar.gz /opt/healchow/transaction

# add start script
ADD transaction-detail-docker.sh bin/
RUN chmod +x bin/transaction-detail-docker.sh
CMD ["bin/transaction-detail-docker.sh"]
