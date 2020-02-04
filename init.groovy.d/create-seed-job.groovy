// This script is run as part of initializing Jenkins.
// It creates a seed job and copies the required DSL script to its workspace
import jenkins.model.*

import hudson.plugins.git.*;
import javaposse.jobdsl.plugin.*;

def scm = new GitSCM("https://github.com/Michael-yud/init_groovy")
scm.branches = [new BranchSpec("*/master")];

def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "seed-job-config.xml")

def parent = Jenkins.instance
def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "New Job")
job.definition = flowDefinition

parent.reload()

def jobName = "my-new-job"
def configXml = "seed-job-config.xml" // your xml goes here

def xmlStream = new ByteArrayInputStream( configXml.getBytes() )

Jenkins.instance.createProjectFromXML(jobName, xmlStream)

/*
println "Creating seed job"
new File("/usr/share/jenkins/seed-job-config.xml").withInputStream { stream ->
  Jenkins.instance.createProjectFromXML("seed", stream)
}
*/
def commands = [
    "mkdir -p /var/jenkins_home/workspace/seed",
    "cp -v /usr/share/jenkins/seeddsl.groovy /var/jenkins_home/workspace/seed/"
  ]
commands.each {
  println "Executing command ${it}"
  def process = it.execute()
  process.waitFor()

  def output = process.in.text
  println output
}
