// THIS IS THE CLASS FILE OF THE VERBS OF "ATTEND" TYPE. THIS EXTENDS THE ACTION CLASS. ALL VERBS ARE OF TYPE ACTION.
public class attend extends action {
	Actor_Place_structure Actor = new Actor_Place_structure(); //STATES THE ACTOR OF THE ATTEND ACTIONS
	IObject_structure organ = new IObject_structure();//STATES THE ORGAN OF THE ATTEND ACTIONS. EG EYE, EAR
	IObject_structure object = new IObject_structure(); //STATES THE OBJECT OF THE ATTEND ACTIONS. THIS CAN BE AN ACTOR OR A PLACE OR AN OBJECT
	/*
	 * cd - form -
	 * (ATTEND 
		(ACTOR VAR1) 
		(ORGAN VAR2) 
		(OBJECT  VAR3)
		(CONC_TENSE VAR4)
		(USING VAR5)
		(ADVERB VAR6)
		)
	 */
}
