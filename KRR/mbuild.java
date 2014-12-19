
public class mbuild extends action{
	IObject_structure initial_object = new IObject_structure(); //abstract object (initial thought)
	IObject_structure final_object = new IObject_structure(); //abstract object ( inference)
	Actor_Place_structure actor = new Actor_Place_structure(); //who does the action
}
/*The CD form for verbs of this type is
									(MBUILD
									(ACTOR VAR1)
									(INITIAL VAR2)
									(FINAL VAR3)
									(CONC_TENSE VAR4)
									(USING VAR5)
									(ADVERB VAR6)
									)
*/
