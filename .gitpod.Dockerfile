FROM gitpod/workspace-full

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh \
             && sdk install java 8.0.265-open && cd server && ./ngrok authtoken 1n6QgKIQuUMr7doqfXPmpYE6dDr_22RYRZv8Z95fNg93TQn89"