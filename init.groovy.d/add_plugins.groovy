/* https://github.com/coreos/jenkins-os/blob/master/init.groovy
 * Create all OS projects on a new Jenkins server.
 *
 * This entire script can be pasted directly into the text box found at
 * ${JENKINS_URL}/script to populate the server with OS jobs.  It will
 * define everything based on the contents of this repository.
 *
 * If any required plugins are not installed when this script is run,
 * they will be downloaded and installed automatically, and Jenkins will
 * be restarted to enable them.  In this case, this script must be run
 * again after the restart to create the jobs.
 *
 * Note that settings such as user permissions and secret credentials
 * are not handled by this script.
 */

/* Install required plugins and restart Jenkins, if necessary.  */

import jenkins.*
import hudson.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*;
import hudson.model.*
import jenkins.model.*
import hudson.security.*

final List<String> REQUIRED_PLUGINS = [
    "authorize-project",
//    "locale",
    "job-dsl",
    "kubernetes",
    "workflow-job",
    "workflow-aggregator",
    "workflow-durable-task-step",
    "workflow-multibranch",
    "ws-cleanup",
    "ansicolor",
    "credentials-binding",
    "git",
    "github",
    "strict-crumb-issuer",
    "docker",
    "docker-build-step",
    "nodelabelparameter",
    "view-job-filters",
    "seed",
    "google-container-registry-auth",
    "google-storage-plugin",
    "google-oauth-plugin",
    "hashicorp-vault-plugin",
    "hashicorp-vault-pipeline",
    "github-organization-folder",
    "groovy",
    "gradle",
    "gradle-repo",
    "pipeline-maven",
    "pipeline-input-step",
    "artifactory",
    "folder-properties",
    "Convert-To-Pipeline",
    "Scriptler",
//    "",
//    "",
]

if (Jenkins.instance.pluginManager.plugins.collect {
        it.shortName
    }.intersect(REQUIRED_PLUGINS).size() != REQUIRED_PLUGINS.size()) {
    REQUIRED_PLUGINS.collect {
        Jenkins.instance.updateCenter.getPlugin(it).deploy()
    }.each {
        it.get()
    }
    Jenkins.instance.restart()
    println 'Run this script again after restarting to create the jobs!'
    throw new RestartRequiredException(null)
}

println "Plugins were installed successfully"



