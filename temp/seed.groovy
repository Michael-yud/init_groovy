def git_cred_id = "jenkins-github-cred"
listView('jobs1') {
    description('All jobs for project 1')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex('jobs1.*')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

pipelineJob("jobs1-build") {
    definition {
        cpsScm {
            scriptPath 'Jenkinsfile'
            scm {
                git {
                    remote {
                        github("https://github.com/Michael-yud/init_groovy")
                        credentials(git_cred_id)
                    }
                    branch '*/master'
                    extensions {}
                }
            }
            triggers {
                githubPush()
            }
        }
    }
}
