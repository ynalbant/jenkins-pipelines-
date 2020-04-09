  
node {
	stage("Stage1"){
		echo "hello"
}
	stage("Stage2"){
		echo "hello"
        // Calls another job called "Timestamp"
        build 'timestamp'
}
	stage("Stage3"){
		echo "hello"
        // Calls another job called "Script"
        build "script"
}
	stage("Stage4"){
		build 'Template2'
        // Calls another job called "Script"
}
	stage("Stage5"){
		echo "hello"
	}
    
}