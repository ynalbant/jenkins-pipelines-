node {

properties([
	// Below line sets "Discard Builds more than 5 "
	buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), 
	
	// Below line triggers this job every minute 
	 pipelineTriggers([pollSCM('* * * * * ')])
	 ])



	stage("Pull Repo"){
		git  'https://github.com/farrukh90/cool_website.git'
    }
   stage("Install Prerequisites"){
		sh """
		ssh centos@jenkins_worker1.ayyildizrug.com                 sudo yum install httpd -y
	
		"""
    }

	stage("Copy Artifacts"){
		sh """
		scp -r *  centos@jenkins_worker1.ayyildizrug.com:/tmp
		ssh centos@jenkins_worker1.ayyildizrug.com                 sudo cp -r /tmp/index.html /var/www/html/
		ssh centos@jenkins_worker1.ayyildizrug.com                 sudo cp -r /tmp/style.css /var/www/html/
		ssh centos@jenkins_worker1.ayyildizrug.com				   sudo chown centos:centos /var/www/html/
		ssh centos@jenkins_worker1.ayyildizrug.com				   sudo chmod 777 /var/www/html/*
		"""
	}
	stage("Restart  web server"){
		sh "ssh centos@jenkins_worker1.ayyildizrug.com                 sudo systemctl restart httpd "
    }
	stage("Slack"){
		slackSend color: '#BADA55', message: 'Hello, World!'
	}
}
