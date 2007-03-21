package org.openmrs.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.PatientSetDAO;
import org.openmrs.reporting.PatientAnalysis;
import org.openmrs.reporting.PatientSet;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PatientSetService {

	public void setPatientSetDAO(PatientSetDAO dao);

	/**
	 * @param ps The set you want to export as XML
	 * @return an XML representation of this patient-set, including patient characteristics, and observations
	 */
	@Transactional(readOnly=true)
	public String exportXml(PatientSet ps);

	@Transactional(readOnly=true)
	public String exportXml(Integer patientId);

	@Transactional(readOnly=true)
	public PatientSet getAllPatients() throws DAOException;

	@Transactional(readOnly=true)
	public PatientSet getPatientsByCharacteristics(String gender,
			Date minBirthdate, Date maxBirthdate) throws DAOException;

	@Transactional(readOnly=true)
	public PatientSet getPatientsByCharacteristics(String gender,
			Date minBirthdate, Date maxBirthdate, Integer minAge,
			Integer maxAge, Boolean aliveOnly, Boolean deadOnly)
			throws DAOException;

	@Transactional(readOnly=true)
	public PatientSet getPatientsHavingNumericObs(Integer conceptId,
			TimeModifier timeModifier, PatientSetService.Modifier modifier,
			Number value, Date fromDate, Date toDate);
	
	@Transactional(readOnly=true)
	public PatientSet getPatientsHavingObs(Integer conceptId,
			TimeModifier timeModifier, Modifier modifier, Object value,
			Date fromDate, Date toDate);

	@Transactional(readOnly=true)
	public PatientSet getPatientsInProgram(Program program, Date fromDate, Date toDate);
	
	@Transactional(readOnly=true)
	public PatientSet getPatientsHavingDateObs(Integer conceptId, Date startTime, Date endTime);
	
	@Transactional(readOnly=true)
	public PatientSet getPatientsHavingTextObs(Concept concept, String value, TimeModifier timeModifier);

	@Transactional(readOnly=true)
	public PatientSet getPatientsHavingTextObs(Integer conceptId, String value, TimeModifier timeModifier);

	@Transactional(readOnly=true)
	public PatientSet getPatientsHavingLocation(Location loc);

	@Transactional(readOnly=true)
	public PatientSet getPatientsHavingLocation(Integer locationId);

	/**
	 * Returns a PatientSet of patient who had drug orders for a set of drugs active on a certain date.
	 * Can also be used to find patient with no drug orders on that date.
	 * @param patientIds Collection of patientIds you're interested in. NULL means all patients.
	 * @param takingAny Collection of drugIds the patient is taking. (Or the empty set to mean "any drug" or NULL to mean "no drugs")
	 * @param onDate Which date to look at the patients' drug orders. (NULL defaults to now().)
	 */
	@Transactional(readOnly=true)
	public PatientSet getPatientsHavingDrugOrder(
			Collection<Integer> patientIds, Collection<Integer> takingIds,
			Date onDate);

	@Transactional(readOnly=true)
	public Map<Integer, String> getShortPatientDescriptions(
			Collection<Integer> patientIds);

	@Transactional(readOnly=true)
	public Map<Integer, List<Obs>> getObservations(PatientSet patients,
			Concept concept);

	/**
	 * Date range is inclusive of both endpoints 
	 */
	@Transactional(readOnly=true)
	public Map<Integer, List<Obs>> getObservations(PatientSet patients,
			Concept concept, Date fromDate, Date toDate);

	/**
	 * 
	 * Map<patientId, List<Obs values>>
	 * 
	 * @param patients
	 * @param c
	 * @return Map<patientId, List<Obs values>>
	 */
	@Transactional(readOnly=true)
	public Map<Integer, List<List<Object>>> getObservationsValues(PatientSet patients, Concept c);
	
	/**
	 * Returns a mapping from patient id to obs for concept <code>c</code>
	 * 
	 * The returned List<attribute value> is [obs value, attr value, attr value, attr value...]
	 * The returned List<List<attribute value>> represents the obs rows 
	 * 
	 * @param patients
	 * @param c
	 * @param attributes list of attributes
	 * @return <code>Map<patientId, List<List<attribute value>>></code>
	 */
	@Transactional(readOnly=true)
	public Map<Integer, List<List<Object>>> getObservationsValues(PatientSet patients, Concept c, List<String> attributes);
	
	/**
	 * 
	 * @param patients
	 * @param encType
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<Integer, Encounter> getEncountersByType(PatientSet patients, EncounterType encType);

	/**
	 * 
	 * @param patients
	 * @param encType
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<Integer, Encounter> getEncountersByType(PatientSet patients, List<EncounterType> encType);

	/**
	 * 
	 * @param patients
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<Integer, Encounter> getEncounters(PatientSet patients);
	
	/**
	 * 
	 * @param patients
	 * @param encType
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<Integer, Encounter> getFirstEncountersByType(
			PatientSet patients, EncounterType encType);
	
	/**
	 * 
	 * @param patients
	 * @param types
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<Integer, Encounter> getFirstEncountersByType(PatientSet patients, List<EncounterType> types);
	
	/**
	 * 
	 * @param patients
	 * @param className
	 * @param property
	 * @param returnAll
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<Integer, Object> getPatientAttributes(PatientSet patients,
			String className, String property, boolean returnAll);

	/**
	 * 
	 * @param patients
	 * @param classNameDotProperty
	 * @param returnAll
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<Integer, Object> getPatientAttributes(PatientSet patients,
			String classNameDotProperty, boolean returnAll);

	/**
	 * 
	 * @param patients
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<Integer, Map<String, Object>> getCharacteristics(
			PatientSet patients);

	/**
	 * 
	 * @param identifiers
	 * @return
	 */
	@Transactional(readOnly=true)
	public PatientSet convertPatientIdentifier(List<String> identifiers);
	
	/**
	 * 
	 * @param patientIds
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Patient> getPatients(Collection<Integer> patientIds);

	/**
	 * 
	 * @param ps
	 */
	public void setMyPatientSet(PatientSet ps);

	/**
	 * 
	 * @return
	 */
	@Transactional(readOnly=true)
	public PatientSet getMyPatientSet();

	/**
	 * 
	 * @param ptId
	 */
	public void addToMyPatientSet(Integer ptId);

	/**
	 * 
	 * @param ptId
	 */
	public void removeFromMyPatientSet(Integer ptId);

	public void clearMyPatientSet();

	@Transactional(readOnly=true)
	public Map<Integer, PatientState> getCurrentStates(PatientSet ps,
			ProgramWorkflow wf);

	@Transactional(readOnly=true)
	public Map<Integer, PatientProgram> getCurrentPatientPrograms(
			PatientSet ps, Program program);

	@Transactional(readOnly=true)
	public Map<Integer, PatientProgram> getPatientPrograms(PatientSet ps,
			Program program);
	
	@Transactional(readOnly=true)
	public Map<Integer, List<Relationship>> getRelationships(PatientSet ps, RelationshipType relType);

	/**
	 * @return all active drug orders whose drug concept is in the given set (or all drugs if that's null) 
	 */
	@Transactional(readOnly=true)
	public Map<Integer, List<DrugOrder>> getCurrentDrugOrders(PatientSet ps,
			Concept drugSet);

	/**
	 * @return all active or finished drug orders whose drug concept is in the given set (or all drugs if that's null) 
	 */
	@Transactional(readOnly=true)
	public Map<Integer, List<DrugOrder>> getDrugOrders(PatientSet ps,
			Concept drugSet);
	
	public void setMyPatientAnalysis(PatientAnalysis pa);

	@Transactional(readOnly=true)
	public PatientAnalysis getMyPatientAnalysis();

	public enum Modifier {
		LESS_THAN ("<"),
		LESS_EQUAL ("<="),
		EQUAL ("="),
		GREATER_EQUAL (">="),
		GREATER_THAN (">");

		public final String sqlRep;
		Modifier(String sqlRep) {
			this.sqlRep = sqlRep;
		}
		public String getSqlRepresentation() {
			return sqlRep;
		}
	}
	
	public enum TimeModifier {
		ANY,
		NO,
		FIRST,
		LAST,
		MIN,
		MAX,
		AVG;
	}

	public enum BooleanOperator {
		AND,
		OR,
		NOT;
	}
	
	// probably should combine this with TimeModifier
	public enum GroupMethod {
		ANY,
		NONE;
	}
}