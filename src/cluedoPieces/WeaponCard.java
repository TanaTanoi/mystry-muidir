package cluedoPieces;


public class WeaponCard implements Card {
	
	WeaponType type;
	
	public WeaponCard(WeaponType type){
		this.type = type;
	}
	
	public WeaponCard(String weapon){
		try {
			type = WeaponType.valueOf(weapon.toUpperCase());
		}catch(IllegalArgumentException e){}
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof WeaponCard){
			WeaponCard c = (WeaponCard)o;
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
