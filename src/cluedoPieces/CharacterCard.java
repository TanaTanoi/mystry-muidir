package cluedoPieces;


public class CharacterCard implements Card{
	Person type;
	
	public CharacterCard(Person type){
		this.type = type;
	}
	
	public CharacterCard(String person){
		try {
			type = Person.valueOf(person.toUpperCase());
		}catch(IllegalArgumentException e){}
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof CharacterCard){
			CharacterCard c = (CharacterCard)o;
			return type == c.type;
		}else{
			return false;
		}
		
	}
	
	@Override
	public String toString(){
		if (type != null) return type.toString();
		else return null;
	}
	
	@Override
	public int hashCode(){
		return type.toString().hashCode();
	}
	
}
