//THIS IS THE CLASS FILE OF ACTION. ALL VERBS ARE OF ACTION TYPE. ALL VERBS INHERIT THIS ACTION CLASS.
public class action extends Object_structure {
	String name = new String(); //NAME OF THE ENTITY
	String token = new String(); //UNIQUE IDENTIFIER OF THE ENTITY
	String cd_form = new String(); //CD-FORM OF THE ENTITY
	String part_of_speech = new String(); //PART OF SPEECH OF THE ENTITY. LIKE NOUN VERB.
	Object_structure using_instrument = new Object_structure(); //THE INSTRUMENT OF THE ACTION. 
	//INSTRUMENT CAN BE OF ACTION TYPE OR IOBJECT_STRUCTURE TYPE
	String conceptual_tense = new String(); //eq past, present, SINGULAR PRESENT AND PARTICIPLE
	Adverb_structure[] adverb = new Adverb_structure[10]; //ASN ARRAY OF 10 ADVERBS FOR THE ACTION ENTITY
}
