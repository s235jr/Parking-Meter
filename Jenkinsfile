pipeline {
  agent any
  stages {
    stage('first') {
      steps {
        echo 'first msg'
      }
    }
    stage('checkout') {
      steps {
        powershell 'mvn test'
      }
    }
  }
}