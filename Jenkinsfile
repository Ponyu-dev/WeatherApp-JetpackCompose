pipeline {
    agent {
        docker {
            image 'ponyu/weather-otus-image:1.0.0' // Ваш Docker-образ
            args '-u root'
        }
    }

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Имя ветки для сборки')
    }

    environment {
        // Креденшиалы для подписи APK
        STORE_FILE = credentials('otus_keystore') // credentialsId для keystore.jks
        STORE_PASSWORD = credentials('storePassword') // Пароль к хранилищу
        KEY_ALIAS = credentials('keyAlias') // Алиас ключа
        KEY_PASSWORD = credentials('keyPassword') // Пароль ключа
    }

    stages {

        stage('Detect') {
            steps {
                script {
                    if (params.BRANCH_NAME ==~ /^release\/.*/) {
                        currentBuild.description = "Release build"
                    } else {
                        currentBuild.description = "Debug build"
                    }
                }
            }
        }

        stage('Branch') {
            steps {
                script {
                    if (params.BRANCH_NAME ==~ /^release\/.*/) {
                        echo "Building release version"
                        buildRelease()
                    } else {
                        echo "Building debug version"
                        buildDebug()
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/build/reports/**/*', allowEmptyArchive: true
        }
    }
}

def buildRelease() {
    stage('Assemble Release') {
        steps {
            sh 'echo "./gradlew assembleRelease"' // Сборка Release версии
        }

        stage('Unit and Integration Tests') {
            steps {
                sh 'echo "./gradlew testReleaseUnitTest connectedReleaseAndroidTest"'
            }

            script {
                if (currentBuild.result == 'SUCCESS') {
                    stage('Deploy to Maven Local') {
                        sh 'echo "./gradlew publishToMavenLocal"'
                    }
                } else {
                    archiveArtifacts artifacts: '**/build/reports/tests/**/*', allowEmptyArchive: true
                }
            }
        }
    }
}

def buildDebug() {
    stage('Assemble Debug') {
        steps {
            sh 'echo "./gradlew assembleDebug"' // Сборка Debug версии
        }

        stage('Unit Tests') {
            steps {
                sh 'echo "./gradlew testDebugUnitTest"'
            }

            script {
                if (currentBuild.result == 'SUCCESS') {
                    stage('Deploy to Maven Local') {
                        sh 'echo "./gradlew publishToMavenLocal"'
                    }
                } else {
                    archiveArtifacts artifacts: '**/build/reports/tests/**/*', allowEmptyArchive: true
                }
            }
        }
    }
}
