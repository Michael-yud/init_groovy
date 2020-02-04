import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import java.util.logging.Logger
import hudson.model.*
import jenkins.model.*;

env = System.getenv()
Logger logger = Logger.getLogger('seed.groovy')

// need to disable security during seed setup
Jenkins.getInstance().disableSecurity()

WORKSPACE_BASE = "${env['JENKINS_HOME']}/workspace"
SEED_JOB_NAME="seed"
def workspace = new File("${WORKSPACE_BASE}")
def seedJobDsl = """

def job_name = '${SEED_JOB_NAME}'
    job(job_name) {
        authorization {
          permission('hudson.model.Item.Build', 'authenticated')
          permission('hudson.model.Item.Read', 'authenticated')
        }

        description('This job is automagically generated.')
        disabled(false)
        blockOnUpstreamProjects()
        logRotator(daysToKeep = 14, numToKeep = 9)
        label("master")

        configure { project ->
            project.remove(project / scm) // remove the existing 'scm' element to fix duplicate entry error
            project / scm(class: 'hudson.plugins.filesystem_scm.FSSCM') {
                path("/usr/share/jenkins/seeds")
            }
        }

        steps {
          dsl {
              additionalClasspath("lib/*.jar")
              external("process.groovy")
              ignoreExisting(false)
              removeAction('DELETE')
              removeViewAction('DELETE')
          }
        }
    }
"""

logger.info(seedJobDsl)

def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
new DslScriptLoader(jobManagement).runScript(seedJobDsl)
logger.info('Created first job')

// run seed now
def job = Hudson.instance.getItem(SEED_JOB_NAME)

// start job
Hudson.instance.queue.schedule(job)
