package it.fmach.metadb.isatab.controller

import it.fmach.metadb.isatab.model.Instrument
import it.fmach.metadb.isatab.model.InstrumentMethod
import it.fmach.metadb.isatab.model.MetaMsDb

class InstrumentController {

	def instrumentService
	
	def index() {
		// list all instruments
		flash.instrumentList = Instrument.list()
	}

	def newInstrument() {

	}

	def saveNewInstrument(){
		def newInstr = new Instrument(name: params.name, metabolightsName: params.metabolightsName, 
						bookingSystemName: params.bookingSystemName, chromatography: params.chromatography)

		// check validity and save if ok
		if(newInstr.validate()){
			newInstr.save(flush: true)
			flash.message = "New instrument was created"
		}else{
			flash.error = newInstr.errors.allErrors.join(" and ")
		}

		redirect(action: 'index')
	}
	
	def updateInstrument(){
		def instr = Instrument.get(session.instrument.id)
		instr.name = params.name
		instr.metabolightsName = params.metabolightsName
		instr.bookingSystemName = params.bookingSystemName
		instr.chromatography = params.chromatography

		// check validity and save if ok
		if(instr.validate()){
			instr.save(flush: true)
			flash.message = "instrument was updated"
		}else{
			flash.error = instr.errors.allErrors.join(" and ")
		}
		
		session.instrument = instr

		redirect(action: 'index')
	}

	def detail(){
		// load this instrument if params.id is provided
		if(params.id) session.instrument = Instrument.get(params.id)

		if(! session.instrument) throw new RuntimeException("missing params.id")
	}
	
	def methodDetail(){
		// load this method if params.id is provided
		if(params.id) flash.method = InstrumentMethod.get(params.id)
			
		if(! flash.method) throw new RuntimeException("missing params.id")
		
		// and get all available MetaMsDBs
		flash.metaMsDb = MetaMsDb.list()
	}
	
	def newMethod(){		
		// and get all available MetaMsDBs
		flash.metaMsDb = MetaMsDb.list()
	}
	
	def saveNewMethod(){
		// create boolean parameter for randomization
		def randomization = (params.randomization == "on") ? (true) : (false)
		
		// create the new method		
		def newMethod = new InstrumentMethod(name: params.name, 
								description: params.description,
								tag: params.tag,
								startPattern: params.startPattern,
								repeatPattern: params.repeatPattern,
								endPattern: params.endPattern,
								metaMsParameterFile: params.metaMsParameterFile,
								randomization: randomization)
		
		// the instrument to add to
		def instrument = Instrument.get(session.instrument.id)
		
		// save if valid
		try{
			instrument.addToMethods(newMethod).save(flush: true, failOnError: true)
		}catch(Exception e){
			flash.error = e.message
			redirect(action: 'detail')
			return
		}

		flash.message = "New method was created"
		
		// set current instrument
		session.instrument = instrument
		redirect(action: 'detail')
	}
	
	
	def updateMethod(){
		// create boolean parameter for randomization
		def randomization = (params.randomization == "on") ? (true) : (false)
		
		// update the method
		def method = InstrumentMethod.get(flash.method.id)
		method.name = params.name
		method.description = params.description
		method.tag = params.tag
		method.startPattern = params.startPattern
		method.repeatPattern = params.repeatPattern
		method.endPattern = params.endPattern
		method.metaMsParameterFile = params.metaMsParameterFile
		method.randomization = randomization

		// check validity and save if ok
		if(method.validate()){
			method.save(flush: true)
			flash.message = "method was updated"
		}else{
			flash.error = method.errors.allErrors.join(" and ")
		}

		redirect(action: 'detail')
	}

	
	
	def delete(){
		// delete selected instrument
		def instrument = Instrument.get(params.id)
		
		try{
			instrumentService.delete(instrument)
		}catch(Exception e){
			e.printStackTrace()
			flash.error = e.message
			redirect(action: 'index')
			return
		}
		
		flash.message = "Instrument was deleted"
		redirect(action: 'index')
	}

}