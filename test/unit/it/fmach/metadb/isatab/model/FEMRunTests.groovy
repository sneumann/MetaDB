package it.fmach.metadb.isatab.model



import grails.test.mixin.*

import org.codehaus.groovy.grails.io.support.ClassPathResource
import org.junit.*

import it.fmach.metadb.User
import it.fmach.metadb.isatab.importer.IsatabImporter
import it.fmach.metadb.isatab.importer.IsatabImporterImpl
import it.fmach.metadb.isatab.testHelper.TestDbSetup
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@Mock([AccessCode, Instrument, FEMSample, FEMAssay])
@TestFor(FEMRun)
class FEMRunTests {

	static def currentUser = new User(username: 'roman', password: 'namor', workDir: '/home/mylonasr/MetaDB/data/roman')
	static String rootDir = "resources/org/isatools/isacreator/io/importisa/"
	
    void testSaveAndLoadRun() {
		
		def configDir = new ClassPathResource(rootDir + "MetaboLightsConfig20130507").getFile().getAbsolutePath()
		def isatabDir = new ClassPathResource(rootDir + "Wine_Storage").getFile().getAbsolutePath()
		
		// create instruments
		def creator = new TestDbSetup()
		creator.createInstrument()
		
		def workDir = File.createTempFile("test_workdir", "")
		workDir.delete();
		workDir.mkdir();

		IsatabImporter importer = new IsatabImporterImpl(configDir, workDir.getAbsolutePath(), currentUser)
		def investigation = importer.importIsatabFiles(isatabDir)
		
		def assay = investigation.studyList.get(0)assays.get(0)
		def run = assay.runs.get(0)
		assay.save(flush: true, failOnError: true)
		
		if (!run.validate()){
			run.errors.allErrors.each {
				println it
			}
		}
		
		// assay.save(flush: true)
		run.save(flush: true, failOnError: true)
		
//		def sample = FEMSample.findByName("0001_R")
//		println("here: "+sample)
//		def loadedRun = FEMRun.findBySample(sample)
		
		def loadedRun = FEMRun.list().get(0)
		String polarity = loadedRun.scanPolarity
		assert polarity == "alternating"
    }
}
