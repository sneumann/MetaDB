package it.fmach.metadb.isatab.controller

import it.fmach.metadb.instrument.export.ExportCsv
import it.fmach.metadb.isatab.model.MetaMsSubmission
import it.fmach.metadb.workflow.metams.MetaMsRunner
import it.fmach.metadb.workflow.metams.MetaMsZipExporter

class MetaMSController {
	
	def metaMsZipExporter = new MetaMsZipExporter()

    def index() {
		
		def assay = session.assay
		if(assay == null){
			flash.error = "No assay is selected"
			return
		}
		
		// re-attach the assay object to the session
		assay.refresh()
		
		session.metaMsSubmissions = assay.metaMsSubmissions
		
	}
	
	
	def downloadZip(){
		def submissionId = params.id
		// load the workDir from the submission
		def submission = MetaMsSubmission.get(submissionId)
		println(submission.workDir)
		
		// create a zip-file from the current workDir
		String tmpFilePath = metaMsZipExporter.createTempZip(submission.workDir)
		println(tmpFilePath)
		File file = new File(tmpFilePath)
		
		// hand zip file to the browser
/*		response.setHeader "Content-disposition", "attachment; filename=${assay.shortName}.csv"
		response.contentType = 'text/zip'
		response.outputStream << csvString
		response.outputStream.flush()*/
		
		if (file.exists()) {
			def os = response.outputStream
			response.setHeader("Content-Type", "application/zip")
			response.setHeader("Content-disposition", "attachment;filename=${file.name}")
	
			def bytes = file.text.bytes
			for(b in bytes) {
			   os.write(b)
			}
	
			os.flush()
		 }
		
	}
	
	
	def metaMsSubmission() {
		
		def runSelection = session.runsSelection
		
		// in case we were not redericted from runMetaMS
		if(! session.runsSelection){
			runSelection = params.list('runSelection')
		}
		
		session.runSelection = runSelection
		flash.message = runSelection.size().toString() + " runs were selected"
		
	}
	
	
	def runMetaMS() {
		
		def minRt = params['minRt']
		def maxRt = params['maxRt']
		def runSelection = flash.runSelection		
		
		// check if RT's are valid
		if(minRt || maxRt){
			if(! (maxRt.isDouble() && minRt.isDouble())){
				flash.error = "invalid retention times [" + minRt + "] and/or [" + maxRt + "]"
				session.runsSelection = runSelection
				redirect(action: 'metaMsSubmission')
				return
			}
		}
	
		def metaMsDir = grailsApplication.config.metadb.conf.metams.script
		def metaMsDbDir = grailsApplication.config.metadb.conf.metams.databases
		def metaMsSettingsDir = grailsApplication.config.metadb.conf.metams.instrumentSettings
		
		def runner = new MetaMsRunner(metaMsDir, metaMsDbDir, metaMsSettingsDir)
		
		def assay = session.assay
		if(assay == null){
			flash.error = "No assay is selected"
		}
		
		// re-attach the assay object to the session
		assay.attach()
		
		// start runner
		runner.runMetaMs(assay, runSelection, minRt, maxRt)
		
		flash.message = "started runMetaMS"
		
		redirect(action: 'index')
	}
	
}
