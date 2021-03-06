package it.fmach.metadb.export

import it.fmach.metadb.export.ExportCsv;
import it.fmach.metadb.isatab.testHelper.TestDomainCreator
import it.fmach.metadb.isatab.model.FEMAssay
import it.fmach.metadb.isatab.model.FEMStudy
import it.fmach.metadb.isatab.model.FEMRun
import it.fmach.metadb.isatab.model.InstrumentMethod
import it.fmach.metadb.isatab.model.Instrument
import it.fmach.metadb.isatab.model.AccessCode
import grails.test.mixin.*

import org.junit.*

@Mock([FEMStudy, FEMAssay, FEMRun, InstrumentMethod, Instrument, AccessCode])
class ExportCsvTest {

	@Test
	public void testExportRandomizedRuns() {
		
		def creator = new TestDomainCreator()
		def assay = creator.createRandomizedRuns()
		
		def exporter = new ExportCsv()
		def csvString = exporter.exportRandomizedRuns(assay)
		
		def lines = csvString.split("\n")
		assert 13 == lines.size()
		
		def entries = lines.getAt(1).split(",")
		assert 6 == entries.size()
		assert "Sample_1_001" == entries[2]
	}
	
	@Test
	public void testExportRandomizedRunsWithQCFirst() {
		
		def creator = new TestDomainCreator()
		def assay = creator.createRandomizedRunsQCFirst()
		
		def exporter = new ExportCsv()
		def csvString = exporter.exportRandomizedRuns(assay)
		
		def lines = csvString.split("\n")
		assert 13 == lines.size()
		
		def entries = lines.getAt(1).split(",")
		assert 2 == entries.size()
		
		assert "QC" == entries[1]
	}

}
