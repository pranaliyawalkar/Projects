//THIS IS THE CLASS FILE OF ACTOR_PLACE_STRUCTURE. IT INHERITS THE IOBJECT_STRUCTURE CLASS.
public class Actor_Place_structure extends IObject_structure{
	String token = new String(); //UNIQUE IDENTIFIER OF THE ENTITY
	String name = new String(); //NAME OF THE ENTITY
	String cd_form = new String(); //CD FORM OF THE ENTITY
	/*CD FORM OF ACTOR - 
	 * (PERSON
	 * 	(NAME VAR1)
	 * 	(POSSBY VAR2)
	 * 	(ISA (ADJECTIVE VAR3)) 
	 * )
	 * CD FORM OF PLACE - 
	 * (PLACE
		(NAME VAR1)
		(POSSBY VAR2)
		(ISA (ADJECTIVE VAR3))
		)
	 */
	String part_of_speech = new String(); //PART OF SPEECH. EG NOUN, VERB ETC
	String type = new String(); //TYPE OF THE ENTITY STATING WHETHER IT IS A PERSON, OR A PLACE.
	Adjective_structure[] adjective = new Adjective_structure[10]; //AN ARRAY OF 10 ADJECTIVES FOR THE ENTITY.
	String specific = new String();//TELLS IF THE ENTITY 
	Object_structure poss_by = new Object_structure();//GIVES THE POSSESSOR OF THE ENTITY.
	int mental_state; //MENTAL STATE OF THE ENTITY
	int physical_state; //PHYSICAL STATE OF THE ENTITY 
	int health; //HEALTH OF THE ENTITY
	int anger; //ANGER STATE OF THE ENTITY 
	int fear; //FEAR STATE OF THE ENTITY 
	
	
	//CONSTRUCTOR CLASS INITIALIZING ALL THE ENTITY STATES TO DEFAULT 999
	public Actor_Place_structure() 
	{
		mental_state = 999;
		physical_state=999;
		health=999;
		anger=999;
		fear=999;
	}
	//A FUNCTION TO GET THE EXACT MEANING OF THE ENTITY'S STATE VARIABLE AND THE VALUE HELD BY THE VARIABLE 
	public String get_state(int state_value, String state_type)
	{
		if(state_type.equals("mental_state"))
		{
			//BASED ON THE VALUES OF THE VRIABLE MENTAL STATE, IT GIVES THE EXACT MENTAL STATE OF THE ENTITY
			switch(state_value)
			{
				case 10: return "ecstatic";
				case 5 : return "happy";
				case 2 : return "pleased";
				case 0 : return "ok";
				case -2 : return "sad";
				case -3 : return "upset";
				case -5 : return "depressed";
				case -9 : return "catatonic";
				default : return "invalid";
			}
		}
		if(state_type.equals("physical_state"))
		{
			//BASED ON THE VALUES OF THE VRIABLE PHYSICAL STATE, IT GIVES THE EXACT PHYSICAL STATE OF THE ENTITY
			switch(state_value)
			{
				case 10: return "ok";
				case -3 : return "hurt";
				case -5 : return "injured";
				case -9 : return "harmed";
				case -10 : return "dead";
			}
		}
		if(state_type.equals("anger"))
		{
			//BASED ON THE VALUES OF THE VRIABLE ANGER STATE, IT GIVES THE EXACT ANGER STATE OF THE ENTITY
			switch(state_value)
			{
				case 0: return "calm";
				case -2 : return "upset";
				case -3 : return "irked";
				case -5 : return "angry";
				case -8 : return "enraged";
				case -9 : return "furious";
			}
		}
		if(state_type.equals("health"))
		{
			//BASED ON THE VALUES OF THE VRIABLE HEALTH STATE, IT GIVES THE EXACT HEALTH STATE OF THE ENTITY
			switch(state_value)
			{
				case 10 : return "perfect";
				case 7 : return "tip top";
				case 0 : return "all right";
				case -7 : return "sick";
				case -9 : return "gravely ill";
				case -10 : return "dead";
			}
		}
		return "invalid";
	}
}
