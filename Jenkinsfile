/*
pipeline {
         agent any
         stages {
                 stage('2') {
                 steps {
                     echo 'Hi, itisgood. Starting to build the App.'
                 }
                 }
                 stage('Test') {
                 steps {
                    input('Do you want to proceed?')
                 }
                 }
                 stage('Deploy') {
                 parallel {
                            stage('Deploy start ') {
                           steps {
                                echo "Start the deploy .."
                           }
                           }
                            stage('Deploying now') {
                            agent {
                                    docker {
                                            reuseNode true
                                            image ‘nginx’
                                           }
                                    }

                              steps {
                                echo "Docker Created"
                              }
                           }
                           }
                           }
                 stage('Prod') {
                     steps {
                                echo "App is Prod Ready"
                              }

              }
}
}
*/

job('commit') {
  description('Build and smoke test')

  scm {
    github('martinmosegaard/vigilant-sniffle')
  }

  steps {
    gradle('jar')
  }

  // Artifactory configuration
  configure {
    it / buildWrappers / 'org.jfrog.hudson.gradle.ArtifactoryGradleConfigurator' {
      deployMaven 'true'
      deployIvy 'false'
      deployBuildInfo 'true'
      deployArtifacts 'true'
      allowPromotionOfNonStagedBuilds 'true'
      skipInjectInitScript 'true'

      // Repository to resolve from
      resolverDetails {
        artifactoryName('artifactory')
        artifactoryUrl('http://artifactory:8081/artifactory')
        resolveReleaseRepository {
          keyFromSelect('libs-release')
        }
      }
      // Repository to deploy to.
      details {
        artifactoryName('artifactory')
        artifactoryUrl('http://artifactory:8081/artifactory')
        deployReleaseRepository {
          keyFromSelect('libs-release-local')
        }
      }

      artifactPattern '[organisation]/[module]/[revision]/[artifact]-[revision].$BUILD_NUMBER(-[classifier]).[ext]'
    }
  }

  publishers {
    downstream('test')
  }
}

job('test') {
  description('Acceptance test')

  steps {
    shell('''\
    echo This is the test job.
    '''.stripIndent())
  }

  publishers {
    downstream('release')
  }
}

job('release') {
  description('Release the artifacts')

  steps {
    shell('''\
    echo This is the release job.
    '''.stripIndent())
  }
}

buildPipelineView('Pipeline') {
  title('vigilant-sniffle pipeline')
  displayedBuilds(50)
  selectedJob('commit')
  alwaysAllowManualTrigger()
  showPipelineParametersInHeaders()
  showPipelineParameters()
  showPipelineDefinitionHeader()
  refreshFrequency(60)
}

// Jenkinsfile pipeline:
pipelineJob('pipeline') {
  definition {
    cpsScm {
      scm {
        github('martinmosegaard/vigilant-sniffle')
      }
      scriptPath('Jenkinsfile')
    }
  }
}