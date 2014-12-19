
public class speak extends action {
	Actor_Place_structure source = new Actor_Place_structure(); //who speaks
	Actor_Place_structure dest = new Actor_Place_structure(); //to whom
	IObject_structure object = new IObject_structure(); //only abstract objects
	int interrogative; //1 if interrogative.
}
/*
	The CD form for words of this type are
							(SPEAK
					(FROM VAR1)
					(TO VAR2)
					(OBJECT VAR3)
					(CONC_TENSE VAR4)
					(USING VAR5)
					(ADVERB VAR6)
					)

*/
