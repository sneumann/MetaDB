package it.fmach.metadb.isatab.model

class FEMProject {

	String name
	String description
	
//	static belongsTo = [fEMGroup: FEMGroup]
	
    static constraints = {
		description nullable: true
		name unique: true
    }
}