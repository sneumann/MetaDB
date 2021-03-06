
package it.fmach.metadb.isatab.model

import it.fmach.metadb.User
import it.fmach.metadb.isatab.importer.IsatabImporter;
import it.fmach.metadb.isatab.importer.IsatabImporterImpl;
import it.fmach.metadb.isatab.model.FEMStudy;
import it.fmach.metadb.isatab.testHelper.TestDbSetup
import grails.test.mixin.*

import org.codehaus.groovy.grails.io.support.ClassPathResource
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@Mock([AccessCode, Instrument])
@TestFor(FEMStudy)
class FEMStudyTests {
	
	static def currentUser = new User(username: 'roman', password: 'namor', workDir: '/home/mylonasr/MetaDB/data/roman')
	static String rootDir = "resources/org/isatools/isacreator/io/importisa/"	
	def configDir = new ClassPathResource(rootDir + "MetaboLightsConfig20130507").getFile().getAbsolutePath()
	def isatabDir = new ClassPathResource(rootDir + "Wine_Storage").getFile().getAbsolutePath()

	
    void testSaveAndLoadStudy() {

		// create instruments
		def creator = new TestDbSetup()
		creator.createInstrument()
		
		def workDir = File.createTempFile("test_workdir", "")
		workDir.delete();
		workDir.mkdir();

		IsatabImporter importer = new IsatabImporterImpl(configDir, workDir.getAbsolutePath(), currentUser)
		def investigation = importer.importIsatabFiles(isatabDir)
		def study = investigation.studyList.get(0)
		
		if (!study.validate()){
			study.errors.allErrors.each {
				println it
			}
		}
		
		study.save(flush: true)		
		def loadedStudy = FEMStudy.findByDescriptionLike("%red wine%")
		
		assert "Metabolic changes during wine storage" == loadedStudy.title
    }
	
}
