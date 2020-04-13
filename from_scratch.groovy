node  {

properties([
	// Below line sets "Discard Builds more than 5 "
	buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), 
	disableConcurrentBuilds(),
	
	// Below line triggers this job every minute 
	 pipelineTriggers([pollSCM('* * * * * ')]),
	 parameters([
		 // Asks for environment to build 
		 choice(choices: [
		 'dev1.ayyildizrug.com', 
		 'qa1.ayyildizrug.com', 
		 'stage1.ayyildizrug.com', 
		 'prod1.ayyildizrug.com'], 
		 description: 'Please choose an environment',
		  name: 'ENVIR'), 

         // Asks for version
		  choice(choices: [
			  'v0.1',
		      'v0.2',
			  'v0.3',
			  'v0.4',
			  'v0.5'
			  ], 
		description: 'Which version should we deploy?', 
		name: 'Version'),


		// Asks for an input
			string(defaultValue: 'v1', 
			description: 'Please enter version number', 
			name: 'APP_VERSION', 
			trim: true)
	    ])
	 ])



	stage("Pull Repo"){
		checkout([$class: 'GitSCM', branches: [[name: '*/FarrukH']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/farrukh90/cool_website.git']]])
    }
   stage("Install Prerequisites"){
		sh """
		ssh centos@${ENVIR}                 sudo yum install httpd -y
	
		"""
    }

	stage("Copy Artifacts"){
		sh """
		scp -r *  centos@${ENVIR}:/tmp
		ssh centos@${ENVIR}                 sudo cp -r /tmp/index.html /var/www/html/
		ssh centos@${ENVIR}                 sudo cp -r /tmp/style.css /var/www/html/
		ssh centos@${ENVIR}				   sudo chown centos:centos /var/www/html/
		ssh centos@${ENVIR}				   sudo chmod 777 /var/www/html/*
		"""
	}
	stage("Restart  web server"){
		ws ("mnt/"){
		    sh "ssh centos@${ENVIR}                sudo systemctl restart httpd "
		}
    }

}









