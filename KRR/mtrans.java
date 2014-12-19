
public class mtrans extends action { //class defintion for actions of MTRANS type
	Actor_Place_structure source = new Actor_Place_structure(); // intial 
	Actor_Place_structure dest = new Actor_Place_structure(); //final
	Actor_Place_structure actor = new Actor_Place_structure(); // who does the action
	Object_structure object = new Object_structure(); //only abstract objects
}

/**The CD Form for verbs of this type is
							(MTRANS
							(FROM VAR1)
							(OBJECT VAR2)
							(ACTOR VAR3)
							(CONC_TENSE VAR4)
							(USING VAR5)
							(ADVERB VAR6)
							)
/*
