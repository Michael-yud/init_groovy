import jenkins.model.Jenkins
import jenkins.plugins.git.GitSCMSource
import jenkins.plugins.git.traits.BranchDiscoveryTrait
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever

List libraries = [] as ArrayList

def instance = Jenkins.getInstance()

println("--- Configuring global library getting")
def githubRepo      = 'https://github.com/ealebed/jenkins-shared-libs.git'
def libraryName     = 'jenkins-shared-libs'
def githubBranch    = 'master'
def credentialsId   = ''

def scm             = new GitSCMSource(githubRepo)
scm.credentialsId   = credentialsId
scm.traits          = [new BranchDiscoveryTrait()]
def retriever       = new SCMSourceRetriever(scm)

def library         = new LibraryConfiguration(libraryName, retriever)
library.defaultVersion          = githubBranch
library.implicit                = true
library.allowVersionOverride    = true
library.includeInChangesets     = true

libraries << library

def global_settings = instance.getExtensionList(GlobalLibraries.class)[0]
global_settings.libraries = libraries
global_settings.save()

