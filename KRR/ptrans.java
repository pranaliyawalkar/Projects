
public class ptrans extends action {
	
	Actor_Place_structure actor = new Actor_Place_structure(); //actor of the action
	Actor_Place_structure source = new Actor_Place_structure();//intial location
	Actor_Place_structure dest = new Actor_Place_structure(); //final location
	IObject_structure object = new IObject_structure(); //what is being moved
	
}
/*
The CD form for verbs of this type is
							(PTRANS
							(FROM VAR1)
							(TO VAR2)
							(OBJECT VAR3)
							(ACTOR VAR4)
							(CONC_TENSE VAR5)
							(USING VAR6)
							(ADVERB VAR7)
							)
*/
