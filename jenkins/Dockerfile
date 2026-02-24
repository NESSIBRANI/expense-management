FROM jenkins/jenkins:lts

USER root

# Base tools
RUN apt-get update && apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    && rm -rf /var/lib/apt/lists/*

# Install Docker CLI ONLY (official script, no daemon)
RUN curl -fsSL https://get.docker.com | sh -s -- --cli

# Install Docker Compose v2 plugin
RUN mkdir -p /usr/local/lib/docker/cli-plugins && \
    curl -fsSL https://github.com/docker/compose/releases/download/v2.26.1/docker-compose-linux-x86_64 \
    -o /usr/local/lib/docker/cli-plugins/docker-compose && \
    chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

USER jenkins