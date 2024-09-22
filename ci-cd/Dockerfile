FROM antonkazakov/otus

# Установите необходимые пакеты
RUN apt-get update && apt-get install -y wget unzip

# Установите Android SDK
RUN mkdir -p /sdk && \
    cd /sdk && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-7302050_latest.zip && \
    unzip commandlinetools-linux-7302050_latest.zip && \
    rm commandlinetools-linux-7302050_latest.zip && \
    yes | /sdk/cmdline-tools/bin/sdkmanager --sdk_root=/sdk --licenses && \
    /sdk/cmdline-tools/bin/sdkmanager --sdk_root=/sdk "platform-tools" "platforms;android-30"

# Установите Gradle Profiler
RUN wget https://github.com/gradle/gradle-profiler/archive/refs/tags/v0.20.0.zip && \
    unzip v0.20.0.zip -d /opt/ && \
    rm v0.20.0.zip && \
    mv /opt/gradle-profiler-0.20.0 /opt/gradle-profiler && \
    echo 'export PATH=$PATH:/opt/gradle-profiler/bin' >> ~/.bashrc


# Добавьте sdkmanager и gradle-profiler в PATH
ENV ANDROID_SDK_ROOT=/sdk
ENV PATH="${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools:/opt/gradle-profiler-0.18.0:${PATH}"

# Переключитесь на пользователя jenkins
USER jenkins
