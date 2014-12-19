
public class IObject_structure extends Object_structure {
	//used for place and objects
	String token = new String(); //unique token identifier
	String name = new String();
	String cd_form = new String();// cd form of the object
	String part_of_speech = new String(); // what part of speech it belongs to
	String type = new String();
	Adjective_structure[] adjective = new Adjective_structure[10]; //what is the adjectives that defines it
	Object_structure poss_by = new Object_structure(); // by whom is this object possessed
	int size; // size of the object
	int physical_state; // physical state of the object
	int quality; // quality of the object
	public String get_state(int state_value, String state_type)
	{
		
		if(state_type.equals("physical_state"))
		{
			switch(state_value) // various parameterised physical states
			{
				case 10: return "ok";
				case -3 : return "hurt";
				case -5 : return "broken";
				case -9 : return "harmed";
				case -10 : return "dead";
			}
		}
		if(state_type.equals("size")) //various parameterised sizes
		{
			switch(state_value)
			{
				case 10: return "massive";
				case 9 : return "huge";
				case 8 : return "normal";
				case 7 : return "small";
				case 6 : return "tiny";
			}
		}
		if(state_type.equals("quality")) //various parameterised degrees of quality of the object
		{
			switch(state_value)
			{
				case 10: return "superb";
				case 9 : return "good";
				case 8 : return "average";
				case 7 : return "bad";
				case 6 : return "poor";
			}
		}
		return "invalid";
	}
}
