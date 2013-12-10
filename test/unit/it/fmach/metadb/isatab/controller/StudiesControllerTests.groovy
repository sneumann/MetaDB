package it.fmach.metadb.isatab.controller

import java.util.Date;
import java.util.List;

import grails.test.mixin.*
import it.fmach.metadb.isatab.model.FEMAssay
import it.fmach.metadb.isatab.model.FEMStudy
import it.fmach.metadb.isatab.model.FEMRun
import it.fmach.metadb.isatab.model.InstrumentMethod
import it.fmach.metadb.isatab.model.Instrument
import it.fmach.metadb.isatab.model.AccessCode
import it.fmach.metadb.isatab.testHelper.TestDomainCreator

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@Mock([FEMStudy, FEMAssay, FEMRun, InstrumentMethod, Instrument, AccessCode])
@TestFor(StudiesController)
class StudiesControllerTests {

    void testIndex() {
		
		// create a test study
		def creator = new TestDomainCreator()
		def study = creator.createStudy()
       	study.save(flush: true)
		   
		controller.index()
		assert 1 == flash.studies.size
		assert "study_id" == flash.studies.get(0).identifier
    }
}